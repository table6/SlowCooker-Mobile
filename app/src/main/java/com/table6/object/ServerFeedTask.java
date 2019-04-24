package com.table6.object;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ServerFeedTask {
    private Context context;
    private String[] directories;

    public ServerFeedTask(Context context, String[] directories) {
        this.context = context;
        this.directories = directories;
    }

    public void getFeed() {
        new RetrieveFeedTask().execute(directories);
    }

    public class RetrieveFeedTask extends AsyncTask<String[], Void, Void> {
        @Override
        protected Void doInBackground(String[]... directories) {
            SharedPreferences.Editor editor = context.getSharedPreferences("", Context.MODE_PRIVATE).edit();

            for (String directory : directories[0]) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(
                            "http://3.18.34.75:5000/" + directory).openConnection();
                    connection.connect();

                    int responseCode = connection.getResponseCode();
                    connection.disconnect();

                    StringBuilder sb = new StringBuilder();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(connection.getInputStream()));

                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }

                        // https://stackoverflow.com/questions/5918328/is-it-ok-to-save-a-json-array-in-sharedpreferences
                        editor.putString(directory, sb.toString());
                    }
                } catch (MalformedURLException e) {
                    Log.e("", e.getMessage());
                } catch (IOException e) {
                    Log.e("", e.getMessage());
                }
            }

            editor.apply();

            return null;
        }
    }

}
