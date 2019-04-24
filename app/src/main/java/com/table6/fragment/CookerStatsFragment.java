package com.table6.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.table6.activity.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class CookerStatsFragment extends ServerFeedFragment {

    public CookerStatsFragment() {
        // Required empty public constructor
    }

    public static CookerStatsFragment newInstance() {
        CookerStatsFragment fragment = new CookerStatsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cooker_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        CookTimeFragment cookTimeFragment = CookTimeFragment.newInstance();
        transaction.add(R.id.cookerStatsContainer, cookTimeFragment);

        TemperatureFragment temperatureFragment = TemperatureFragment.newInstance();
        transaction.add(R.id.cookerStatsContainer, temperatureFragment);

        transaction.commit();

        ToggleButton secureLidToggleBtn = (ToggleButton) view.findViewById(R.id.cookerStatsSecureLidToggleBtn);
        new RetrieveFeedTask().execute();

        secureLidToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled. Attempt to secure lid.
                    JSONObject json = new JSONObject();
                    try {
                        json.put("status", "secure");
                        new PushFeedTask().execute(json);
                    } catch (JSONException e) {
                        Log.e("", e.getMessage());
                    }
                } else {
                    // The toggle is disabled. Attempt to unsecure lid.
                    JSONObject json = new JSONObject();
                    try {
                        json.put("status", "unsecure");
                        new PushFeedTask().execute(json);
                    } catch (JSONException e) {
                        Log.e("", e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    protected void update() {
        new RetrieveFeedTask().execute();
    }

    // https://www.tutorialspoint.com/android/android_json_parser.htm
    // https://stackoverflow.com/questions/21404252/post-request-send-json-data-java-httpurlconnection
    public class PushFeedTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... jsons) {

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(getString(R.string.server_address) + "control_lid_status").openConnection();

                // Configure header.
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Accept","application/json");
                connection.setDoOutput(true);

                connection.connect();

                // Be sure to specify UTF-8 for JSON byte array.
                OutputStream os = connection.getOutputStream();
                os.write(jsons[0].toString().getBytes("UTF-8"));
                os.close();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    // Report failure.
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Could not send lid toggle control message",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (MalformedURLException e) {
                Log.e("", e.getMessage());
            } catch (IOException e) {
                Log.e("", e.getMessage());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Could not contact server",
                                Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                Log.e("", e.getMessage());
            }

            return null;
        }
    }

    public class RetrieveFeedTask extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... voids) {

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(getString(R.string.server_address) + "lid_status").openConnection();
                connection.connect();

                int responseCode = connection.getResponseCode();
                connection.disconnect();

                StringBuilder sb = new StringBuilder();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    return new JSONObject(sb.toString());
                }
            } catch (MalformedURLException e) {
                Log.e("", e.getMessage());
            } catch (IOException e) {
                Log.e("", e.getMessage());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Could not contact server",
                                Toast.LENGTH_LONG).show();
                    }
                });
            } catch (JSONException e) {
                Log.e("", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);

            if (json != null) {
                try {
                    final String status = json.getString("status");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToggleButton secureLidToggleBtn = (ToggleButton) getView().findViewById(R.id.cookerStatsSecureLidToggleBtn);
                            secureLidToggleBtn.setChecked(status.equals("secure"));
                        }
                    });
                } catch (JSONException e) {
                    Log.e("", e.getMessage());
                }
            }
        }
    }
}
