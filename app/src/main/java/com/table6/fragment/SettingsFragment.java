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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.table6.activity.R;

public class SettingsFragment extends Fragment {
    private static final String ARG_TEMP_MODE = "argTempMode";

    private String argTempMode;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String argTempMode) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEMP_MODE, argTempMode);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.argTempMode = getArguments().getString(ARG_TEMP_MODE);
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

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.settingsTemperatureGroup);

        // Get and set previously selected user mode. Set to fahrenheit by default.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int tempMode = sharedPreferences.getInt(argTempMode, 0);

        int fRadioBtnId = view.findViewById(R.id.fBtn).getId();
        int cRadioBtnId = view.findViewById(R.id.cBtn).getId();

        switch (tempMode) {
            case 0:
                radioGroup.check(fRadioBtnId);
                break;
            case 1:
                radioGroup.check(cRadioBtnId);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton fRadioButton = (RadioButton) getView().findViewById(R.id.fBtn);

                // If fahrenheit is checked, set mode to 0, otherwise set to 1.
                int mode = (checkedId == fRadioButton.getId()) ? 0 : 1;

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(argTempMode, mode);
                editor.commit();
            }
        });
    }
}
