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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.table6.activity.R;

public class ControlCookTimeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    int userSelection;

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

        spinner = (Spinner) view.findViewById(R.id.controlCookTimeSpinner);
        adapter = ArrayAdapter.createFromResource(getContext(), R.array.time_choices,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        userSelection = 0;
    }

    /**
     * Gets and validates user input cook time.
     * @return A formatted cook time string or empty string if input is invalid.
     */
    public String getCookTime() {
        String result = "";

        try {
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
