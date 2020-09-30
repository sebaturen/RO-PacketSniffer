package com.eclipse.apiRequest;

import com.eclipse.gameDetailsDecrypt.ActorDecrypt;
import com.eclipse.gameDetailsDecrypt.GeneralInfoDecrypt;
import com.eclipse.gameDetailsDecrypt.GuildDetailDecrypt;
import com.eclipse.guildWoeBreaker.WoEBreaker;
import com.eclipse.sniffer.network.PacketDecryption;
import com.eclipse.sniffer.network.ROPacketDetail;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

        // Schedule to try send the queue elements
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            if (queue.size() > 0) {
                System.out.println("API Queue Size: "+ queue.size());
                try {
                    APIRequestQueue q = queue.remove(0);
                    request(q.getType(), q);
                } catch (IndexOutOfBoundsException e) {
                    // The queue not have element, other thread use
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);

    }

    public void POST(APIRequestQueue request) {
        request(request.getType(), request);
    }

    public void PUT(APIRequestQueue request) {
        request(request.getType(), request);
    }

    private void request(String type, APIRequestQueue request) {

        try {
            URL url = new URL(URL_API+request.getRoute());

            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod(type);
            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(request.getInfo().toString());
            out.close();

            int responseCode = httpCon.getResponseCode();
            System.out.println(responseCode);

            if (responseCode == 200) {
                httpCon.getInputStream();
            }

            if (responseCode != 200 && responseCode != 304 && responseCode != 403) {
                if (responseCode == 500) {
                    queue.add(request);
                }
                System.out.println("Put info error! code!: "+ responseCode);
            } else {
                if (queue.size() > 0) {
                    System.out.println("API Queue Size: "+ queue.size());
                    try {
                        APIRequestQueue q = queue.remove(0);
                        request(q.getType(), q);
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
