package com.sese.showmethebeer.manager;

import com.sese.showmethebeer.Constants;
import com.sese.showmethebeer.data.DetailBeerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailBeerInfoHelper {
    public DetailBeerInfoHelper() {

    }

    //RemoteServer에서 준 response를 가지고, DetailBeerInfo를 만들어서 return
    //원래는 DetailBeerInfoHelper에서 ServerConnection까지 관리하려했는데 현재는 필요없어 보임
    public DetailBeerInfo getDetailBeerInfo(JSONObject respObj)  {
        /*
    {
    "id": "b00033",
    "name": "버드와이저",
    "englishName": "Budweiser",
    "manufacturer": "앤하이저부시 인베브",
    "country": "미국",
    "alcoholVolume": "5.0",
    "categoryId": "lager_2",
    "iconUrl": "https://assets.business.veluga.kr/media/public/%E1%84%87%E1%85%A5%E1%84%83%E1%85%B3_%E1%84%8B%E1%85%A1%E1%86%AF%E1%84%85%E1%85%AE%E1%84%87%E1%85%A9%E1%84%90%E1%85%B3%E1%86%AF_1.png",
    "description": "세계 맥주 브랜드 가치 1위, 미국 대표 라거 맥주, 버드와이저는 90% 이상의 강력한 브랜드 인지도를 가진 맥주입니다. 버드와이저가 등장한 1876년부터 미국 내에서 판매량 1위를 지키고 있다고 합니다. 부드러운 목 넘김을 선사하기 위해 '비치우드 에이징'이라는 독특한 방법으로 21일 이상 숙성해 만든 진정한 King of Beers입니다. ",
    "carbonicAcid": "3",
    "ibu": "12",
    "relatedBeers": []
    }
         */
        try {
            //JSONArray list = respObj.getJSONArray(Constants.KEY_SERVER_LIST);
            //if (list != null && list.length() > 0) {
                DetailBeerInfo detailBeerInfo = new DetailBeerInfo(respObj);
                return detailBeerInfo;
            //}

        } catch (Exception e) {
            return null;
        }

    }
}
