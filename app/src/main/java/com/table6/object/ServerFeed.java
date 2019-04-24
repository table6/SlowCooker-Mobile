package com.table6.object;

import org.json.JSONObject;

public class ServerFeed {
    private String directory;
    private JSONObject json;

    public ServerFeed() {
        this.directory = "";
        this.json = new JSONObject();
    }

    public ServerFeed(String directory, JSONObject json) {
        this.directory = directory;
        this.json = json;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return this.directory;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public JSONObject getJson() {
        return this.json;
    }

    public String toString() {
        return this.directory + ": " + this.json.toString();
    }

}
