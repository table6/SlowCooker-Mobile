package com.table6.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class ServerFeedFragment extends Fragment {

    private boolean fragmentActive;
    private int updateFrequency;

    private final Handler handler = new Handler();

    public ServerFeedFragment() {
        this.updateFrequency = 10;
    }

    public ServerFeedFragment(int updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startUpdateThread();
    }

    // https://stackoverflow.com/questions/6400846/updating-time-and-date-by-the-second-in-android
    private final Runnable runnable = new Runnable() {
        public void run() {
            if (fragmentActive) {
                handler.postDelayed(runnable, updateFrequency * 1000);
                update();
            }
        }
    };

    protected abstract void update();

    private void startUpdateThread() {
        fragmentActive = true;
        handler.post(runnable);
    }

}
