package com.sese.showmethebeer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BeerListActivity extends AppCompatActivity {

    GridView gridView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);
        //getSupportActionBar().hide();

        gridView = findViewById(R.id.beer_grid_view);
        BeerListAdapter beerListAdapter = new BeerListAdapter(this, new BeerModelArrayList().setListData());
        gridView.setAdapter(beerListAdapter);
        //gridView.setOnItemClickListener(this);

        Intent intent = getIntent();

        String caller = intent.getStringExtra("caller");

        if(caller.equals("category")) {
            String parentCategory = intent.getStringExtra("parentCategory");
            String detailCategory = intent.getStringExtra("detailCategory");

            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null){
                actionBar.setTitle(parentCategory + " > " + detailCategory);
            }

            String title = parentCategory + " > " + detailCategory;
            actionBar.setTitle(title);
            actionBar.show();
        }

    }

    /*
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BeerModel model = (BeerModel) parent.getItemAtPosition(position);
        Toast.makeText(this, "Clicked By:" + model.getName(), Toast.LENGTH_SHORT).show();
    }
     */
}
