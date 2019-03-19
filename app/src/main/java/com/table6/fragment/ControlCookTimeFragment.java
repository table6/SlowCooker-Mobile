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

    private TextInputEditText controlCookTimeTxt;

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

        controlCookTimeTxt = (TextInputEditText) view.findViewById(R.id.controlCookTimeTxt);
        controlCookTimeTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public String getCookTime() {
        String result = "";
        if (controlCookTimeTxt != null) {
            String cookTimeText = controlCookTimeTxt.getText().toString();
            String[] tokens = cookTimeText.split(":");
        }

        return result;
    }
}
