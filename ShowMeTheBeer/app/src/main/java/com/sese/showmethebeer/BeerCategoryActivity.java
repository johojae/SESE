package com.sese.showmethebeer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_category);

        recyclerView = findViewById(R.id.category_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoryItemLists = new ArrayList<>();
        BeerCategoryJsonParser beerCategoryJsonParser = new BeerCategoryJsonParser(this);
        categoryItemLists = beerCategoryJsonParser.GetCategoryItemLists();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.searchbar_menu, menu);
        MenuItem menuItem = menu.findItem((R.id.search));
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("맥주 이름을 입력하여 검색하세요.");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                return false;
            }

        });

        return super.onCreateOptionsMenu(menu);
    }
}