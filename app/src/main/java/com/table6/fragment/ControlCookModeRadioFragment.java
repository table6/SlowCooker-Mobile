package com.table6.fragment;

import android.content.Context;
import android.net.Uri;
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

public class ControlCookModeRadioFragment extends Fragment {
    private final int COOK_MODE_PROBE = 0;
    private final int COOK_MODE_PROGRAM = 1;
    private final int COOK_MODE_MANUAL = 2;

    private RadioGroup controlModeGroup;
    private SparseIntArray buttonIdMap;

    public ControlCookModeRadioFragment() {
        // Required empty public constructor
    }

    public static ControlCookModeRadioFragment newInstance() {
        return new ControlCookModeRadioFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_control_cook_mode_radio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        controlModeGroup = (RadioGroup) view.findViewById(R.id.controlTemperatureGroup);

        buttonIdMap = new SparseIntArray();

        RadioButton controlTemperatureGroupWarm = (RadioButton) view.findViewById(R.id.controlCookModeGroupProbe);
        buttonIdMap.append(controlTemperatureGroupWarm.getId(), COOK_MODE_PROBE);

        RadioButton controlTemperatureGroupLow = (RadioButton) view.findViewById(R.id.controlCookModeGroupProgram);
        buttonIdMap.append(controlTemperatureGroupLow.getId(), COOK_MODE_PROGRAM);

        RadioButton controlTemperatureGroupHigh = (RadioButton) view.findViewById(R.id.controlCookModeGroupManual);
        buttonIdMap.append(controlTemperatureGroupHigh.getId(), COOK_MODE_MANUAL);
    }

    /**
     * Gets the user selected heating mode.
     * @return the user selected mode.
     */
    public String getCookMode() {
        String mode = "";

        int buttonId = controlModeGroup.getCheckedRadioButtonId();
        if(buttonId != -1) {
            int modeId = buttonIdMap.get(buttonId);

            switch (modeId) {
                case COOK_MODE_PROBE:
                    mode = "probe";
                    break;
                case COOK_MODE_PROGRAM:
                    mode = "program";
                    break;
                case COOK_MODE_MANUAL:
                    mode = "manual";
                    break;
                default:
                    break;
            }
        }

        return mode;
    }
}
