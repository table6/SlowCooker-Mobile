package com.table6.object;

public class RaspberryPi {

    private String url;

    public RaspberryPi() {

    }

    public boolean connect() {

        // Start thread to attempt connect a finite amount of times.

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
}
