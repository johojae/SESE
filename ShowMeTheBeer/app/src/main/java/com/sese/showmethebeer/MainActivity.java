package com.sese.showmethebeer;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;

import com.sese.showmethebeer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showMenu();

        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void showMenu(){
        ImageView menuScan = binding.getRoot().getRootView().findViewById(R.id.id_main_menu_scan);
        menuScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BeerClassifierActivity.class);
                startActivity(intent);
            }
        });

        ImageView menuCategory = binding.getRoot().getRootView().findViewById(R.id.id_main_menu_category);
        menuCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BeerCategoryActivity.class);
                startActivity(intent);
            }
        });

        ImageView menuRecommend = binding.getRoot().getRootView().findViewById(R.id.id_main_menu_recommend);
        menuRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BeerRecommenderActivity.class);
                startActivity(intent);
            }
        });

        ImageView menuStore = binding.getRoot().getRootView().findViewById(R.id.id_main_menu_store);
        menuStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BeerStoreViewpagerManagerActivity.class);
                startActivity(intent);
            }
        });

        ImageView menuMyBeer = binding.getRoot().getRootView().findViewById(R.id.id_main_menu_my_beer);
        menuMyBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}