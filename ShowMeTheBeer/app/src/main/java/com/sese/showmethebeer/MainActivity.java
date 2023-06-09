package com.sese.showmethebeer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.sese.showmethebeer.databinding.ActivityMainBinding;
import com.sese.showmethebeer.manager.NetworkConnectionUtil;
import com.sese.showmethebeer.sqlite.SQLiteManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private SQLiteManager sqLiteManager;

    RelativeLayout userGuideLayout;
    CoordinatorLayout mainMenuLayout;
	SearchView searchView;
	
    int guideImageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sqLiteManager = ((App)getApplication()).getSQLiteManager();

        userGuideLayout = binding.getRoot().getRootView().findViewById(R.id.userGuideLayout);
        mainMenuLayout = binding.getRoot().getRootView().findViewById(R.id.mainMenuLayout);

        //mainMenuLayout.findViewById(R.id.main_home_button).setVisibility(View.GONE);

        boolean userGuideRead = sqLiteManager.checkUserGuideRead();
        System.out.println("MainActivity :: OnCreate : checkUserGuideRead:" + userGuideRead);

        if (!userGuideRead) { //기존에 user guide를 보여주지 않았으면
            showUserGuideLayout();
        } else {
            showMainLayout();
        }
    }

    protected void onResume() {
        super.onResume();
        boolean userGuideRead = sqLiteManager.checkUserGuideRead();
        System.out.println("MainActivity :: onResume : checkUserGuideRead:" + userGuideRead);
        if (!userGuideRead) { //기존에 user guide를 보여주지 않았으면
            guideImageIndex = 0;
            showUserGuideLayout();
        } else {
            showMainLayout();
        }
    }

    private void showUserGuideLayout() {
        userGuideLayout.setVisibility(View.VISIBLE);
        mainMenuLayout.setVisibility(View.GONE);

        ImageView userGuideSkipImageView = userGuideLayout.findViewById(R.id.userGuideSkipImageView);
        ImageView userGuideNextImageView = userGuideLayout.findViewById(R.id.userGuideNextImageView);
        ImageView userGuideImageView = userGuideLayout.findViewById(R.id.userGuideImageView);

        showUserGuideMageView(userGuideImageView, getDrawableIndex());

        userGuideSkipImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMainLayout();
                sqLiteManager.updateUserGuideRead(true);
            }
        });

        userGuideNextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (guideImageIndex >=3) {
                    showMainLayout();
                    sqLiteManager.updateUserGuideRead(true);
                } else {
                    if (guideImageIndex < 3) {
                        guideImageIndex++;
                    }
                    showUserGuideMageView(userGuideImageView, getDrawableIndex());
                }
            }
        });
    }

    private int getDrawableIndex() {
        int drawableIndex = R.drawable.guide1;
        switch (guideImageIndex) {
            case 0:
                drawableIndex = R.drawable.guide0;
                break;
            case 1:
                drawableIndex = R.drawable.guide1;
                break;
            case 2:
                drawableIndex = R.drawable.guide2;
                break;
            case 3:
                drawableIndex = R.drawable.guide3;
                break;
            default :
                drawableIndex = R.drawable.guide0;
                break;
        }
        return drawableIndex;
    }

    private void showUserGuideMageView(ImageView userGuideImageView, int resourceId) {
        userGuideImageView.setImageResource(resourceId);
    }

    private void showMainLayout() {
        userGuideLayout.setVisibility(View.GONE);
        mainMenuLayout.setVisibility(View.VISIBLE);
        ImageView newBeerImageView = mainMenuLayout.findViewById(R.id.newBeerImageView);
        AppBarLayout appBarLayout = mainMenuLayout.findViewById(R.id.mainAppBar);
        appBarLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(MainActivity.this, HiddenMenuActivity.class);
                startActivity(intent);
                return true;
            }
        });

        searchView = mainMenuLayout.findViewById(R.id.beerSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                if(!NetworkConnectionUtil.isNetworkAvailable(MainActivity.this)) {
                    showNoNetworkDialog();
                    return false;
                }
                Intent intent = new Intent(MainActivity.this, BeerListActivity.class);

                intent.putExtra(Constants.INTENT_KEY_CALLER, Constants.INTENT_VAL_SEARCH);
                intent.putExtra(Constants.INTENT_KEY_SEARCH_TEXT, query);
                MainActivity.this.startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                return false;
            }

        });

        showMenu();
        showFloatingActionButton();

        newBeerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailBeerInfoActivity.class);
                intent.putExtra(Constants.INTENT_KEY_BEERID, "b00101"); //kelly
                intent.putExtra(Constants.INTENT_KEY_FROM, Constants.ACTIVITY_NAME_MAIN);
                startActivity(intent);

            }
        });

    }

    private void showMenu() {
        TableRow menuScan = mainMenuLayout.findViewById(R.id.id_main_row_scan);
        menuScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetworkConnectionUtil.isNetworkAvailable(MainActivity.this))
                {
					showNoNetworkDialog();
                    return;
                }

                //Intent intent = new Intent(MainActivity.this, BeerClassifierActivity.class);
                Intent intent = new Intent(MainActivity.this, DetailBeerInfoActivity.class);
                intent.putExtra(Constants.INTENT_KEY_TYPE, Constants.INTENT_VAL_SCAN);
                startActivity(intent);
            }
        });

        TableRow menuCategory = mainMenuLayout.findViewById(R.id.id_main_row_category);
        menuCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetworkConnectionUtil.isNetworkAvailable(MainActivity.this)) {
                    showNoNetworkDialog();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, BeerCategoryActivity.class);
                startActivity(intent);
            }
        });

        TableRow menuRecommend = mainMenuLayout.findViewById(R.id.id_main_row_recommend);
        menuRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetworkConnectionUtil.isNetworkAvailable(MainActivity.this)) {
                    showNoNetworkDialog();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, BeerListActivity.class);

                intent.putExtra(Constants.INTENT_KEY_CALLER, Constants.INTENT_VAL_RECOMMEND);
                MainActivity.this.startActivity(intent);
            }
        });

        TableRow menuStore = mainMenuLayout.findViewById(R.id.id_main_row_store);
        menuStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnectionUtil.isNetworkAvailable(MainActivity.this)) {
                    showNoNetworkDialog();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, BeerStoreManagerActivity.class);
                startActivity(intent);
            }
        });

        TableRow menuMyBeer = mainMenuLayout.findViewById(R.id.id_main_row_my_beer);
        menuMyBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetworkConnectionUtil.isNetworkAvailable(MainActivity.this)) {
                    showNoNetworkDialog();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), BeerListActivity.class);
                intent.putExtra(Constants.INTENT_KEY_CALLER, Constants.INTENT_VAL_USER_INFO);
                startActivity(intent);
            }
        });
    }

    private void showFloatingActionButton() {
        FloatingActionButton fab2 = binding.fab2; //fab2 TO BE REMOVED
        fab2.setOnClickListener(new View.OnClickListener() { //DetailBeerInfoActivity를 띄우기 위한 임의 코드
            @Override
            public void onClick(View view) {
                if(!NetworkConnectionUtil.isNetworkAvailable(MainActivity.this)) {
                    showNoNetworkDialog();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, DetailBeerInfoActivity.class);
                intent.putExtra(Constants.INTENT_KEY_BARCODE, "8801021213217");
                intent.putExtra(Constants.INTENT_KEY_TEST_MODE, true);
                startActivity(intent);
            }
        });
    }

    private void showNoNetworkDialog() {
        new AlertDialog.Builder(MainActivity.this)
            .setTitle("네트워크 에러")
            .setMessage("네트워크가 연결 된 이후에 재시도 해주세요.")
            .setPositiveButton(R.string.text_confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            })
            .create()
            .show();
    }
}