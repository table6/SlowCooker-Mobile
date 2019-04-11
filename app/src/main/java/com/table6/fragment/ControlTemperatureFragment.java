package com.table6.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.table6.activity.R;

public class ControlTemperatureFragment extends Fragment {

    private static final int MODE_FAHRENHEIT = 0;
    private static final int MODE_CELSIUS = 1;

    private TextInputEditText controlTemperatureTxt;
    private RadioGroup controlTemperatureModeGroup;
    private SparseIntArray buttonIdMap;

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

        controlTemperatureTxt = (TextInputEditText) view.findViewById(R.id.controlTemperatureTxt);
        controlTemperatureTxt.setInputType(InputType.TYPE_CLASS_NUMBER);

        controlTemperatureModeGroup = (RadioGroup) view.findViewById(R.id.controlTemperatureModeGroup);

        buttonIdMap = new SparseIntArray();

        RadioButton temperatureModeFahrenheit = (RadioButton) view.findViewById(R.id.controlTemperatureModeFahrenheit);
        buttonIdMap.append(temperatureModeFahrenheit.getId(), MODE_FAHRENHEIT);
        temperatureModeFahrenheit.setChecked(true);

        RadioButton temperatureModeCelsius = (RadioButton) view.findViewById(R.id.controlTemperatureModeCelsius);
        buttonIdMap.append(temperatureModeCelsius.getId(), MODE_CELSIUS);
    }

    public int getTemperatureMode() {
        int buttonId = controlTemperatureModeGroup.getCheckedRadioButtonId();

        return (buttonId != -1 ? buttonIdMap.get(buttonId) : -1);
    }

    public String getTemperatureModeString() {
        String mode = "";

        int buttonId = controlTemperatureModeGroup.getCheckedRadioButtonId();
        if(buttonId != -1) {
            int modeId = buttonIdMap.get(buttonId);

            switch (modeId) {
                case MODE_FAHRENHEIT:
                    mode = "F";
                    break;
                case MODE_CELSIUS:
                    mode = "C";
                    break;
                default:
                    break;
            }
        }

        return mode;
    }

    /**
     * Gets and validates user input temperature.
     * @return A formatted temperature string or empty string if input is invalid.
     */
    public String getTemperature() {
        String result = "";

        int temperatureMode = getTemperatureMode();

        if (controlTemperatureTxt != null) {
            try
            {
                String temperatureText = controlTemperatureTxt.getText().toString();
                int temperature = Integer.parseInt(temperatureText);

                if(temperatureMode == MODE_FAHRENHEIT) {
                    // Fahrenheit range is [140-180] in 5 degree increments.
                    if (temperature >= 140 && temperature <= 180 && temperature % 5 == 0) {
                        result = temperatureText;
                    }
                } else if (temperatureMode == MODE_CELSIUS)  {
                    // Celsius range is [60-80] in 5 degree increments.
                    if (temperature >= 60 && temperature <= 80 && temperature % 5 == 0) {
                        result = temperatureText;
                    }
                }
            } catch (NullPointerException e) {
                Log.e("", e.getMessage());
            } catch (NumberFormatException e) {
                Log.e("", e.getMessage());
            }
        }

        return result;
    }
}
