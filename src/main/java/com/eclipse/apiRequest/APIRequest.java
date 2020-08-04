package com.eclipse.apiRequest;

import com.google.gson.JsonObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class APIRequest {

    public static final String URL_API = "https://panel.delmal.cl/rest";
    public static APIRequest shared = new APIRequest();
    private static List<APIRequestQueue> queue = new ArrayList<>();

    public APIRequest() {

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }

    }

    public void PUT(APIRequestQueue request) {

        try {
            URL url = new URL(URL_API+request.getRoute());

            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(request.getInfo().toString());
            out.close();

            httpCon.getInputStream();
            int responseCode = httpCon.getResponseCode();

            if (responseCode != 200 && responseCode != 304) {
                if (responseCode == 500) {
                    queue.add(request);
                }
                System.out.println("Put info error! code!: "+ httpCon.getResponseCode());
            } else {
                if (queue.size() > 0) {
                    System.out.println("API Queue Size: "+ queue.size());
                    try {
                        PUT(queue.remove(0));
                    } catch (IndexOutOfBoundsException e) {
                        // The queue not have element, other thread use
                    }
                }
            }

        } catch (IOException e) {
            queue.add(request);
            System.out.println("API Connection exception "+ e);
            System.out.println("Request: "+ request);
        }

    }
}
