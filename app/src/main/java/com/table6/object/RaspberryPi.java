package com.table6.object;

import com.table6.utility.RetrieveFeedTask;

import java.net.MalformedURLException;
import java.net.URL;

public class RaspberryPi implements RetrieveFeedTask.RetrieveFeedTaskListener {

    private URL url = null;

    public RaspberryPi() {

    }

    public boolean connect() {
        try {
            this.url = new URL("http://10.0.2.2:5000");
            RetrieveFeedTask task = new RetrieveFeedTask(this);
            task.execute(this.url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getCookTime() {

        // Call makeHttpRequest(..) with appropriate value for cook time.

        // Return value from reply.
        return "";
    }

    public String getCookTemperature() {

        // Call makeHttpRequest(..) with appropriate value for cook temperature.

        // Return value from reply.
        return "";
    }

    public String getProbeTemperature() {

        // Call makeHttpRequest(..) with appropriate value for probe temperature.

        // Return value from reply.
        return "";
    }

    private String makeHttpRequest(String x) {

        // Accept String x as value to get from RPi via HTTP request.

        // Return properly formatted request.
        return "";
    }

    private void sendHttpRequest(String x) {
        // Accept string x as request to send to RPi via thread.
    }

    @Override
    public void onRetrieveFeedTaskResponse(String x) {
        System.out.println("\t\tRPi onRetrieveFeedTaskResponse: x=" + x);
    }
}
