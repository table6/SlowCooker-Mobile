package com.table6.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.table6.activity.R;

public class ControlCookTimeFragment extends Fragment {

    private TextInputEditText controlCookTimeHourTxt;
    private TextInputEditText controlCookTimeMinuteTxt;

    public ControlCookTimeFragment() {
        // Required empty public constructor
    }

    public static ControlCookTimeFragment newInstance() {
        ControlCookTimeFragment fragment = new ControlCookTimeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_control_cook_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(2);

        controlCookTimeHourTxt = (TextInputEditText) view.findViewById(R.id.controlCookTimeHourTxt);
        controlCookTimeHourTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
        controlCookTimeHourTxt.setFilters(filterArray);

        controlCookTimeMinuteTxt = (TextInputEditText) view.findViewById(R.id.controlCookTimeMinuteTxt);
        controlCookTimeMinuteTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
        controlCookTimeMinuteTxt.setFilters(filterArray);
    }

    /**
     * Gets and validates user input cook time.
     * @return A formatted cook time string or empty string if input is invalid.
     */
    public String getCookTime() {
        String result = "";

        try {
            String cookTimeHourText = controlCookTimeHourTxt.getText().toString();
            if (cookTimeHourText.length() != 0) {
                int cookTimeHour = Integer.parseInt(cookTimeHourText);

                if (cookTimeHour >= 0 && cookTimeHour < 10) {
                    result = "0"+ cookTimeHour + ":";
                } else if (cookTimeHour >= 10 && cookTimeHour <= 12) {
                    result = cookTimeHourText + ":";
                }
            } else {
                result = "00:";
            }
        } catch (NullPointerException e) {
            Log.e("", e.getMessage());
        } catch (NumberFormatException e) {
            Log.e("", e.getMessage());
        }

        try {
            String cookTimeMinuteText = controlCookTimeMinuteTxt.getText().toString();
            if (controlCookTimeMinuteTxt.length() != 0) {
                int cookTimeMinute = Integer.parseInt(cookTimeMinuteText);

                if (cookTimeMinute == 0) {
                    result += "00";
                } else if (cookTimeMinute == 30) {
                    result += "30";
                }
            } else {
                result += "00";
            }
        } catch (NullPointerException e) {
            Log.e("", e.getMessage());
        } catch (NumberFormatException e) {
            Log.e("", e.getMessage());
        }

        if(result.length() != 5) {
            result = "";
        }

        return result;
    }
}
