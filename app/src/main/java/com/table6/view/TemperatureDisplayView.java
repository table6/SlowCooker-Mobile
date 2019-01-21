/**
 * Project: The  Most’ve’er’st  Bestestery Smartesteresteresteringer  Sloweringer Cookery
 * Author: Jonathon Gebhardt
 * Created on: 17 January 2019
 */

package com.table6.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.table6.slowcooker.R;

// TODO: Implement interface to update display views
public class TemperatureDisplayView extends View {

    public TemperatureDisplayView(Context context) {
        super(context);
    }

    public TemperatureDisplayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TemperatureDisplayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMeasurement(String x) {
        TextView measurement = (TextView) findViewById(R.id.measurement);
        measurement.setText(x);
    }

    public String getMeasurement() {
        TextView measurement = (TextView) findViewById(R.id.measurement);
        return measurement.getText().toString();
    }
}
