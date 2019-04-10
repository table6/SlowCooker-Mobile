package com.table6.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.table6.fragment.ControlConfirmFragment;
import com.table6.fragment.ControlCookModeRadioFragment;
import com.table6.fragment.ControlCookTimeFragment;
import com.table6.fragment.ControlTemperatureFragment;
import com.table6.fragment.ControlTemperatureRadioFragment;
import com.table6.object.ServerFeed;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ControlSlowCookerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private Button nextBtn;
    private Button cancelBtn;
    private ArrayMap<String, String> userChoices;

    final private int PAGE_COOK_MODE = 0;
    final private int PAGE_COOK_TIME = 1;
    final private int PAGE_COOK_TEMP = 2;
    final private int PAGE_COOK_TEMP_RADIO = 3;
    final private int PAGE_CONFIRM = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slowcooker_view);

        userChoices = new ArrayMap<>();

        viewPager = (ViewPager) findViewById(R.id.controlSlowCookerActivityViewPager);
        fragmentPagerAdapter = new SlowCookerPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);

        nextBtn = (Button) findViewById(R.id.controlSlowCookerActivityNextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                Fragment fragment = fragmentPagerAdapter.getItem(currentItem);

                int nextItem = currentItem + 1;

                if (fragment instanceof ControlCookModeRadioFragment) {
                    String cookMode = ((ControlCookModeRadioFragment) fragment).getCookMode();
                    if (cookMode.equals("probe")) {
                        nextItem = PAGE_COOK_TEMP;
                    } else if (cookMode.equals("manual")) {
                        nextItem = PAGE_COOK_TEMP_RADIO;
                    }

                    userChoices.put("Cook mode", cookMode);

                } else if (fragment instanceof ControlCookTimeFragment) {
                    String cookTime = ((ControlCookTimeFragment) fragment).getCookTime();
                    userChoices.put("Cook time", cookTime);

                    nextItem = PAGE_CONFIRM;
                } else if (fragment instanceof ControlTemperatureFragment) {
                    String temperature = ((ControlTemperatureFragment) fragment).getTemperature();
                    userChoices.put("Cook temperature", temperature);

                    nextItem = PAGE_CONFIRM;
                } else if (fragment instanceof ControlTemperatureRadioFragment) {
                    String temperatureMode = ((ControlTemperatureRadioFragment) fragment).getHeatMode();
                    userChoices.put("Cook temperature mode", temperatureMode);

                    nextItem = PAGE_CONFIRM;
                }

                if(nextItem == PAGE_CONFIRM) {
                    ControlConfirmFragment controlConfirmFragment = (ControlConfirmFragment) fragmentPagerAdapter.getItem(PAGE_CONFIRM);
                    controlConfirmFragment.populateView(userChoices);
                }

                viewPager.setCurrentItem((nextItem < fragmentPagerAdapter.getCount() ? nextItem : fragmentPagerAdapter.getCount()));
            }
        });

        cancelBtn = (Button) findViewById(R.id.controlSlowCookerActivityCancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoices = new ArrayMap<>();
                viewPager.setCurrentItem(PAGE_COOK_MODE);
            }
        });

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

        private SparseArray<Fragment> fragmentMap;

        private int NUM_ITEMS = 5;

        public SlowCookerPagerAdapter(FragmentManager fm) {
            super(fm);

            fragmentMap = new SparseArray<>();
            for(int i = 0; i < NUM_ITEMS; i++) {
                switch(i) {
                    case PAGE_COOK_MODE:
                        fragmentMap.append(i, ControlCookModeRadioFragment.newInstance());
                        break;
                    case PAGE_COOK_TIME:
                        fragmentMap.append(i, ControlCookTimeFragment.newInstance());
                        break;
                    case PAGE_COOK_TEMP:
                        fragmentMap.append(i, ControlTemperatureFragment.newInstance());
                        break;
                    case PAGE_COOK_TEMP_RADIO:
                        fragmentMap.append(i, ControlTemperatureRadioFragment.newInstance());
                        break;
                    case PAGE_CONFIRM:
                        fragmentMap.append(i, ControlConfirmFragment.newInstance());
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentMap.get(i, null);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }

}
