package com.table6.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.table6.activity.R;

public class SettingsFragment extends Fragment {
    private static final String ARG_TEMPERATURE_MODE = "argTemperatureMode";
    private static final String ARG_MEASUREMENT_MODE = "argMeasurementMode";

    private String argTemperatureMode;
    private String argMeasurementMode;

    private OnSettingsFragmentDoneListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String argTemperatureMode, String argMeasurementMode) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEMPERATURE_MODE, argTemperatureMode);
        args.putString(ARG_MEASUREMENT_MODE, argMeasurementMode);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.argTemperatureMode = getArguments().getString(ARG_TEMPERATURE_MODE);
            this.argMeasurementMode = getArguments().getString(ARG_MEASUREMENT_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Get and set previously selected user mode. Set to fahrenheit by default.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        int temperatureMode = sharedPreferences.getInt(argTemperatureMode, 0);

        int cookRadioBtnId = view.findViewById(R.id.settingsFragmentCookBtn).getId();
        int probeRadioBtnId = view.findViewById(R.id.settingsFragmentProbeBtn).getId();

        RadioGroup temperatureModeRadioGroup = (RadioGroup) view.findViewById(R.id.settingsFragmentModeGroup);

        switch (temperatureMode) {
            case 0:
                temperatureModeRadioGroup.check(cookRadioBtnId);
                break;
            case 1:
                temperatureModeRadioGroup.check(probeRadioBtnId);
                break;
        }

        temperatureModeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton cookRadioButton = (RadioButton) getView().findViewById(R.id.settingsFragmentCookBtn);

                // If fahrenheit is checked, set mode to 0, otherwise set to 1.
                int mode = (checkedId == cookRadioButton.getId()) ? 0 : 1;

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(argTemperatureMode, mode);
                editor.commit();
            }
        });

        int measurementMode = sharedPreferences.getInt(argMeasurementMode, 0);

        int fahrenheitRadioBtnId = view.findViewById(R.id.settingsFragmentFahrenheitBtn).getId();
        int celsiusRadioBtnId = view.findViewById(R.id.settingsFragmentCelsiusBtn).getId();

        RadioGroup measurementModeRadioGroup = (RadioGroup) view.findViewById(R.id.settingsFragmentMeasurementGroup);

        switch (measurementMode) {
            case 0:
                measurementModeRadioGroup.check(fahrenheitRadioBtnId);
                break;
            case 1:
                measurementModeRadioGroup.check(celsiusRadioBtnId);
                break;
        }

        measurementModeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton fahrenheitRadioBtnId = (RadioButton) getView().findViewById(R.id.settingsFragmentFahrenheitBtn);

                // If fahrenheit is checked, set mode to 0, otherwise set to 1.
                int mode = (checkedId == fahrenheitRadioBtnId.getId()) ? 0 : 1;

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(argMeasurementMode, mode);
                editor.commit();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingsFragmentDoneListener) {
            mListener = (OnSettingsFragmentDoneListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener.onSettingsFragmentDone();
        mListener = null;
    }

    public interface OnSettingsFragmentDoneListener {
        void onSettingsFragmentDone();
    }
}
