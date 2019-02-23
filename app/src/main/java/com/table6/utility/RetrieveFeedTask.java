package com.table6.utility;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

// https://stackoverflow.com/questions/36132059/android-httpurlconnection-connect-always-failed

public class RetrieveFeedTask extends AsyncTask<URL, Void, Void> {
    private RetrieveFeedTaskListener listener;
    private HttpURLConnection connection = null;

    public RetrieveFeedTask(Object listener) {
        this.listener = (RetrieveFeedTaskListener) listener;
    }

    @Override
    protected Void doInBackground(URL... urls) {
        String response = "";
        String responseError = "";

        InputStream in = null;
        try {
            this.connection = (HttpURLConnection) urls[0].openConnection();
            this.connection.setReadTimeout(15000);
            this.connection.setConnectTimeout(15000);
            this.connection.connect();

            int responseCode = this.connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while ((line = br.readLine()) != null) {
                    response += line;
                }

                this.listener.onRetrieveFeedTaskResponse(response);
            } else if(responseCode == HttpURLConnection.HTTP_CONFLICT) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                while ((line = br.readLine()) != null) {
                    responseError += line;
                }

                this.listener.onRetrieveFeedTaskResponse(responseError);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        return null;
    }

    public interface RetrieveFeedTaskListener {
        void onRetrieveFeedTaskResponse(String x);
    }
}
