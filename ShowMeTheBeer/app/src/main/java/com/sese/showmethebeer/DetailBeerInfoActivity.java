package com.sese.showmethebeer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class DetailBeerInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_beer_info);


        Intent intent = getIntent();
        String barcode = intent.getStringExtra(Constants.KEY_BARCODE);
        Toast.makeText(this, "barcode :: " + barcode, 60*60).show();
    }
}