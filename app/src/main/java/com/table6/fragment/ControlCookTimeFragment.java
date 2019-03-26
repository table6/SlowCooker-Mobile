package com.table6.fragment;

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

        controlCookTimeHourTxt = (TextInputEditText) view.findViewById(R.id.controlCookTimeHourTxt);
        controlCookTimeHourTxt.setInputType(InputType.TYPE_CLASS_NUMBER);

        controlCookTimeMinuteTxt = (TextInputEditText) view.findViewById(R.id.controlCookTimeMinuteTxt);
        controlCookTimeMinuteTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    /**
     * Gets and validates user input cook time.
     * @return A formatted cook time string or empty string if input is invalid.
     */
    public String getCookTime() {
        String result = "";

        try {
            String cookTimeHourText = controlCookTimeHourTxt.getText().toString();
            int cookTimeHour = Integer.parseInt(cookTimeHourText);

            if (cookTimeHour < 0 || cookTimeHour > 12) {

            } else {
                result = cookTimeHourText + ":";
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
            String cookTimeMinuteText = controlCookTimeMinuteTxt.getText().toString();
            int cookTimeMinute = Integer.parseInt(cookTimeMinuteText);

            if (cookTimeMinute != 0 && cookTimeMinute != 30) {

            } else {
                result += cookTimeMinuteText;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return result;
    }
}
