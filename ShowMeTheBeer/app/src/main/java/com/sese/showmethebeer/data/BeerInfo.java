package com.sese.showmethebeer.data;

import com.sese.showmethebeer.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class BeerInfo {
    protected String beerId;
    protected String name;
    protected String engName;

    protected String categoryId;
    protected String thumbnail;
    protected String country;

    protected boolean isNew = false;

    public BeerInfo(JSONObject jsonObj) {
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
            beerId = jsonObj.getString(Constants.KEY_SERVER_ID);
        } catch (Exception e) {
        }

        try {
            name = jsonObj.getString(Constants.KEY_SERVER_BEER_NAME);
        } catch (Exception e) {
        }

        try {
            engName = jsonObj.getString(Constants.KEY_SERVER_BEER_ENG_NAME);
        } catch (Exception e) {
        }

        try {
            categoryId = jsonObj.getString(Constants.KEY_SERVER_CATEGORY_ID);
        } catch (Exception e) {
        }

        try {
            thumbnail = jsonObj.getString(Constants.KEY_SERVER_ICON);
        } catch (Exception e) {
        }

        try {
            country = jsonObj.getString(Constants.KEY_SERVER_COUNTRY);
        } catch (Exception e) {
        }

        try {
            if (jsonObj.has(Constants.KEY_SERVER_NEW)) {
                isNew = jsonObj.getBoolean(Constants.KEY_SERVER_NEW);
            }
        } catch (Exception e) {
        }
    }

    public BeerInfo() { //test code

    }

    public String getBeerId() {
        return beerId;
    }

    public String getName() {
        return name;
    }

    public String getEngName() {
        return engName;
    }

    /*public String getCategory() {
        return category;
    }*/

    public String getCategoryId() {
        return categoryId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getCountry() {
        return country;
    }

    public boolean isNew() { return isNew; }

}
