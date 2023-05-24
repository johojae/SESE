package com.sese.showmethebeer;

import java.util.ArrayList;
import java.util.List;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;

public class BeerStoreManager {
    class StoreData {
        int distance;
        String phone;
        String place_name;
        String address_name;
        double lat;
        double lng;

        int id;
        String category_group_code;
        String category_group_name;
        String category_name;
        String place_url;
        String road_address_name;

        String logString()
        {
            return "place_name : " + place_name + ", distance = " + distance;
        }
    }

    List<StoreData> storeList = new ArrayList<StoreData>();
    List<StoreData> makeStoreList(String jsonString)
    {
        JSONObject responseJson = new JSONObject(jsonString);
        List<StoreData> tempStoreList = new ArrayList<StoreData>();

        try {
            JSONArray documents = responseJson.getJSONArray("documents");
            for (int i = 0; i < documents.length(); i++) {
                JSONObject data = documents.getJSONObject(i);
                StoreData tempStoreData = new StoreData();
                if(data.has("distance"))
                    tempStoreData.distance = data.getInt("distance");
                if(data.has("phone"))
                    tempStoreData.phone = data.getString("phone");
                if(data.has("place_name"))
                    tempStoreData.place_name = data.getString("place_name");
                if(data.has("address_name"))
                    tempStoreData.address_name = data.getString("address_name");
                if(data.has("y"))
                    tempStoreData.lat = data.getDouble("y");
                if(data.has("x"))
                    tempStoreData.lng = data.getDouble("x");
                if(data.has("id"))
                    tempStoreData.id = data.getInt("id");
                if(data.has("category_group_code"))
                    tempStoreData.category_group_code = data.getString("category_group_code");
                if(data.has("category_group_name"))
                    tempStoreData.category_group_name = data.getString("category_group_name");
                if(data.has("category_name"))
                    tempStoreData.category_name = data.getString("category_name");
                if(data.has("place_url"))
                    tempStoreData.place_url = data.getString("place_url");
                if(data.has("road_address_name"))
                    tempStoreData.road_address_name = data.getString("road_address_name");

                tempStoreList.add(tempStoreData);
            }
            storeList.addAll(tempStoreList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return storeList;
    }

    void flushStoreList() {
        storeList.clear();
    }

//        /* 키 해시 얻기*/
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("키해시는 :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
}
