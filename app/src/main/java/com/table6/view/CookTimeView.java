package com.table6.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.table6.activity.R;

public class CookTimeView extends DisplayView {

    public CookTimeView(Context context) {
        super(context);
    }

    public CookTimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CookTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void update() {
        // TODO: update our time as necessary
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
