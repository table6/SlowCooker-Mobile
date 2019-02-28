package com.table6.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.table6.activity.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class CookTimeFragment extends Fragment implements SlowcookerFragment {

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
    private String rpiTime;

    private TextView cookTime;

    public CookTimeFragment() {
        // Required empty public constructor
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
        update();
    }

    public void setCookTime(String x) {
        this.cookTime.setText(x);
    }

    // https://stackoverflow.com/questions/21285161/android-difference-between-two-dates
    public String printDifference(Date date) {
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
        different = different % minutesInMilli;

        return elapsedHours + ":" + elapsedMinutes;
    }

    @Override
    public void update() {
        if(this.cookTime != null) {
            new RetrieveFeedTask().execute();
        }
    }

    // https://www.tutorialspoint.com/android/android_json_parser.htm
    public class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            StringBuilder sb = new StringBuilder();

            try {
                connection = (HttpURLConnection) new URL("http://3.18.34.75:5000/cook_time").openConnection();
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.connect();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    // Do something with response.
                    JSONObject json = new JSONObject(sb.toString());
                    System.out.println("\t\tCookTimeFragment: response=" + json);

                    String jsonTime = json.getString("start_time");
                    String resultTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse(jsonTime));

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setCookTime(time);
                        }
                    });

                    // 2019-02-27 03:42:22.626722


                } else if(responseCode == HttpURLConnection.HTTP_CONFLICT) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    // Report failure.
                    System.out.println("\t\tCookerStatsFragment: responseError=" + sb.toString());
                } else {
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }

            return null;
        }

    }
}
