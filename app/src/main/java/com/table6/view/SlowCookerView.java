package com.table6.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.table6.activity.R;
import com.table6.fragment.ControlCookTimeFragment;
import com.table6.fragment.ControlTemperatureFragment;
import com.table6.fragment.ControlTemperatureRadioFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class SlowCookerView extends AppCompatActivity {

    private ControlCookTimeFragment controlCookTimeFragment;
    private ControlTemperatureFragment controlTemperatureFragment;
    private ControlTemperatureRadioFragment controlTemperatureRadioFragment;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slowcooker_view);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        controlCookTimeFragment = ControlCookTimeFragment.newInstance();
        fragmentTransaction.add(R.id.slowCookerViewFragmentContainer, controlCookTimeFragment);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String type = sharedPreferences.getString(getString(R.string.preference_file_type), "");
        if (type.equals("probe")) {
            controlTemperatureFragment = ControlTemperatureFragment.newInstance();
            fragmentTransaction.add(R.id.slowCookerViewFragmentContainer, controlTemperatureFragment);
        }

        controlTemperatureRadioFragment = ControlTemperatureRadioFragment.newInstance();
        fragmentTransaction.add(R.id.slowCookerViewFragmentContainer, controlTemperatureRadioFragment);

        fragmentTransaction.commit();

        confirmBtn = (Button) findViewById(R.id.slowCookerViewConfirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlCookTimeFragment != null) {
                    String cookTime = controlCookTimeFragment.getCookTime();
                    if(cookTime.length() > 0) {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("cook_time", cookTime);

                            // Send json to server.
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (controlTemperatureFragment!= null) {
                    String temperature = controlTemperatureFragment.getTemperature();
                    if (temperature.length() > 0) {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("temperature", temperature);

                            // Send json to server.
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (controlTemperatureRadioFragment != null) {
                    System.out.println(controlTemperatureRadioFragment.getChecked());
                    int heatMode = controlTemperatureRadioFragment.getChecked();
                    if (heatMode > -1) {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("heat_mode", heatMode);

                            // Send json to server.
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
