package com.table6.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.table6.activity.R;
import com.table6.fragment.ControlCookTimeFragment;
import com.table6.fragment.ControlTemperatureFragment;
import com.table6.fragment.ControlTemperatureRadioFragment;
import com.table6.object.ServerFeed;

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
        }

        controlTemperatureRadioFragment = ControlTemperatureRadioFragment.newInstance();
        fragmentTransaction.add(R.id.slowCookerViewFragmentContainer, controlTemperatureRadioFragment);

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
                            e.printStackTrace();
                        }
                    }
                }

                ServerFeed temperatureFeed = new ServerFeed();
                if (controlTemperatureFragment != null) {
                    String temperature = controlTemperatureFragment.getTemperature();
                    if (temperature.length() > 0) {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("temperature", temperature);
                            temperatureFeed = new ServerFeed("control_temperature", json);
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                            e.printStackTrace();
                        }
                    }
                }

                new PushFeedTask().execute(cookTimeFeed, temperatureFeed);
            }
        });
    }

    // https://www.tutorialspoint.com/android/android_json_parser.htm
    public class PushFeedTask extends AsyncTask<ServerFeed, Void, Void> {

        @Override
        protected Void doInBackground(ServerFeed... feeds) {
            HttpURLConnection connection = null;
            StringBuilder sb = new StringBuilder();

            for(ServerFeed feed : feeds) {
                System.out.println("SlowCookerView: feed=" + feed);

                if (feed.getDirectory().length() != 0) {
                    try {
                        connection = (HttpURLConnection) new URL(R.string.server_address + feed.getDirectory()).openConnection();

                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        connection.setRequestProperty("Accept","application/json");
                        connection.setDoOutput(true);

                        connection.connect();

                        // Be sure to specify UTF-8 for JSON byte array.
                        OutputStream os = connection.getOutputStream();
                        os.write(feed.getJson().toString().getBytes("UTF-8"));
                        os.close();

                        int responseCode = connection.getResponseCode();

                        if (responseCode == HttpsURLConnection.HTTP_OK) {
                            String line;
                            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }

                            // Report success

                        } else if(responseCode == HttpURLConnection.HTTP_CONFLICT) {
                            String line;
                            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }

                            // Report failure.

                        }

                        Thread.sleep(1000);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();

//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getActivity().getApplicationContext(),
//                                "Could not contact server",
//                                Toast.LENGTH_LONG).show();
//                    }
//                });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        connection.disconnect();
                    }

                }
            }

            return null;
        }
    }
}
