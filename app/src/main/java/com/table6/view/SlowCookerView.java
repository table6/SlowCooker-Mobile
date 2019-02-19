package com.table6.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.table6.activity.R;
import com.table6.fragment.ControlCookTimeFragment;
import com.table6.fragment.ControlTemperatureFragment;

public class SlowCookerView extends AppCompatActivity implements
        ControlTemperatureFragment.OnControlTemperatureFragmentInteractionListener,
        ControlCookTimeFragment.OnControlCookTimeFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slowcooker_view);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ControlTemperatureFragment controlTemperatureFragment = ControlTemperatureFragment.newInstance();
        fragmentTransaction.add(R.id.slowCookerViewFragmentContainer, controlTemperatureFragment);

        ControlCookTimeFragment controlCookTimeFragment = ControlCookTimeFragment.newInstance();
        fragmentTransaction.add(R.id.slowCookerViewFragmentContainer, controlCookTimeFragment);

        fragmentTransaction.commit();
    }

    @Override
    public void onControlTemperatureFragmentInteraction(String x) {
        // TODO: Send temperature to RPi
        System.out.println("\t\tNew temperature: " + x);
    }

    @Override
    public void onControlCookTimeFragmentInteraction(String x) {
        // TODO: Send temperature to RPi
        System.out.println("\t\tNew time: " + x);
    }
}
