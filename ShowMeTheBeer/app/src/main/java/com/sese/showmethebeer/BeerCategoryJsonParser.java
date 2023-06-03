package com.sese.showmethebeer;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BeerCategoryJsonParser {

    private Context context;
    private List<CategoryItem> categoryItemLists;

    public BeerCategoryJsonParser(Context context){
        this.context = context;
    }

    public List<CategoryItem> GetCategoryItemLists(){
        String json = "";

        categoryItemLists = new ArrayList<>();

        try {
            InputStream is = context.getAssets().open("category_list.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray categoryArray = jsonObject.getJSONArray("category");

            for(int i=0; i<categoryArray.length(); i++)
            {
                JSONObject categoryObject = categoryArray.getJSONObject(i);

                CategoryItem categoryItem = new CategoryItem();

                categoryItem.setId(categoryObject.getString("id"));
                categoryItem.setName(categoryObject.getString("name"));

                categoryItem.detailCategorys = new ArrayList<>();

                JSONArray listArray= categoryObject.getJSONArray("list");

                for(int j = 0; j<listArray.length(); j++)
                {
                    JSONObject listObject = listArray.getJSONObject(j);

                    DetailCategory detailCategory = new DetailCategory();

                    detailCategory.setId(listObject.getString("id"));
                    detailCategory.setName(listObject.getString("name"));

                    categoryItem.detailCategorys.add(detailCategory);
                }

                categoryItemLists.add(categoryItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return categoryItemLists;
    }

    public String GetParentCategoryName(List<CategoryItem> list, String id){
        for(int i = 0; i<list.size(); i++){
            for(int j = 0; j<list.get(i).detailCategorys.size(); j++){
                if(list.get(i).detailCategorys.get(j).id.equals(id)){
                    return list.get(i).name;
                }
            }
        }

        return null;
    }

    public String GetDetailCategoryName(List<CategoryItem> list, String id){
        for(int i = 0; i<list.size(); i++){
            for(int j = 0; j<list.get(i).detailCategorys.size(); j++){
                if(list.get(i).detailCategorys.get(j).id.equals(id)){
                    return list.get(i).detailCategorys.get(j).name;
                }
            }
        }

        return null;
    }


}
