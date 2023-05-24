package com.sese.showmethebeer;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

public class BeerListActivity extends AppCompatActivity {

    GridView gridView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);
        getSupportActionBar().hide();

        gridView = findViewById(R.id.beer_grid_view);
        BeerListAdapter beerListAdapter = new BeerListAdapter(this, new BeerModelArrayList().setListData());
        gridView.setAdapter(beerListAdapter);
        //gridView.setOnItemClickListener(this);
    }

    /*
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BeerModel model = (BeerModel) parent.getItemAtPosition(position);
        Toast.makeText(this, "Clicked By:" + model.getName(), Toast.LENGTH_SHORT).show();
    }
     */
}
