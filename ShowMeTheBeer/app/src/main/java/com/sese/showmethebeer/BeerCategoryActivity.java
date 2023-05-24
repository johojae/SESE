package com.sese.showmethebeer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class BeerCategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<CategoryDataModel> mList;
    private CategoryItemAdapter adapter;

    private List<CategoryItem> categoryItemLists;

    private String getJsonString()
    {
        String json = "";

        try {
            InputStream is = getAssets().open("category_list.json");
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

        return json;
    }

    private void jsonParsing(String json)
    {
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_category);

        recyclerView = findViewById(R.id.category_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoryItemLists = new ArrayList<>();
        jsonParsing(getJsonString());

        mList = new ArrayList<>();

        for(int i = 0; i<categoryItemLists.size(); i++)
        {
            List<NestedList> subMenu = new ArrayList<>();
            NestedList nestedList;
            for(int j = 0; j<categoryItemLists.get(i).detailCategorys.size(); j++)
            {
                subMenu.add(new NestedList(categoryItemLists.get(i).detailCategorys.get(j).name, categoryItemLists.get(i).detailCategorys.get(j).id));
            }
            mList.add(new CategoryDataModel(subMenu, categoryItemLists.get(i).name));
        }

        adapter = new CategoryItemAdapter(mList);
        recyclerView.setAdapter(adapter);


    }
}