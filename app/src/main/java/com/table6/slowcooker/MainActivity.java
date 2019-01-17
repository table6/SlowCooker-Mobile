/**
 * Project: The  Most’ve’er’st  Bestestery Smartesteresteresteringer  Sloweringer Cookery
 * Author: Jonathon Gebhardt
 * Created on: 17 January 2019
 */

package com.table6.slowcooker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.table6.view.CookTimeDisplayView;
import com.table6.view.DisplayView;
import com.table6.view.TemperatureDisplayView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button openTempViewBtn = (Button) findViewById(R.id.openTempViewBtn);
        openTempViewBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TemperatureDisplayView.class));
            }

        });

        Button openCookTimeViewBtn = (Button) findViewById(R.id.openCookTimeViewBtn);
        openTempViewBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CookTimeDisplayView.class));
            }

        });
    }
}
