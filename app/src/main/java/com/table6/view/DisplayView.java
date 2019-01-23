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

public abstract class DisplayView extends View {

    public DisplayView(Context context) {
        super(context);
    }

    public DisplayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DisplayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DisplayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public abstract void update();
}
