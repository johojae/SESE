package com.sese.showmethebeer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class BeerClassifierActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_classifier);

        Toast.makeText(getApplicationContext(), "BeerClassfier", 60*60).show();
    }
}