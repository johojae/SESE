package com.sese.showmethebeer;

import com.sese.showmethebeer.data.DetailBeerInfo;
import com.sese.showmethebeer.manager.DetailBeerInfoHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BeerListParser {
    BeerListParser(){}

    void getItemList(List<DetailBeerInfo> beerList, JSONObject object) {
        try {
            JSONArray listArray = object.getJSONArray("list");


            for(int i=0; i<listArray.length(); i++){
                JSONObject listObject = listArray.getJSONObject(i);

                DetailBeerInfo detailBeerInfo = new DetailBeerInfo(listObject);
                beerList.add(detailBeerInfo);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
