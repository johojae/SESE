package com.sese.showmethebeer;

public class Constants {

    //intent에 사용하는 상수 데이터 등 정의
    public static final String INTENT_KEY_BARCODE = "key_barcode";
    public static final String INTENT_KEY_BEERID = "key_beerId";
    public static final String INTENT_KEY_CATEGORY = "category";
    public static final String INTENT_KEY_FROM = "from";

    public static final String INTENT_KEY_CALLER = "caller";
    public static final String INTENT_KEY_CATEGORY_ID = "categoryid";
    public static final String INTENT_VAL_CATEGORY = "category";

    public static final String INTENT_VAL_SEARCH = "search";

    public static final String INTENT_KEY_SEARCH_TEXT = "searchtext";

    public static final String INTENT_VAL_RECOMMEND = "recommend";

    public static final String INTENT_KEY_TEST_MODE = "testmode";

    public static final String ACTIVITY_NAME_MAIN = "MainActivity";

    public static final String TAG = "ShowBeTheBeerDebug";

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
    public static final String KEY_SERVER_BEER_ENG_NAME = "englishName";
    public static final String KEY_SERVER_MANUFACTURER= "manufacturer";
    public static final String KEY_SERVER_ICON = "iconUrl";
    public static final String KEY_SERVER_ALCHOLOICITY = "alcoholVolume";
    public static final String KEY_SERVER_CATEGORY_ID = "categoryId";

    public static final String KEY_CARBONIC_ACID = "carbonicAcid";

    public static final String KEY_SERVER_IBU = "ibu";

    public static final String KEY_SERVER_RELATIVE_BEERS = "relatedBeers";

    public static final String KEY_SERVER_DESC = "description";

    public static final String KEY_SERVER_COUNTRY = "country";

    public static final String KEY_SERVER_NEW = "new";

    public static final String KEY_SERVER_RELATED_BEERS = "relatedBeers";
}
