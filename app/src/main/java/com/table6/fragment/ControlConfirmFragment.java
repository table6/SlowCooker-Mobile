package com.table6.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.table6.activity.R;

public class ControlConfirmFragment extends Fragment {

    private ArrayMap<String, String> items = new ArrayMap<>();

    public ControlConfirmFragment() {
        // Required empty public constructor
    }

    public static ControlConfirmFragment newInstance() {
        ControlConfirmFragment fragment = new ControlConfirmFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_control_confirm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout linearLayout = view.findViewById(R.id.controlConfirmContainer);
        for(String key : items.keySet()) {
            String value = items.get(key);

            TextView textView = new TextView(view.getContext());
            textView.setText(key + ": " + value);

            textView.setTypeface(textView.getTypeface(), Typeface.ITALIC);
            textView.setTextSize(20);

            linearLayout.addView(textView);
        }
    }

    public void populateView(ArrayMap<String, String> userChoices) {
        this.items = userChoices;
    }

    public void cleanView() {
        this.items = new ArrayMap<>();
    }
}
