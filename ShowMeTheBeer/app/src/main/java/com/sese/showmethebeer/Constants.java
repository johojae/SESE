package com.sese.showmethebeer;

public class Constants {

    //intent에 사용하는 상수 데이터 등 정의
    public static final String KEY_BARCODE = "key_barcode";


    //REST API의 response에 사용되는 KEY
    /*"count": 1,
            "list": [
    {
        "id": "800010000001",
            "name": "버드와이저",
            "iconUrl": "https://assets.business.veluga.kr/media/public/%E1%84%87%E1%85%A5%E1%84%83%E1%85%B3_%E1%84%8B%E1%85%A1%E1%8 "manufacturer": "오비맥주",
        "alcoholVolume": "5.0",
            "categoryId": "lager_1",
            "categoryName": "라거",
            "description": "미국을 대표하는 맥주로, 매년 미국에서 판매량 1위를 놓치지 않는 앤하이저부시의 대표적인 상품이다."
    }*/
    public static final String KEY_SERVER_COUNT = "count";
    public static final String KEY_SERVER_LIST = "list";
    public static final String KEY_SERVER_ID = "id";
    public static final String KEY_SERVER_BEER_NAME = "name";
    public static final String KEY_SERVER_MANUFACTURER= "manufacturer";
    public static final String KEY_SERVER_ICON = "iconUrl";
    public static final String KEY_SERVER_ALCHOLOICITY = "alcoholVolume";
    public static final String KEY_SERVER_CATEGORY_ID = "categoryId";
    public static final String KEY_SERVER_DESC = "description";

    public static final String KEY_SERVER_COUNTRY = "country";
}
