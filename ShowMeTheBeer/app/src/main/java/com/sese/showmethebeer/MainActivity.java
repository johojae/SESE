package com.sese.showmethebeer;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.sese.showmethebeer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    ImageView newBeerImageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        newBeerImageView = binding.getRoot().getRootView().findViewById(R.id.newBeerImageView);

        AppBarLayout appBarLayout = binding.getRoot().getRootView().findViewById(R.id.mainAppBar);
        appBarLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(getApplicationContext(), HiddenMenuActivity.class);
                startActivity(intent);
                return true;
            }
        });

        showMenu();
        showFloatingActionButton();

        newBeerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailBeerInfoActivity.class);
                intent.putExtra(Constants.INTENT_KEY_BEERID, "b00101"); //kelly
                intent.putExtra(Constants.INTENT_KEY_FROM, Constants.ACTIVITY_NAME_MAIN);
                startActivity(intent);

            }
        });
    }

    private void showMenu() {
        TableRow menuScan = binding.getRoot().getRootView().findViewById(R.id.id_main_row_scan);
        menuScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BeerClassifierActivity.class);
                startActivity(intent);
            }
        });

        TableRow menuCategory = binding.getRoot().getRootView().findViewById(R.id.id_main_row_category);
        menuCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BeerCategoryActivity.class);
                startActivity(intent);
            }
        });

        TableRow menuRecommend = binding.getRoot().getRootView().findViewById(R.id.id_main_row_recommend);
        menuRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BeerRecommenderActivity.class); //TODO
                startActivity(intent);
            }
        });

        TableRow menuStore = binding.getRoot().getRootView().findViewById(R.id.id_main_row_store);
        menuStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BeerStoreManagerActivity.class);
                startActivity(intent);
            }
        });

        TableRow menuMyBeer = binding.getRoot().getRootView().findViewById(R.id.id_main_row_my_beer);
        menuMyBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showFloatingActionButton() {
        FloatingActionButton fab2 = binding.fab2; //fab2 TO BE REMOVED
        fab2.setOnClickListener(new View.OnClickListener() { //DetailBeerInfoActivity를 띄우기 위한 임의 코드
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailBeerInfoActivity.class);
                intent.putExtra(Constants.INTENT_KEY_BARCODE, "8801021213217");
                intent.putExtra(Constants.INTENT_KEY_TEST_MODE, true);
                startActivity(intent);
            }
        });
    }

}