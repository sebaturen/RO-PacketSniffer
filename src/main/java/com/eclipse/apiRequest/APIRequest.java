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

public class APIRequest {

    public static final String URL_API = "https://artofwar.cl/team_eclipse/rest";
    public static APIRequest shared = new APIRequest();

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

    public void PUT(String route, JsonObject object) {

        try {
            URL url = new URL(URL_API+route);

            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(object.toString());
            out.close();

            httpCon.getInputStream();

            if (httpCon.getResponseCode() != 200 && httpCon.getResponseCode() != 304) {
                System.out.println("Put info error! code!: "+ httpCon.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
