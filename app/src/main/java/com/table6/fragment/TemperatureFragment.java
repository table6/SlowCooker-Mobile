package com.table6.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.table6.activity.R;

import java.util.Locale;

public class TemperatureFragment extends Fragment {

    private static final int MODE_FAHRENHEIT = 0;
    private static final int MODE_CELSIUS = 1;
    private static final double DEFAULT_TEMP = 0.0;
    private static final String ARG_MODE_PREF = "modePref";

    private String modePref;
    private TextView tempTxt;
    private TextView modeTxt;

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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int tempMode = sharedPreferences.getInt(modePref, 0);
        this.setMode(tempMode);
    }

    public void setMode(int x) {
        this.modeTxt.setText((x == 0) ? "F" : "C");
        if (x == MODE_FAHRENHEIT) {
            this.modeTxt.setText("F");

            double oldTemperature = Double.parseDouble(this.getTemperature());
            this.setTemperature(this.celsiusToFahrenheit(oldTemperature));
        } else if (x == MODE_CELSIUS){
            this.modeTxt.setText("C");

            double oldTemperature = Double.parseDouble(this.getTemperature());
            this.setTemperature(this.fahrenheitToCelsius(oldTemperature));
        }

    }

    public String getMode() {
        return this.modeTxt.getText().toString();
    }

    public void setTemperature(String x) {
        this.tempTxt.setText(x);
    }

    public void setTemperature(Double x) {
        this.setTemperature(String.format(Locale.US, "%.2f", x));
    }

    public String getTemperature() {
        return this.tempTxt.getText().toString();
    }

    // multiply by 1.8 and add 32
    public double celsiusToFahrenheit(double x) {
        return x * 1.8 + 32;
    }

    // subtract 32 and divide by 1.8
    public double fahrenheitToCelsius(double x) {
        return (x - 32) / 1.8;
    }

    public void update() {
        // TODO: Update temperature as necessary

        // update temperature

        // if the mode has changed, convert the current temperature

        // set the new temperature

    }
}
