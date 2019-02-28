package com.table6.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.table6.activity.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ServerFeedFragment extends Fragment implements {


    public ServerFeedFragment() {
        // Required empty public constructor
    }

    public static ServerFeedFragment newInstance(String param1, String param2) {
        ServerFeedFragment fragment = new ServerFeedFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_server_feed, container, false);
    }

    // https://www.tutorialspoint.com/android/android_json_parser.htm
    public class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection connection = null;
            StringBuilder sb = new StringBuilder();

            try {
                connection = (HttpURLConnection) new URL("http://192.168.0.186:5000/cook_time" + strings[0]).openConnection();
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.connect();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    // Do something with response.
                    JSONObject json = new JSONObject(sb.toString());
                    System.out.println(json);

                } else if (responseCode == HttpURLConnection.HTTP_CONFLICT) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    // Report failure.
                    System.out.println("\t\tCookerStatsFragment: responseError=" + sb.toString());
                } else {
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }

            return null;
        }
    }

}
