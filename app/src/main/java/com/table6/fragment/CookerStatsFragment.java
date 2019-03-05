package com.table6.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.net.ssl.HttpsURLConnection;

public class CookerStatsFragment extends Fragment {

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
        secureLidToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO: In any case, run a thread to attempt to secure/unsecure lid
                if (isChecked) {
                    // The toggle is enabled. Attempt to secure lid.
                    JSONObject json = new JSONObject();
                    try {
                        json.put("status", "secure");
                        new RetrieveFeedTask().execute(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // The toggle is disabled. Attempt to unsecure lid.
                    JSONObject json = new JSONObject();
                    try {
                        json.put("status", "unsecure");
                        new RetrieveFeedTask().execute(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // https://www.tutorialspoint.com/android/android_json_parser.htm
    // https://stackoverflow.com/questions/42767249/android-post-request-with-json
    public class RetrieveFeedTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... jsons) {
            HttpURLConnection connection = null;
            StringBuilder sb = new StringBuilder();

            try {
                connection = (HttpURLConnection) new URL("http://3.18.34.75:5000/lid_status").openConnection();
                connection.connect();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept","application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.connect();

                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                os.writeBytes(URLEncoder.encode(jsons[0].toString(), "UTF-8"));

                os.flush();
                os.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Could not contact server",
                                Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
            }

            return null;
        }
    }
}
