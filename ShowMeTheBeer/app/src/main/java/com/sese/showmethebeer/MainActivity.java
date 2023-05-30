package com.sese.showmethebeer;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sese.showmethebeer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    Button hiddenMenuButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showMenu();
        showFloatingActionButton();

        hiddenMenuButton = binding.getRoot().getRootView().findViewById(R.id.hiddenMenuButton);
        hiddenMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HiddenMenuActivity.class);
                startActivity(intent);
            }
        });
    }
    private void showMenu() {
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
                Intent intent = new Intent(getApplicationContext(), BeerStoreManagerActivity.class);
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

    private void showFloatingActionButton() {
        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //test 기능
                App app = (App) getApplication();
                app.getSQLiteManager().saveRating("cass", 5);
                app.getSQLiteManager().saveRating("hite", 10);

                app.getSQLiteManager().getUserBeerList();
            }
        });

        FloatingActionButton fab2 = binding.fab2;
        fab2.setOnClickListener(new View.OnClickListener() { //DetailBeerInfoActivity를 띄우기 위한 임의 코드
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailBeerInfoActivity.class);
                intent.putExtra(Constants.KEY_BARCODE, "test");
                startActivity(intent);
            }
        });
    }

}