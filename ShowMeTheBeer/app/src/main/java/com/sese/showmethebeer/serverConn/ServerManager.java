package com.sese.showmethebeer.serverConn;

import android.content.Context;

import com.sese.showmethebeer.App;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;

public class ServerManager {
    /*private static OkHttpClient httpClient = new OkHttpClient();

    private static ServerManager instance = new ServerManager();

    public static ServerManager getInstance(Context context) {
        return instance;
    }

    private ServerManager() {
        this.httpClient = new OkHttpClient();
    }
*/
    App app = null;
    Context context = null;

    String ipAddr = null;

    public static final String SUB_API_INFO_BY_BARCODE = "beerinfo/barcode/";
    public static final String SUB_API_INFO_BY_BEER_ID = "beerinfo/beerid/";

    public ServerManager(App app, Context context) {
        this.app = app;
        this.context = context;
        ipAddr = "https://192.168.1.176:8443"; //app.getSQLiteManager().getServerIpAddress(); //ex, 192.168.0.10:1234
    }

    public Call send(String apiSubUrl, Callback callback) {
        if (ipAddr == null || ipAddr.length() == 0) {
            return null;
        }
        
        try {
            String serverUrl = ipAddr + "/" + apiSubUrl;

            System.out.println("serverUrl :: " + serverUrl);

            Request request = new Request.Builder()
                    .url(serverUrl)
                    .build();

            Call call = TrustOkHttpClientUtil.getUnsafeOkHttpClient().build().newCall(request);
            call.enqueue(callback);

            return call;
        } catch (Exception e) {
            System.out.println("serverUrl :: exception");
            e.printStackTrace();
        }
        return null;
    }

    public void updateServerIpAddress(String ipAddress) {
        ipAddr = ipAddress;
    }
}
