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
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class TemperatureFragment extends Fragment implements SlowcookerFragment {

    private static final int MODE_FAHRENHEIT = 0;
    private static final int MODE_CELSIUS = 1;
    private static final int MODE_NONE = 2;
    private static final String ARG_MODE_PREF = "modePref";
    private static final int UPDATE_FREQUENCY = 60;

    private final Handler handler = new Handler();

    private boolean fragmentActive;
    private String modePref;
    private TextView tempTxt;
    private TextView modeTxt;

    // https://stackoverflow.com/questions/6400846/updating-time-and-date-by-the-second-in-android
    private final Runnable runnable = new Runnable() {
        public void run() {
            if (fragmentActive) {
                handler.postDelayed(runnable, UPDATE_FREQUENCY * 1000);
                update();
            }
        }
    };

    public TemperatureFragment() {
        // Required empty public constructor
    }

    public static TemperatureFragment newInstance(String modePref) {
        TemperatureFragment fragment = new TemperatureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MODE_PREF, modePref);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.modePref = getArguments().getString(ARG_MODE_PREF);
        }
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

        startUpdateThread();
    }

    public void setMode(int x) {
        if (x == MODE_FAHRENHEIT) {
            this.modeTxt.setText("F");
        } else if (x == MODE_CELSIUS){
            this.modeTxt.setText("C");
        } else if (x == MODE_NONE) {
            this.modeTxt.setText("");
        }

    }

    public void setTemperature(String x) {
        this.tempTxt.setText(String.format(Locale.US, "%.2f", Double.parseDouble(x)));
    }

    public void setTemperature(Double x) {
        this.setTemperature(String.format(Locale.US, "%.2f", x));
    }

    @Override
    public void update() {
        if(this.tempTxt != null && this.modeTxt != null) {
            new RetrieveFeedTask().execute();
        }
    }

    private void startUpdateThread() {
        fragmentActive = true;
        handler.post(runnable);
    }

    // https://www.tutorialspoint.com/android/android_json_parser.htm
    public class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            StringBuilder sb = new StringBuilder();

            try {
                connection = (HttpURLConnection) new URL("http://3.18.34.75:5000/temperature").openConnection();
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
                    final String type = json.getString("type");
                    final String temperature = json.getString("temperature");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (type.equals("program")) {
                                // Don't show C or F
                                setMode(MODE_NONE);

                            } else {
                                setMode((type.equals("F") ? MODE_FAHRENHEIT : MODE_CELSIUS));
                            }

                            setTemperature(temperature);
                        }
                    });

                } else if(responseCode == HttpURLConnection.HTTP_CONFLICT) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    // Report failure.

                } else {
                    return null;
                }
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
            } finally {
                connection.disconnect();
            }

            return null;
        }
    }
}
