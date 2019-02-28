package com.table6.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.table6.activity.R;

public class CookerStatsFragment extends Fragment {

    private static final String ARG_TEMPERATURE_MODE_PREF = "modePref";

    private String temperatureModePref;
    private OnFragmentInteractionListener mListener;

    public CookerStatsFragment() {
        // Required empty public constructor
    }

    public static CookerStatsFragment newInstance(String modePref) {
        CookerStatsFragment fragment = new CookerStatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEMPERATURE_MODE_PREF, modePref);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.temperatureModePref = getArguments().getString(ARG_TEMPERATURE_MODE_PREF);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cooker_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int tempMode = sharedPreferences.getInt(temperatureModePref, 0);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        CookTimeFragment cookTimeFragment = CookTimeFragment.newInstance();
        transaction.add(R.id.cookerStatsContainer, cookTimeFragment);

        TemperatureFragment temperatureFragment = TemperatureFragment.newInstance(this.temperatureModePref);
        transaction.add(R.id.cookerStatsContainer, temperatureFragment);

        transaction.commit();

        ToggleButton secureLidToggleBtn = (ToggleButton) view.findViewById(R.id.cookerStatsSecureLidToggleBtn);
        secureLidToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO: In any case, run a thread to attempt to secure/unsecure lid
                if (isChecked) {
                    // The toggle is enabled. Attempt to secure lid.
                } else {
                    // The toggle is disabled. Attempt to unsecure lid.
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
