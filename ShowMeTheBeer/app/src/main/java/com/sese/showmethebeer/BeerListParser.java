package com.sese.showmethebeer;

import com.sese.showmethebeer.data.DetailBeerInfo;
import com.sese.showmethebeer.manager.DetailBeerInfoHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BeerListParser {
    List<DetailBeerInfo> beerList;

    BeerListParser(){}

    List<DetailBeerInfo> getItemList(JSONArray jsonArray){
        beerList = new ArrayList<>();

        try {
            JSONObject object = jsonArray.getJSONObject(0);
            JSONArray listArray = object.getJSONArray("list");


            for(int i=0; i<listArray.length(); i++){
                JSONObject listObject = listArray.getJSONObject(i);

                DetailBeerInfo objDetailBeerInfo = new DetailBeerInfoHelper().getDetailBeerInfo(listObject);
                beerList.add(objDetailBeerInfo);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return beerList;
    }
}
