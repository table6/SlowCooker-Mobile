/**
 * Project: The  Most’ve’er’st  Bestestery Smartesteresteresteringer  Sloweringer Cookery
 * Author: Jonathon Gebhardt
 * Created on: 17 January 2019
 */

package com.table6.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.table6.slowcooker.R;

// TODO: Implement interface to update display views
public class TemperatureView extends View implements TemperatureModeChangeEvent {

    public TemperatureView(Context context) {
        super(context);
    }

    public TemperatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TemperatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMeasurement(int x) {
        TextView measurement = (TextView) findViewById(R.id.measurement);
        measurement.setText(x);
    }

    public String getMeasurement() {
        TextView measurement = (TextView) findViewById(R.id.measurement);
        return measurement.getText().toString();
    }

    public void setTemperature(String x) {
        TextView temperature = (TextView) findViewById(R.id.temperature);
        temperature.setText(x);
    }

    public String getTemperature() {
        TextView temperature = (TextView) findViewById(R.id.temperature);
        return temperature.getText().toString();
    }

    // multiply by 1.8 and add 32
    public double celciusToFarenheit(double x) {
        return x * 1.8 + 32;
    }

    // subtract 32 and divide by 1.8
    public double farenheitToCelcius(double x) {
        return (x - 32) / 1.8;
    }

    public void update() {
        // update temperature

        // if the mode has changed, convert the current temperature

            // set the new temperature

    }

    @Override
    public void onTemperatureModeChange(String mode) {

    }
}
