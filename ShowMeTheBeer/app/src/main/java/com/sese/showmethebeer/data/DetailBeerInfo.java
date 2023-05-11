package com.sese.showmethebeer.data;

import org.json.JSONObject;

public class DetailBeerInfo extends BeerInfo{

    private String manufacturer;
    private Float alcoholicity;
    private String shortTitle; //간략한 설명
    private String description; //긴 description

    public DetailBeerInfo(JSONObject jsonObj) {
        //TODO :: parsing해서 값 채워 넣어야 함
        super(jsonObj);

    }
}
