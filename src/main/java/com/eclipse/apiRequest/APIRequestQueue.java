package com.eclipse.apiRequest;

import com.eclipse.sniffer.Sniffer;
import com.google.gson.JsonObject;

public class APIRequestQueue {

    private String route;
    private JsonObject info;

    public APIRequestQueue(String route, JsonObject info) {
        this.route = route;
        this.info = info;

        if (!this.info.has("api_key")) {
            this.info.addProperty("api_key", Sniffer.getApiKey());
        }
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public JsonObject getInfo() {
        return info;
    }

    public void setInfo(JsonObject info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"APIRequestQueue\", " +
                "\"route\":" + (route == null ? "null" : "\"" + route + "\"") + ", " +
                "\"info\":" + (info == null ? "null" : info) +
                "}";
    }
}
