package com.sese.showmethebeer.data;

import com.sese.showmethebeer.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailBeerInfo extends BeerInfo {

    private String manufacturer;
    private String alcoholicity;
    private int carbonicAcidLevel = -1;
    private String ibu;
	
    private String description; //긴 description

    private ArrayList<DetailBeerInfo> relatedBeers;

    //https://assets.business.veluga.kr/media/public/%E1%84%87%E1%85%A5%E1%84%83%E1%85%B3_%E1%84%8B%E1%85%A1%E1%86%AF%E1%84%85%E1%85%AE%E1%84%87%E1%85%A9%E1%84%90%E1%85%B3%E1%86%AF_1.png
    String testData = "{\"id\": \"800010000001\","
            + "\"name\": \"버드와이저\","
            +  "\"englishName\": \"Budweiser \","
            + "\"iconUrl\": \"https://assets.business.veluga.kr/media/public/%E1%84%87%E1%85%A5%E1%84%83%E1%85%B3_%E1%84%8B%E1%85%A1%E1%86%AF%E1%84%85%E1%85%AE%E1%84%87%E1%85%A9%E1%84%90%E1%85%B3%E1%86%AF_1.png\","
            + "\"manufacturer\": \"오비맥주\","
            + "\"alcoholVolume\": \"5.0\","
            + "\"categoryId\": \"lager_1\","
            + "\"categoryName\": \"라거\","
            + "\"country\": \"미국\","
            + "\"carbonicAcid\": \"3\","
            + "\"ibu\": \"12\","
            + "\"new\": false\","
            + "\"description\": \"버드와이저 !! \n\r\n\r 미국을 대표하는 맥주로, 매년 미국에서 판매량 1위를 놓치지 않는 앤하이저부시의 대표적인 상품이다.\"" // + " }";
            + ", \"relatedBeers\": [{"
                    + "\"id\": \"b00063\","
                    + "\"categoryId\": \"lager_2\","
                    + "\"iconUrl\": \"https://cdn.veluga.kr/files/supplier/undefined/drinks/20._%E1%84%92%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%90%E1%85%B3%E1%84%8C%E1%85%B5%E1%86%AB%E1%84%85%E1%85%A9_%E1%84%89%E1%85%B5%E1%86%BC%E1%84%92%E1%85%A1.png\""
                    + "}]"
            +" }";


    public DetailBeerInfo(JSONObject jsonObj) {
        //TODO :: parsing해서 값 채워 넣어야 함
        super(jsonObj);
        try {
            manufacturer = jsonObj.getString(Constants.KEY_SERVER_MANUFACTURER);
        } catch (Exception e) {
        }


        try {
            alcoholicity = jsonObj.getString(Constants.KEY_SERVER_ALCHOLOICITY);
        } catch (Exception e) {

        }

        try {
            String carbonicAcid = jsonObj.getString(Constants.KEY_CARBONIC_ACID);
            if (carbonicAcid != null && !carbonicAcid.isEmpty()) {
                carbonicAcidLevel = Integer.parseInt(carbonicAcid);
            }
        } catch (Exception e) {
        }

        try {
            ibu = jsonObj.getString(Constants.KEY_SERVER_IBU);
        } catch (Exception e) {


        }

        try {
            description = jsonObj.getString(Constants.KEY_SERVER_DESC);
        } catch (Exception e) {

        }

        try {
            JSONArray relatedBeersJsonArr= jsonObj.getJSONArray(Constants.KEY_SERVER_RELATED_BEERS);

            if (relatedBeersJsonArr != null && relatedBeersJsonArr.length() > 0) {
                relatedBeers = new ArrayList<DetailBeerInfo>();
                for (int i = 0; i <relatedBeersJsonArr.length(); i++) {
                    DetailBeerInfo relatedBeerInfo = new DetailBeerInfo(relatedBeersJsonArr.getJSONObject(i));
                    //필수 요소 체크
                    String beerId = relatedBeerInfo.getBeerId();
                    if (beerId == null || beerId.isEmpty()) {
                        System.out.println("invalid beer information");
                    } else {
                        relatedBeers.add(relatedBeerInfo);
                    }
                }
            }

        }catch (Exception e) {

        }
    }

    public DetailBeerInfo(){

    }

    public JSONObject getTestData(){
        JSONObject obj = null;
        try {
            obj = new JSONObject(testData);
        } catch(Exception e){
        }
        return obj;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getAlcoholicity() {
        if (alcoholicity != null && !alcoholicity.contains("미만") && !alcoholicity.contains("이하")) {
            return alcoholicity + " %";
        }
        return alcoholicity;
    }

    public int getCarbonicAcidLevel() {
        return carbonicAcidLevel;
    }

    public String getIbu(){
        return ibu;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<DetailBeerInfo> getRelatedBeersInfos() {
        return relatedBeers;
    }
}
