package com.sese.showmethebeer;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class BeerStoreManager {
    String clientKey = "KakaoAK c902cfe962c6fe2b91572930b36db204";
    //String address = "https://dapi.kakao.com/v2/local/search/keyword.json?";
    String str;
    String receiveMsg;
    String SearchStores(double lat, double lng, int rad) {

        URL url = null;
        try {
            url = new URL("https://dapi.kakao.com/v2/local/search/keyword.json?y=37.514322572335935&x=127.06283102249932&radius=20000&query=호프"); // 서버 URL

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("Authorization", clientKey);

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                //Log.i("receiveMsg : ", receiveMsg);

                reader.close();
            } else {
                //Log.i("결과", conn.getResponseCode() + "Error");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }
}
