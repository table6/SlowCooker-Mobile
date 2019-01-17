/**
 * Project: The  Most’ve’er’st  Bestestery Smartesteresteresteringer  Sloweringer Cookery
 * Author: Jonathon Gebhardt
 * Created on: 17 January 2019
 */

package com.table6.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.table6.slowcooker.R;

// TODO: Implement interface to update display views
public class TemperatureDisplayView extends DisplayView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_display_view);
    }
}
