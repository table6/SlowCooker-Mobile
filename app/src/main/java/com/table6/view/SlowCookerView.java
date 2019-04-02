package com.table6.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.table6.activity.R;
import com.table6.fragment.ControlCookTimeFragment;
import com.table6.fragment.ControlTemperatureFragment;
import com.table6.fragment.ControlTemperatureRadioFragment;
import com.table6.object.ServerFeed;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SlowCookerView extends AppCompatActivity {

    private ControlCookTimeFragment controlCookTimeFragment;
    private ControlTemperatureFragment controlTemperatureFragment;
    private ControlTemperatureRadioFragment controlTemperatureRadioFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slowcooker_view);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        controlCookTimeFragment = ControlCookTimeFragment.newInstance();
        fragmentTransaction.add(R.id.slowCookerViewFragmentContainer, controlCookTimeFragment);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String type = sharedPreferences.getString(getString(R.string.preference_file_type), "");
        if (type.equals("probe")) {
            controlTemperatureFragment = ControlTemperatureFragment.newInstance();
            fragmentTransaction.add(R.id.slowCookerViewFragmentContainer, controlTemperatureFragment);
        } else {
            controlTemperatureRadioFragment = ControlTemperatureRadioFragment.newInstance();
            fragmentTransaction.add(R.id.slowCookerViewFragmentContainer, controlTemperatureRadioFragment);
        }

        fragmentTransaction.commit();

        Button confirmBtn = (Button) findViewById(R.id.slowCookerViewConfirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ServerFeed cookTimeFeed = new ServerFeed();
                if (controlCookTimeFragment != null) {
                    String cookTime = controlCookTimeFragment.getCookTime();
                    if(cookTime.length() > 0) {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("start_time", cookTime);
                            cookTimeFeed = new ServerFeed("control_cook_time", json);
                        } catch (JSONException e) {
                            Log.e("", e.getMessage());
                        }
                    }
                }

                ServerFeed temperatureFeed = new ServerFeed();
                if (controlTemperatureFragment != null) {
                    String temperature = controlTemperatureFragment.getTemperature();
                    if (temperature.length() > 0) {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("type", "probe");
                            json.put("temperature", temperature);
                            json.put("measurement", "F");
                            temperatureFeed = new ServerFeed("control_temperature", json);
                        } catch (JSONException e) {
                            Log.e("", e.getMessage());
                        }
                    }
                }

                if (controlTemperatureRadioFragment != null) {
                    String heatMode = controlTemperatureRadioFragment.getHeatMode();
                    if (heatMode.length() > 0) {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("heat_mode", heatMode);

                            // send json to server

                        } catch (JSONException e) {
                            Log.e("", e.getMessage());
                        }
                    }
                }

                new PushFeedTask().execute(cookTimeFeed, temperatureFeed);
            }
        });
    }

    public class PushFeedTask extends AsyncTask<ServerFeed, Void, Void> {
        @Override
        protected Void doInBackground(ServerFeed... feeds) {
            for (ServerFeed feed : feeds) {
                if (feed.getDirectory().length() != 0) {
                    try {
                        HttpURLConnection connection = (HttpURLConnection) new URL(getString(R.string.server_address) + feed.getDirectory()).openConnection();

                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        connection.setRequestProperty("Accept", "application/json");
                        connection.setDoOutput(true);

                        connection.connect();

                        // Be sure to specify UTF-8 for JSON byte array.
                        OutputStream os = connection.getOutputStream();
                        os.write(feed.getJson().toString().getBytes("UTF-8"));
                        os.close();

                        int responseCode = connection.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_CONFLICT) {
                            // Report failure.
                        }

                        connection.disconnect();
                    } catch (MalformedURLException e) {
                        Log.e("", e.getMessage());
                    } catch (IOException e) {
                        Log.e("", e.getMessage());
                    }
                }

            }

            return null;
        }
    }
}
