package com.table6.fragment;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import static java.util.TimeZone.getTimeZone;

public class CookTimeFragment extends ServerFeedFragment {

    private TextView cookTime;

    public CookTimeFragment() {

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
    public String getOffsetFromTime(Date date) throws ParseException {
        // https://stackoverflow.com/questions/308683/how-can-i-get-the-current-date-and-time-in-utc-or-gmt-in-java
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("HH:mm:ss");
        dateFormatGmt.setTimeZone(getTimeZone("GMT"));

        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("HH:mm:ss");

        // https://stackoverflow.com/questions/21285161/android-difference-between-two-dates
        //milliseconds
        long different = dateFormatLocal.parse(dateFormatGmt.format(new Date())).getTime() - date.getTime();

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

    public Date getTimeFromJson(String timeStamp) throws ParseException {
        String[] tokens = timeStamp.split(":");

        String[] hourTokens = tokens[0].split(" ");
        String hour = hourTokens[hourTokens.length - 1];

        String minute = tokens[1];

        String second = tokens[2].split(" ")[0];

        return new SimpleDateFormat("hh:mm:ss").parse(hour + ":" + minute + ":" + second);
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
    public class RetrieveFeedTask extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... voids) {

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(getString(R.string.server_address) + "cook_time").openConnection();
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
                    final String resultTime = getOffsetFromTime(getTimeFromJson(json.getString("date")));

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setCookTime(resultTime);
                        }
                    });
                } catch (JSONException e) {
                    Log.e("", e.getMessage());
                } catch (ParseException e) {
                    Log.e("", e.getMessage());
                }
            }
        }
    }
}