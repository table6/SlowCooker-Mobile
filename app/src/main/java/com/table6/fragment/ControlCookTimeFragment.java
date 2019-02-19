package com.table6.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.table6.activity.R;

public class ControlCookTimeFragment extends Fragment {

    private Button controlCookTimeBtn;
    private TextInputEditText controlCookTimeTxt;
    private OnControlCookTimeFragmentInteractionListener mListener;

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
        controlCookTimeTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String newTime = controlCookTimeTxt.getText().toString();

                if (actionId == EditorInfo.IME_ACTION_DONE && !newTime.isEmpty()) {
                    mListener.onControlCookTimeFragmentInteraction(newTime);
                    Toast.makeText(getActivity(), "Setting cook time to " + newTime, Toast.LENGTH_LONG ).show();
                }

                return false;
            }
        });

        controlCookTimeBtn = (Button) view.findViewById(R.id.controlCookTimeBtn);
        controlCookTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTemperature = controlCookTimeTxt.getText().toString();
                if(!newTemperature.isEmpty()) {
                    mListener.onControlCookTimeFragmentInteraction(newTemperature);
                    Toast.makeText(getActivity(), "Setting temperature to " + newTemperature, Toast.LENGTH_LONG ).show();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnControlCookTimeFragmentInteractionListener) {
            mListener = (OnControlCookTimeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnControlCookTimeFragmentInteractionListener {
        void onControlCookTimeFragmentInteraction(String x);
    }
}
