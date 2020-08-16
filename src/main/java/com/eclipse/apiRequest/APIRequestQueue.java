package com.eclipse.apiRequest;

import com.eclipse.sniffer.Sniffer;
import com.google.gson.JsonObject;

public class APIRequestQueue {

    private String route;
    private JsonObject info;
    private String type;

    public APIRequestQueue(String route, JsonObject info, String type) {
        this.route = route;
        this.info = info;
        this.type = type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"APIRequestQueue\", " +
                "\"route\":" + (route == null ? "null" : "\"" + route + "\"") + ", " +
                "\"info\":" + (info == null ? "null" : info) + ", " +
                "\"type\":" + (type == null ? "null" : "\"" + type + "\"") +
                "}";
    }
}
