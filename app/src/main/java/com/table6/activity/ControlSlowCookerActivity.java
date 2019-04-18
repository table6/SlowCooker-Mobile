package com.table6.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.table6.fragment.LockableViewPager;
import com.table6.object.ServerFeed;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ControlSlowCookerActivity extends AppCompatActivity {

    private LockableViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private Button nextBtn;
    private Button cancelBtn;
    private ArrayMap<String, String> userChoices;
    private ArrayMap<Integer, String> pageTranslations;
    private String userSelectedMode;

    final private int PAGE_COOK_MODE = 0;
    final private int PAGE_COOK_TIME = 1;
    final private int PAGE_COOK_TEMP = 2;
    final private int PAGE_COOK_TEMP_RADIO = 3;
    final private int PAGE_CONFIRM = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slowcooker_view);

        pageTranslations = new ArrayMap<>();
        pageTranslations.put(PAGE_COOK_MODE, "Cook mode");
        pageTranslations.put(PAGE_COOK_TIME, "Cook time");
        pageTranslations.put(PAGE_COOK_TEMP, "Cook temperature");
        pageTranslations.put(PAGE_COOK_TEMP_RADIO, "Cook temperature mode");

        userChoices = new ArrayMap<>();

        viewPager = (LockableViewPager) findViewById(R.id.controlSlowCookerActivityViewPager);
        viewPager.setSwipeable(false);

        fragmentPagerAdapter = new SlowCookerPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);

        nextBtn = (Button) findViewById(R.id.controlSlowCookerActivityNextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                Fragment fragment = fragmentPagerAdapter.getItem(currentItem);

                int nextItem = currentItem + 1;

                if (fragment instanceof ControlConfirmFragment) {
                    try {
                        JSONObject temperatureJson = new JSONObject();
                        temperatureJson.put("type", userSelectedMode);

                        if (userSelectedMode.equals("probe")) {
                            String[] tokens = userChoices.get(pageTranslations.get(PAGE_COOK_TEMP)).split(" ");

                            temperatureJson.put("temperature", tokens[0]);
                            temperatureJson.put("measurement", tokens[1]);
                        } else {
                            temperatureJson.put("temperature", userChoices.get(pageTranslations.get(PAGE_COOK_TEMP_RADIO)));
                            temperatureJson.put("measurement", "N/A");

                            if (userSelectedMode.equals("program")) {
                                JSONObject timeJson = new JSONObject();
                                timeJson.put("start_time", userChoices.get(pageTranslations.get(PAGE_COOK_TIME)));

                                new PushFeedTask().execute(new ServerFeed("control_cook_time", timeJson));
                            }
                        }

                        new PushFeedTask().execute(new ServerFeed("control_temperature", temperatureJson));

                        Toast.makeText(getApplicationContext(), "Great success!",
                                Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("", e.getMessage());
                    } catch (NullPointerException e) {
                        Log.e("", e.getMessage());
                    }
                } else {
                    if (fragment instanceof ControlCookModeRadioFragment) {
                        userSelectedMode = ((ControlCookModeRadioFragment) fragment).getCookMode();
                        if (userSelectedMode.equals("probe")) {
                            nextItem = PAGE_COOK_TEMP;
                        } else if (userSelectedMode.equals("manual")) {
                            nextItem = PAGE_COOK_TEMP_RADIO;
                        }

                        userChoices.put(pageTranslations.get(PAGE_COOK_MODE), userSelectedMode);

                    } else {
                        nextItem = PAGE_CONFIRM;

                        if (fragment instanceof ControlCookTimeFragment) {
                            String cookTime = ((ControlCookTimeFragment) fragment).getCookTime();

                            userChoices.put(pageTranslations.get(PAGE_COOK_TIME), cookTime);

                            if (userSelectedMode.equals("program")) {
                                nextItem = PAGE_COOK_TEMP_RADIO;
                            }

                        } else if (fragment instanceof ControlTemperatureFragment) {
                            String temperature = ((ControlTemperatureFragment) fragment).getTemperature();
                            String temperatureMode = ((ControlTemperatureFragment) fragment).getTemperatureModeString();

                            userChoices.put(pageTranslations.get(PAGE_COOK_TEMP), temperature + " " + temperatureMode);
                        } else if (fragment instanceof ControlTemperatureRadioFragment) {
                            String temperatureMode = ((ControlTemperatureRadioFragment) fragment).getHeatMode();

                            userChoices.put(pageTranslations.get(PAGE_COOK_TEMP_RADIO), temperatureMode);
                        }
                    }
                }

                if(nextItem == PAGE_CONFIRM) {
                    nextBtn.setText("Submit");
                    ControlConfirmFragment controlConfirmFragment = (ControlConfirmFragment) fragmentPagerAdapter.getItem(PAGE_CONFIRM);
                    controlConfirmFragment.populateView(userChoices);
                }

                viewPager.setCurrentItem(nextItem);
            }
        });

        cancelBtn = (Button) findViewById(R.id.controlSlowCookerActivityCancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextBtn.setText("Next");
                userChoices = new ArrayMap<>();
                viewPager.setCurrentItem(PAGE_COOK_MODE);

                ControlConfirmFragment controlConfirmFragment = (ControlConfirmFragment) fragmentPagerAdapter.getItem(PAGE_CONFIRM);
                controlConfirmFragment.populateView(userChoices);
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
