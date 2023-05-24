package com.sese.showmethebeer;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BeerStoreManager2 extends AsyncTask<String, Void, List<BeerStoreManager.StoreData>>{
    double latitude, longitude;
    int radius;

    BeerStoreManager2(double _latitude, double _longitude, int _radius)
    {
        latitude = _latitude;
        longitude = _longitude;
        radius = _radius;
    }
    String clientKey = "KakaoAK c902cfe962c6fe2b91572930b36db204";
    private String str, receiveMsg;

    List<BeerStoreManager.StoreData> storeList = new ArrayList<BeerStoreManager.StoreData>();

    @Override
    protected List<BeerStoreManager.StoreData> doInBackground(String... params) {
        URL url = null;
        BeerStoreManager beerStoreManager = new BeerStoreManager();
        try {
            for(int qIdx = 1; qIdx <= 45; qIdx++)
            {
                url = new URL("https://dapi.kakao.com/v2/local/search/keyword.json?x=" + longitude + "&y=" + latitude + "&radius=" + radius + "&page=" + qIdx + "&sort=distance&query=호프"); // 서버 URL

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
                    Log.i("receiveMsg : ", receiveMsg);

                    reader.close();
                } else {
                    Log.i("결과", conn.getResponseCode() + "Error");
                }

                try {
                    storeList.addAll(beerStoreManager.makeStoreList(receiveMsg));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                if(beerStoreManager.is_end)
                    break;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return storeList;
    }
}
