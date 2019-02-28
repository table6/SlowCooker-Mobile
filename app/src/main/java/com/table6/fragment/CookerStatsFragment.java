package com.table6.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.table6.activity.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class CookerStatsFragment extends Fragment {

    private static final int TEMPERATURE_MODE_FAHRENHEIT = 0;
    private static final int TEMPERATURE_MODE_CELSIUS = 1;
    private static final String ARG_TEMPERATURE_MODE_PREF = "modePref";
    private static final String[] PAGES = {"temperature", "cook_time"};
    private static final int UPDATE_FREQUENCY = 60;
    private static final ArrayList<SlowcookerFragment> FRAGMENTS = new ArrayList<>();

    private boolean fragmentActive;
    private String temperatureModePref;
    private TextView timeTxt;
    private TextView temperatureTxt;
    private TextView temperatureModeTxt;

    private OnFragmentInteractionListener mListener;

    // https://stackoverflow.com/questions/6400846/updating-time-and-date-by-the-second-in-android
    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
    private final Handler handler = new Handler();

    private final Runnable runnable = new Runnable() {
        public void run() {
            if (fragmentActive) {
                for (SlowcookerFragment f : FRAGMENTS) {
                    f.update();
                }

                handler.postDelayed(runnable, UPDATE_FREQUENCY * 1000);
            }
        }
    };

    public CookerStatsFragment() {
        // Required empty public constructor
    }

    public static CookerStatsFragment newInstance(String modePref) {
        CookerStatsFragment fragment = new CookerStatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEMPERATURE_MODE_PREF, modePref);
        fragment.setArguments(args);

        CookTimeFragment cookTimeFragment = CookTimeFragment.newInstance();
        FRAGMENTS.add(cookTimeFragment);

        TemperatureFragment temperatureFragment = TemperatureFragment.newInstance(modePref);
        FRAGMENTS.add(temperatureFragment);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.temperatureModePref = getArguments().getString(ARG_TEMPERATURE_MODE_PREF);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cooker_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int tempMode = sharedPreferences.getInt(temperatureModePref, 0);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (SlowcookerFragment f : FRAGMENTS) {
            transaction.add(R.id.cookerStatsContainer, (Fragment) f);
        }

//        CookTimeFragment cookTimeFragment = CookTimeFragment.newInstance();
//        this.FRAGMENTS.add(cookTimeFragment);
//        transaction.add(R.id.cookerStatsContainer, cookTimeFragment);
//
//        TemperatureFragment temperatureFragment = TemperatureFragment.newInstance(this.temperatureModePref);
//        this.FRAGMENTS.add(temperatureFragment);
//        transaction.add(R.id.cookerStatsContainer, temperatureFragment);

        transaction.commit();

//        this.setTemperatureMode(tempMode);

        ToggleButton secureLidToggleBtn = (ToggleButton) view.findViewById(R.id.cookerStatsSecureLidToggleBtn);
        secureLidToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO: In any case, run a thread to attempt to secure/unsecure lid
                if (isChecked) {
                    // The toggle is enabled. Attempt to secure lid.
                } else {
                    // The toggle is disabled. Attempt to unsecure lid.
                }
            }
        });

        startUpdateThread();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

//    public void setTemperatureMode(int x) {
//        this.temperatureModeTxt.setText((x == 0) ? "F" : "C");
//        if (x == TEMPERATURE_MODE_FAHRENHEIT) {
//            this.temperatureModeTxt.setText("F");
//
//            double oldTemperature = Double.parseDouble(this.getTemperatureText());
//            this.setTemperatureText(this.celsiusToFahrenheit(oldTemperature));
//        } else if (x == TEMPERATURE_MODE_CELSIUS){
//            this.temperatureModeTxt.setText("C");
//
//            double oldTemperature = Double.parseDouble(this.getTemperatureText());
//            this.setTemperatureText(this.fahrenheitToCelsius(oldTemperature));
//        }
//
//    }

    // TODO: Update once RPi interface is implemented.
    private String getTime() {
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    private void startUpdateThread() {
        fragmentActive = true;
        handler.post(runnable);
    }

    public String getTemperatureMode() {
        return this.temperatureModeTxt.getText().toString();
    }

    public void setTemperatureText(String x) {
        this.temperatureTxt.setText(x);
    }

    public void setTemperatureText(Double x) {
        this.setTemperatureText(String.format(Locale.US, "%.2f", x));
    }

    public String getTemperatureText() {
        return this.temperatureTxt.getText().toString();
    }

    public double celsiusToFahrenheit(double x) {
        return x * 1.8 + 32;
    }

    public double fahrenheitToCelsius(double x) {
        return (x - 32) / 1.8;
    }




}
