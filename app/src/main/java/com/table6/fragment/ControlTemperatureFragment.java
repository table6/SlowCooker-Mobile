package com.table6.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.table6.activity.R;

public class ControlTemperatureFragment extends Fragment {

    private static final int MODE_FAHRENHEIT = 0;
    private static final int MODE_CELSIUS = 1;
    private static final int MODE_NONE = 2;

    private TextInputEditText controlTemperatureTxt;

    public ControlTemperatureFragment() {
        // Required empty public constructor
    }

    public static ControlTemperatureFragment newInstance() {
        ControlTemperatureFragment fragment = new ControlTemperatureFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
    }

    /**
     * Gets and validates user input temperature.
     * @return A formatted temperature string or empty string if input is invalid.
     */
    public String getTemperature() {
        String result = "";
        if (controlTemperatureTxt != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            int type = sharedPreferences.getInt(getString(R.string.preference_file_measurement), MODE_NONE);

            try
            {
                String temperatureText = controlTemperatureTxt.getText().toString();
                int temperature = Integer.parseInt(temperatureText);

                if(type == MODE_FAHRENHEIT) {
                    // Fahrenheit range is [140-180] in 5 degree increments.
                    if(temperature < 140 || temperature > 180 || temperature % 5 != 0) {

                    } else {
                        result = temperatureText;
                    }

                } else if (type == MODE_CELSIUS)  {
                    // Celsius range is [60-80] in 5 degree increments.
                    if (temperature < 60 || temperature > 80 || temperature % 5 != 0) {

                    } else {
                        result = temperatureText;
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
