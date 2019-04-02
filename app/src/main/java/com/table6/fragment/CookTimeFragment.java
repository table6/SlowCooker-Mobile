package com.table6.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class CookTimeFragment extends ServerFeedFragment {

    private static final int UPDATE_FREQUENCY = 60;

    private TextView cookTime;

    public CookTimeFragment() {
        super(UPDATE_FREQUENCY);
    }

    public static CookTimeFragment newInstance() {
        CookTimeFragment fragment = new CookTimeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cook_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.cookTime = (TextView) view.findViewById(R.id.cookTimeFragmentCookTimeTextView);
    }

    /**
     * Set the cook time of the appropriate TextView.
     * @param x the cook time to be displayed.
     */
    public void setCookTime(String x) {
        this.cookTime.setText(x);
    }

    /**
     * Get the date offset in a HH:MM format.
     * @param date the UTC date to be used as an offset.
     * @return a formatted time string in HH:MM format.
     */
    public String getOffsetFromTime(Date date) {
        // https://stackoverflow.com/questions/21285161/android-difference-between-two-dates

        //milliseconds
        long different = System.currentTimeMillis() - date.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        // Remove days from difference.
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;

        return String.format("%02d:%02d", elapsedHours, elapsedMinutes);
    }

    /**
     *
     */
    @Override
    protected void update() {
        if(this.cookTime != null) {
            new RetrieveFeedTask().execute();
        }
    }

    // https://www.tutorialspoint.com/android/android_json_parser.htm
    public class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(getString(R.string.server_address) + "cook_time").openConnection();
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.connect();

                int responseCode = connection.getResponseCode();

                StringBuilder sb = new StringBuilder();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    JSONObject json = new JSONObject(sb.toString());
                    String jsonTime = json.getString("start_time");

                    final String resultTime = getOffsetFromTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse(jsonTime));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setCookTime(resultTime);
                        }
                    });
                } else if(responseCode == HttpURLConnection.HTTP_CONFLICT) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    // Report failure.

                }

                connection.disconnect();

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

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
