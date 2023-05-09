package com.sese.showmethebeer.test;

import org.json.JSONException;
import org.json.JSONObject;


public class JsonObjectTest {
    public JsonObjectTest() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("name", "Jone");
        jo.put("city", "Seoul");

        System.out.println(jo.toString());
    }
}
