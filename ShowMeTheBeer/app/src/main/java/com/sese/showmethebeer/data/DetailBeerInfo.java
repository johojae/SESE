package com.sese.showmethebeer.data;

import com.sese.showmethebeer.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailBeerInfo extends BeerInfo {

    private String manufacturer;
    private String alcoholicity;
    private int carbonicAcidLevel = -1;
    private String ibu;
	
    private String description; //긴 description


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
            + "\"description\": \"버드와이저 !! \n\r\n\r 미국을 대표하는 맥주로, 매년 미국에서 판매량 1위를 놓치지 않는 앤하이저부시의 대표적인 상품이다.\"}";


    public DetailBeerInfo(JSONObject jsonObj) {
        //TODO :: parsing해서 값 채워 넣어야 함
        super(jsonObj);
        // {
        //                "id": "800010000001",
        //                "name": "버드와이저",
        //                "iconUrl": "https://assets.business.veluga.kr/media/public/%E1%84%87%E1%85%A5%E1%84%83%E1%85%B3_%E1%84%8B%E1%85%A1%E1%8 "manufacturer": "오비맥주",
        //                "alcoholVolume": "5.0",
        //                "categoryId": "lager_1",
        //                "categoryName": "라거",
        //                "description": "미국을 대표하는 맥주로, 매년 미국에서 판매량 1위를 놓치지 않는 앤하이저부시의 대표적인 상품이다."
        //                }
        //
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

}
