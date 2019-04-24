package com.table6.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.table6.activity.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class TemperatureFragment extends ServerFeedFragment {

    private static final int MODE_FAHRENHEIT = 0;
    private static final int MODE_CELSIUS = 1;
    private static final int MODE_NONE = 2;

    private TextView tempTxt;
    private TextView modeTxt;

    public TemperatureFragment() {

    }

    public static TemperatureFragment newInstance() {
        TemperatureFragment fragment = new TemperatureFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temperature, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.tempTxt = view.findViewById(R.id.tempFragmentTempTxt);
        this.modeTxt = view.findViewById(R.id.tempFragmentModeTxt);
    }

    /**
     * Sets the given temperature mode to be displayed in the appropriate TextView.
     * @param x the mode to be displayed.
     */
    public void setMode(int x) {
        if (x == MODE_FAHRENHEIT) {
            this.modeTxt.setText("F");
        } else if (x == MODE_CELSIUS){
            this.modeTxt.setText("C");
        } else if (x == MODE_NONE) {
            this.modeTxt.setText(" ");
        }

        // Write new mode to shared preferences.
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.preference_file_measurement), x);
        editor.apply();
    }

    /**
     * Sets the given temperature to be displayed in the appropriate TextView.
     * @param x the temperature to be displayed.
     */
    public void setTemperature(String x) {
        try {
            this.tempTxt.setText(String.format(Locale.US, "%.1f", Double.parseDouble(x)));
        } catch (NumberFormatException e) {
            this.tempTxt.setText(x);
        }
    }

    @Override
    protected void update() {
        if(this.tempTxt != null && this.modeTxt != null) {
            new RetrieveFeedTask().execute();
        }
    }

    // https://www.tutorialspoint.com/android/android_json_parser.htm
    public class RetrieveFeedTask extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... voids) {

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(getString(R.string.server_address) + "temperature").openConnection();
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
                    final String type = json.getString("type");

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.preference_file_type), type);
                    editor.apply();

                    final String temperature = json.getString("temperature");
                    final String measurement = json.getString("measurement");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!type.equals("probe")) {
                                // Don't show C or F
                                setMode(MODE_NONE);

                            } else {
                                setMode((measurement.equals("F") ? MODE_FAHRENHEIT : MODE_CELSIUS));
                            }

                            setTemperature(temperature);
                        }
                    });
                } catch (JSONException e) {
                    Log.e("", e.getMessage());
                }
            }
        }
    }
}
