package com.table6.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.table6.activity.R;

public class ControlTemperatureFragment extends Fragment implements AdapterView.OnItemSelectedListener {

//    private static final int MODE_FAHRENHEIT = 0;
//    private static final int MODE_CELSIUS = 1;
//
//    private RadioGroup controlTemperatureModeGroup;
//    private SparseIntArray buttonIdMap;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private int userSelection;

    public ControlTemperatureFragment() {
        // Required empty public constructor
    }

    public static ControlTemperatureFragment newInstance() {
        return new ControlTemperatureFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_control_temperature, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner = (Spinner) view.findViewById(R.id.controlTemperatureSpinner);
        adapter = ArrayAdapter.createFromResource(getContext(), R.array.fahrenheit_choices,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        userSelection = 0;

//        controlTemperatureModeGroup = (RadioGroup) view.findViewById(R.id.controlTemperatureModeGroup);
//
//        buttonIdMap = new SparseIntArray();
//
//        RadioButton temperatureModeFahrenheit = (RadioButton) view.findViewById(R.id.controlTemperatureModeFahrenheit);
//        buttonIdMap.append(temperatureModeFahrenheit.getId(), MODE_FAHRENHEIT);
//        temperatureModeFahrenheit.setChecked(true);
//
//        RadioButton temperatureModeCelsius = (RadioButton) view.findViewById(R.id.controlTemperatureModeCelsius);
//        buttonIdMap.append(temperatureModeCelsius.getId(), MODE_CELSIUS);
//
//        controlTemperatureModeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if(buttonIdMap.get(checkedId) == MODE_FAHRENHEIT) {
//                    adapter = ArrayAdapter.createFromResource(getContext(), R.array.fahrenheit_choices,
//                            android.R.layout.simple_spinner_item);
//
//                } else {
//                    adapter = ArrayAdapter.createFromResource(getContext(), R.array.celsius_choices,
//                            android.R.layout.simple_spinner_item);
//                }
//
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner.setAdapter(adapter);
//            }
//        });
    }

//    public int getTemperatureMode() {
//        int buttonId = controlTemperatureModeGroup.getCheckedRadioButtonId();
//
//        return (buttonId != -1 ? buttonIdMap.get(buttonId) : -1);
//    }

    public String getTemperatureModeString() {
        String mode = "F";

//        int buttonId = controlTemperatureModeGroup.getCheckedRadioButtonId();
//        if(buttonId != -1) {
//            int modeId = buttonIdMap.get(buttonId);
//
//            switch (modeId) {
//                case MODE_FAHRENHEIT:
//                    mode = "F";
//                    break;
//                case MODE_CELSIUS:
//                    mode = "C";
//                    break;
//                default:
//                    break;
//            }
//        }

        return mode;
    }

    /**
     * Gets and validates user input temperature.
     * @return A formatted temperature string or empty string if input is invalid.
     */
    public String getTemperature() {
        String result = "";

        try
        {
            result = adapter.getItem(userSelection).toString();
        } catch (NullPointerException e) {
            Log.e("", e.getMessage());
        }

        return result;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userSelection = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
