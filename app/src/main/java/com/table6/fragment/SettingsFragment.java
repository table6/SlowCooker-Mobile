package com.table6.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.table6.activity.R;

public class SettingsFragment extends Fragment {
    private static final String ARG_PREF_KEY = "argPrefKey";

    private String argPrefKey;

    private OnSettingsFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String argPrefKey) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PREF_KEY, argPrefKey);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.argPrefKey = getArguments().getString(ARG_PREF_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.settingsTemperatureGroup);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int tempMode = sharedPreferences.getInt(argPrefKey, 0);

        int fRadioBtnId = view.findViewById(R.id.fBtn).getId();
        int cRadioBtnId = view.findViewById(R.id.cBtn).getId();

        switch (tempMode) {
            case 0:
                radioGroup.check(fRadioBtnId);
                break;
            case 1:
                radioGroup.check(cRadioBtnId);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton fRadioButton = (RadioButton) getView().findViewById(R.id.fBtn);

                // If fahrenheit is checked, set mode to 0, otherwise set to 1.
                int mode = (checkedId == fRadioButton.getId()) ? 0 : 1;

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(argPrefKey, mode);
                editor.commit();

                mListener.onSettingsFragmentInteraction();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingsFragmentInteractionListener) {
            mListener = (OnSettingsFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSettingsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSettingsFragmentInteraction();
    }
}
