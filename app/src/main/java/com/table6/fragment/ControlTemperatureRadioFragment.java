package com.table6.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.table6.activity.R;

public class ControlTemperatureRadioFragment extends Fragment {

    private final int HEAT_MODE_WARM = 0;
    private final int HEAT_MODE_LOW = 1;
    private final int HEAT_MODE_HIGH = 2;

    private RadioGroup controlTemperatureGroup;
    private SparseIntArray buttonIdMap;

    public ControlTemperatureRadioFragment() {
        // Required empty public constructor
    }

    public static ControlTemperatureRadioFragment newInstance() {
        return new ControlTemperatureRadioFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_control_temperature_radio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        controlTemperatureGroup = (RadioGroup) view.findViewById(R.id.controlTemperatureGroup);

        buttonIdMap = new SparseIntArray();

        RadioButton controlTemperatureGroupWarm = (RadioButton) view.findViewById(R.id.controlTemperatureGroupWarm);
        buttonIdMap.append(controlTemperatureGroupWarm.getId(), HEAT_MODE_WARM);
        controlTemperatureGroupWarm.setChecked(true);

        RadioButton controlTemperatureGroupLow = (RadioButton) view.findViewById(R.id.controlTemperatureGroupLow);
        buttonIdMap.append(controlTemperatureGroupLow.getId(), HEAT_MODE_LOW);

        RadioButton controlTemperatureGroupHigh = (RadioButton) view.findViewById(R.id.controlTemperatureGroupHigh);
        buttonIdMap.append(controlTemperatureGroupHigh.getId(), HEAT_MODE_HIGH);
    }

    /**
     * Gets the user selected heating mode.
     * @return the user selected mode.
     */
    public String getHeatMode() {
        String mode = "";

        int buttonId = controlTemperatureGroup.getCheckedRadioButtonId();
        if(buttonId != -1) {
            int modeId = buttonIdMap.get(buttonId);

            switch (modeId) {
                case HEAT_MODE_WARM:
                    mode = "warm";
                    break;
                case HEAT_MODE_LOW:
                    mode = "low";
                    break;
                case HEAT_MODE_HIGH:
                    mode = "high";
                    break;
                default:
                    break;
            }
        }

        return mode;
    }
}
