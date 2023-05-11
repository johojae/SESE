package com.sese.showmethebeer.data;

import org.json.JSONException;
import org.json.JSONObject;

public class BeerInfo {
    private String beerId;
    private String name;
    private String category;
    private String thumbnail;
    private String country;

    public BeerInfo(JSONObject jsonObj) {
        //TODO :: parsing해서 값 채워 넣어야 함

    }

    public String getBeerId() {
        return beerId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getCountry() {
        return country;
    }

}
