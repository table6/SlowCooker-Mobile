package com.table6.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
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
import java.text.SimpleDateFormat;

import javax.net.ssl.HttpsURLConnection;

public class CookTimeFragment extends Fragment {

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
    private String rpiTime;

    private TextView cookTime;

    public CookTimeFragment() {
        // Required empty public constructor
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static CookTimeFragment newInstance() {
        CookTimeFragment fragment = new CookTimeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.cookTime = (TextView) getActivity().findViewById(R.id.cookTimeFragmentCookTimeTextView);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cook_time, container, false);
    }

    public void setCookTime(String x) {
        this.cookTime.setText(x);
    }

    public String convertSecondsToHMmSs(long seconds) {
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d:%02d", h, m);
    }

    // https://www.tutorialspoint.com/android/android_json_parser.htm
    public class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            StringBuilder sb = new StringBuilder();

            try {
                connection = (HttpURLConnection) new URL("http://192.168.0.186:5000/cook_time").openConnection();
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
                    System.out.println(json);
                    System.out.println("\t\tCookTimeFragment: response=" + json);


                    rpiTime = json.getString("start_time");


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
            } finally {
                connection.disconnect();
            }

            return null;
        }

    }
}
