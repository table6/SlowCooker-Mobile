package com.table6.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.table6.slowcooker.R;

public class CookTimeDisplayView extends View {

    public CookTimeDisplayView(Context context) {
        super(context);
    }

    public CookTimeDisplayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CookTimeDisplayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getCookTime() {
        TextView cookTime = (TextView) findViewById(R.id.time);
        return cookTime.toString();
    }

    public void setCookTime(String x) {
        TextView cookTime = (TextView) findViewById(R.id.time);
        cookTime.setText(x);
    }
}
