package com.table6.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.table6.activity.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CookerStatsFragment extends Fragment {

    private static final int TEMPERATURE_MODE_FAHRENHEIT = 0;
    private static final int TEMPERATURE_MODE_CELSIUS = 1;
    private static final String ARG_TEMPERATURE_MODE_PREF = "modePref";

    private boolean fragmentActive;
    private String temperatureModePref;
    private TextView timeTxt;
    private TextView temperatureTxt;
    private TextView temperatureModeTxt;
    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

    private OnFragmentInteractionListener mListener;

    private final Handler handler = new Handler();

    // https://stackoverflow.com/questions/6400846/updating-time-and-date-by-the-second-in-android
    private final Runnable mRunnable = new Runnable() {
        public void run() {
            if (fragmentActive) {
                if (timeTxt != null) {
                    timeTxt.setText(getTime());
                }
                handler.postDelayed(mRunnable, 1000);
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

        this.timeTxt = view.findViewById(R.id.cookerStatsTimeTxt);
        this.temperatureTxt = view.findViewById(R.id.cookerStatsFragmentTemperatureTxt);
        this.temperatureModeTxt = view.findViewById(R.id.cookerStatsFragmentTemperatureModeTxt);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int tempMode = sharedPreferences.getInt(temperatureModePref, 0);
        this.setTemperatureMode(tempMode);

        ToggleButton secureLidToggleBtn = (ToggleButton) view.findViewById(R.id.cookerStatsSecureLidToggleBtn);
        secureLidToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                }
            }
        });

        startClock();
    }

    //    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setTemperatureMode(int x) {
        this.temperatureModeTxt.setText((x == 0) ? "F" : "C");
        if (x == TEMPERATURE_MODE_FAHRENHEIT) {
            this.temperatureModeTxt.setText("F");

            double oldTemperature = Double.parseDouble(this.getTemperatureText());
            this.setTemperatureText(this.celsiusToFahrenheit(oldTemperature));
        } else if (x == TEMPERATURE_MODE_CELSIUS){
            this.temperatureModeTxt.setText("C");

            double oldTemperature = Double.parseDouble(this.getTemperatureText());
            this.setTemperatureText(this.fahrenheitToCelsius(oldTemperature));
        }

    }

    // TODO: Update once RPi interface is implemented.
    private String getTime() {
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    private void startClock() {
        fragmentActive = true;
        handler.post(mRunnable);
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

    // multiply by 1.8 and add 32
    public double celsiusToFahrenheit(double x) {
        return x * 1.8 + 32;
    }

    // subtract 32 and divide by 1.8
    public double fahrenheitToCelsius(double x) {
        return (x - 32) / 1.8;
    }
}
