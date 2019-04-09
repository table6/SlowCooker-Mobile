package com.table6.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.table6.activity.R;
import com.table6.fragment.ControlCookModeRadioFragment;
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

    FragmentPagerAdapter fragmentPagerAdapter;

    private ControlCookTimeFragment controlCookTimeFragment;
    private ControlCookModeRadioFragment controlCookModeFragment;
    private ControlTemperatureFragment controlTemperatureFragment;
    private ControlTemperatureRadioFragment controlTemperatureRadioFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slowcooker_view);

        ViewPager viewPager = (ViewPager) findViewById(R.id.slowCookerViewViewPager);
        fragmentPagerAdapter = new SlowCookerPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                // Called when the page changes

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        controlCookModeFragment = ControlCookModeRadioFragment.newInstance();
//        fragmentTransaction.add(R.id.slowCookerViewFragmentContainer, controlCookModeFragment);
//
//        controlCookTimeFragment = ControlCookTimeFragment.newInstance();
//        fragmentTransaction.add(R.id.slowCookerViewFragmentContainer, controlCookTimeFragment);
//
//        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//        String type = sharedPreferences.getString(getString(R.string.preference_file_type), "");
//        if (type.equals("probe")) {
//            controlTemperatureFragment = ControlTemperatureFragment.newInstance();
//            fragmentTransaction.add(R.id.slowCookerViewFragmentContainer, controlTemperatureFragment);
//        } else {
//            controlTemperatureRadioFragment = ControlTemperatureRadioFragment.newInstance();
//            fragmentTransaction.add(R.id.slowCookerViewFragmentContainer, controlTemperatureRadioFragment);
//        }
//
//        fragmentTransaction.commit();
//
//        Button confirmBtn = (Button) findViewById(R.id.slowCookerViewConfirmBtn);
//        confirmBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ServerFeed cookTimeFeed = new ServerFeed();
//                if (controlCookTimeFragment != null) {
//                    String cookTime = controlCookTimeFragment.getCookTime();
//                    if(cookTime.length() > 0) {
//                        JSONObject json = new JSONObject();
//                        try {
//                            json.put("start_time", cookTime);
//                            cookTimeFeed = new ServerFeed("control_cook_time", json);
//                        } catch (JSONException e) {
//                            Log.e("", e.getMessage());
//                        }
//                    }
//                }
//
//                ServerFeed temperatureFeed = new ServerFeed();
//                if (controlTemperatureFragment != null) {
//                    String temperature = controlTemperatureFragment.getTemperature();
//                    if (temperature.length() > 0) {
//                        JSONObject json = new JSONObject();
//                        try {
//                            json.put("type", "probe");
//                            json.put("temperature", temperature);
//                            json.put("measurement", "F");
//                            temperatureFeed = new ServerFeed("control_temperature", json);
//                        } catch (JSONException e) {
//                            Log.e("", e.getMessage());
//                        }
//                    }
//                } else if (controlTemperatureRadioFragment != null) {
//                    String heatMode = controlTemperatureRadioFragment.getHeatMode();
//                    if (heatMode.length() > 0) {
//                        JSONObject json = new JSONObject();
//                        try {
//                            json.put("type", "manual");
//                            json.put("temperature", heatMode);
//                            json.put("measurement", "N/A");
//                            temperatureFeed = new ServerFeed("control_temperature", json);
//                        } catch (JSONException e) {
//                            Log.e("", e.getMessage());
//                        }
//                    }
//                }
//
//                if (controlCookModeFragment != null) {
//                    String heatMode = controlTemperatureRadioFragment.getHeatMode();
//                    if (heatMode.length() > 0) {
//                        JSONObject json = temperatureFeed.getJson();
//                        try {
//                            json.put("type", "manual");
//                            temperatureFeed = new ServerFeed("control_temperature", json);
//                        } catch (JSONException e) {
//                            Log.e("", e.getMessage());
//                        }
//                    }
//                }
//
//                new PushFeedTask().execute(cookTimeFeed, temperatureFeed);
//            }
//        });
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

                        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                            // Report failure.
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Could not send control message",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
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

    public class SlowCookerPagerAdapter extends FragmentPagerAdapter {

        private int NUM_ITEMS = 4;
        private int PAGE_COOK_MODE = 0;
        private int PAGE_COOK_TIME = 1;
        private int PAGE_COOK_TEMPERATURE = 2;
        private int PAGE_COOK_TEMPERATURE_RADIO = 3;

        public SlowCookerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch(i) {
                case 0:
                    return ControlCookModeRadioFragment.newInstance();
                case 1:
                    return ControlCookTimeFragment.newInstance();
                case 2:
                    return ControlTemperatureFragment.newInstance();
                case 3:
                    return ControlTemperatureRadioFragment.newInstance();
                default:
                    break;
            }

            return null;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }

}
