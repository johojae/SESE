package com.sese.showmethebeer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.sese.showmethebeer.data.DetailBeerInfo;
import com.sese.showmethebeer.serverConn.ServerManager;
import com.sese.showmethebeer.sqlite.SQLiteManager;
import com.sese.showmethebeer.data.UserBeerInfo;

import org.json.JSONException;
import org.json.JSONObject;

import me.relex.circleindicator.CircleIndicator3;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BeerListActivity extends FragmentActivity{
    public CircleIndicator3 mIndicator;
    private ViewPager2 pager;
    private PagerAdapter pm;

    private SQLiteManager sqLiteManager;

    Context context;

    List<DetailBeerInfo> beerList;
    List<UserBeerInfo> userBeerInfoList;

    boolean isUserInfo = false;
    boolean userInfoUpdateRequest = false;

    ArrayList<DetailBeerInfo> a;

    private static final int MESSAGE_ID_CATEGORY_BEER_INFO = 0;

    private static final int MESSAGE_ID_RECOMMEND_NEW_BEER_INFO = 1;

    private static final int MESSAGE_ID_RECOMMEND_RATE_BEER_INFO = 2;

    private static final int MESSAGE_ID_SEARCH_BEER_INFO = 3;
    private static final int MESSAGE_ID_BEER_INFO_ID_INFO = 4;

    private static final int MESSAGE_ID_BEER_INFO_ID_INFO_EMPTY = 5;

    private static final int MESSAGE_ID_DIALOG_ERROR_NOT_FOUND_BEER = 100;
    private static final int MESSAGE_ID_DIALOG_ERROR_OTHERS = 101;


    final Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg){
            int messageId = msg.what;

            switch (messageId) {
                case MESSAGE_ID_CATEGORY_BEER_INFO:
                case MESSAGE_ID_SEARCH_BEER_INFO:
                case MESSAGE_ID_RECOMMEND_RATE_BEER_INFO:
                    for(int i = 0; i<beerList.size(); i++){
                        a.add(i, beerList.get(i));
                    }

                case MESSAGE_ID_BEER_INFO_ID_INFO:
                    if(a.size() == 0){
                        Toast.makeText(BeerListActivity.this, "등록된 맥주가 없습니다.", Toast.LENGTH_SHORT).show(); //TODO
                    }

                    Iterator<DetailBeerInfo> it = a.iterator();

                    List<BeerListGridFragment> gridFragments = new ArrayList<BeerListGridFragment>();
                    //it = a.iterator();

                    int i = 0;
                    while(it.hasNext()){
                        ArrayList<DetailBeerInfo> tempBeerList = new ArrayList<DetailBeerInfo>();

                        tempBeerList.add(it.next());
                        i++;

                        for(int j = 0; j<8; j++)
                        {
                            if(it.hasNext()){
                                tempBeerList.add(it.next());
                                i++;
                            }
                            else {
                                break;
                            }

                        }

                        boolean isCategory = false;
                        if(messageId == MESSAGE_ID_CATEGORY_BEER_INFO){
                            isCategory = true;
                        }

                        DetailBeerInfo[] bp = {};
                        DetailBeerInfo[] beerPage = tempBeerList.toArray(bp);
                        gridFragments.add(new BeerListGridFragment(beerPage, BeerListActivity.this, isCategory));
                    }

                    pm = new PagerAdapter(BeerListActivity.this, gridFragments);
                    pager.setAdapter(pm);

                    mIndicator.setViewPager(pager);

                    break;
                case MESSAGE_ID_BEER_INFO_ID_INFO_EMPTY:
                    gridFragments = new ArrayList<BeerListGridFragment>();

                    pm = new PagerAdapter(BeerListActivity.this, gridFragments);
                    pager.setAdapter(pm);

                    mIndicator.setViewPager(pager);
                    break;
                case MESSAGE_ID_RECOMMEND_NEW_BEER_INFO:
                    sendData("rate", null);
                    break;
                case MESSAGE_ID_DIALOG_ERROR_NOT_FOUND_BEER:
                case MESSAGE_ID_DIALOG_ERROR_OTHERS:
                    showErrorDialog(messageId);
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);

        ImageButton button = (ImageButton) findViewById(R.id.beer_list_home_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        context = this;
        App app = (App)getApplication();
        sqLiteManager = app.getSQLiteManager();

        Intent intent = getIntent();

        beerList = new ArrayList<>();
        userBeerInfoList = new ArrayList<>();
        a = new ArrayList<DetailBeerInfo>();

        String called_from = intent.getStringExtra(Constants.INTENT_KEY_CALLER);

        if(called_from != null && called_from.equalsIgnoreCase(Constants.INTENT_VAL_CATEGORY)) {
            String id = intent.getStringExtra(Constants.INTENT_KEY_CATEGORY_ID);

            List<CategoryItem> categoryItemLists = new ArrayList<>();
            BeerCategoryJsonParser beerCategoryJsonParser = new BeerCategoryJsonParser(this);
            categoryItemLists = beerCategoryJsonParser.GetCategoryItemLists();

            String parentCategory = beerCategoryJsonParser.GetParentCategoryName(categoryItemLists, id);
            String detailCategory = beerCategoryJsonParser.GetDetailCategoryName(categoryItemLists, id);

            TextView title_view = (TextView) findViewById(R.id.beer_list_title);

            String title = parentCategory + " > " + detailCategory;
            title_view.setText(title);

            sendData("category", id);
        }
        else if(called_from != null && called_from.equalsIgnoreCase(Constants.INTENT_VAL_RECOMMEND)){
            TextView title_view = (TextView) findViewById(R.id.beer_list_title);

            title_view.setText("추천 맥주");

            sendData("new", null);
        }
        else if(called_from != null && called_from.equalsIgnoreCase(Constants.INTENT_VAL_SEARCH)) {
            String search = intent.getStringExtra(Constants.INTENT_KEY_SEARCH_TEXT);
            TextView title_view = (TextView) findViewById(R.id.beer_list_title);

            title_view.setText("맥주 검색 결과");

            sendData("search", search);
        }
        else if(called_from != null && called_from.equalsIgnoreCase(Constants.INTENT_VAL_USER_INFO)) {
            isUserInfo = true;

            TextView title_view = (TextView) findViewById(R.id.beer_list_title);
            title_view.setText("My Beers!");

            userBeerInfoList.addAll(sqLiteManager.getUserBeerList());

            if (!userBeerInfoList.isEmpty()) {
                String beerInfoSearchParameterById = new String();

                boolean firstFlag = true;
                for (UserBeerInfo userBeerInfo : userBeerInfoList) {
                    if (firstFlag)
                        firstFlag = false;
                    else
                        beerInfoSearchParameterById += ";";
                    beerInfoSearchParameterById += userBeerInfo.getBeerId();
                }
                sendData("beerinfo/id", beerInfoSearchParameterById);
            } else {
                Toast.makeText(BeerListActivity.this, "저장된 맥주가 없어요!", Toast.LENGTH_SHORT).show();
            }
        }

        pager = (ViewPager2) findViewById(R.id.pager);
        mIndicator = (CircleIndicator3) findViewById(R.id.pagerIndicator);
    }

    private class PagerAdapter extends FragmentStateAdapter {
        private List<BeerListGridFragment> fragments;

        public PagerAdapter(FragmentActivity activity){
            super(activity);

        }

        public PagerAdapter(FragmentActivity activity, List<BeerListGridFragment> fragments){
            super(activity);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return this.fragments.size();
        }

        /*
        public PagerAdapter(FragmentManager fm, List<GridFragment> fragments){
            super(fm);
            //super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int pos){
            return this.fragments.get(pos);
        }

        @Override
        public int getCount(){
            return this.fragments.size();
        }

         */
    }

    private void sendData(String method, String parameter) {
        if(method.equals("category")) {
            new Thread() {
                public void run() {
                    //파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                    App app = (App)getApplication();
                    ServerManager manager = app.getServerMngr();
                    manager.send(ServerManager.SUB_API_INFO_BY_CATEGORY_BEER_LIST + parameter, getCategoryListCallback());
                }
            }.start();
        }

        if(method.equals(("new"))) {
            new Thread() {
                public void run() {
                    //파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                    App app = (App)getApplication();
                    ServerManager manager = app.getServerMngr();
                    manager.send(ServerManager.SUB_API_INFO_BY_RECOMMEND_NEW_BEER_LIST, getRecommendNewListCallback());
                }
            }.start();
        }

        if(method.equals(("rate"))) {
            new Thread() {
                public void run() {
                    //파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                    App app = (App)getApplication();
                    ServerManager manager = app.getServerMngr();

                    StringBuilder param = new StringBuilder();

                    ArrayList<String> rate =  sqLiteManager.getBeerIdsByRate(10);
                    for(int i = 0; i<rate.size(); i++){
                        param.append(rate.get(i)+";");
                    }

                    manager.send(ServerManager.SUB_API_INFO_BY_RECOMMEND_RATE_BEER_LIST + param, getRecommendRateListCallback());
                }
            }.start();
        }

        if(method.equals(("search"))) {
            new Thread() {
                public void run() {
                    //파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                    App app = (App)getApplication();
                    ServerManager manager = app.getServerMngr();
                    manager.send(ServerManager.SUB_API_INFO_BY_SEARCH + parameter, getSearchListCallback());
                }
            }.start();
        }

        if(method.equals(("beerinfo/id"))) {
            new Thread() {
                public void run() {
                    //파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                    App app = (App)getApplication();
                    ServerManager manager = app.getServerMngr();
                    manager.send(ServerManager.SUB_API_INFO_BY_BEER_ID + parameter, getBeerinfoListByIdCallback());
                }
            }.start();
        }
    }

    private Callback getCategoryListCallback() {
        return new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println("BeerListActivity::onResponse:" + response);
                if (response.code() == 200) {
                    // Get response
                    String jsonData = response.body().string();
                    System.out.println("serverUrl::" + jsonData);
                    try {
                        BeerListParser beerListParser = new BeerListParser();
                        beerListParser.getItemList(beerList, new JSONObject(jsonData));

                        handler.sendEmptyMessage(MESSAGE_ID_CATEGORY_BEER_INFO);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else if (response.code() == 404) {
                    handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_NOT_FOUND_BEER);
                } else {
                    handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_OTHERS);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("BeerListActivity::onFailure:" + e.getLocalizedMessage());
                handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_OTHERS);
            }
        };
    }

    private Callback getRecommendNewListCallback() {
        return new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println("BeerListActivity::onResponse:" + response);
                if (response.code() == 200) {
                    // Get response
                    String jsonData = response.body().string();
                    System.out.println("serverUrl::" + jsonData);
                    try {
                        BeerListParser beerListParser = new BeerListParser();
                        beerListParser.getItemList(beerList, new JSONObject(jsonData));

                        handler.sendEmptyMessage(MESSAGE_ID_RECOMMEND_NEW_BEER_INFO);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else if (response.code() == 404) {
                    handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_NOT_FOUND_BEER);
                } else {
                    handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_OTHERS);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("BeerListActivity::onFailure:" + e.getLocalizedMessage());
                handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_OTHERS);
            }
        };
    }

    private Callback getRecommendRateListCallback() {
        return new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println("BeerListActivity::onResponse:" + response);
                if (response.code() == 200) {
                    // Get response
                    String jsonData = response.body().string();
                    System.out.println("serverUrl::" + jsonData);
                    try {
                        BeerListParser beerListParser = new BeerListParser();
                        beerListParser.getItemList(beerList, new JSONObject(jsonData));

                        handler.sendEmptyMessage(MESSAGE_ID_RECOMMEND_RATE_BEER_INFO);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else if (response.code() == 404) {
                    handler.sendEmptyMessage(MESSAGE_ID_RECOMMEND_RATE_BEER_INFO);
                } else {
                    handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_OTHERS);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("BeerListActivity::onFailure:" + e.getLocalizedMessage());
                handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_OTHERS);
            }
        };
    }

    private Callback getSearchListCallback() {
        return new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println("BeerListActivity::onResponse:" + response);
                if (response.code() == 200) {
                    // Get response
                    String jsonData = response.body().string();
                    System.out.println("serverUrl::" + jsonData);
                    try {
                        BeerListParser beerListParser = new BeerListParser();
                        beerListParser.getItemList(beerList, new JSONObject(jsonData));

                        handler.sendEmptyMessage(MESSAGE_ID_SEARCH_BEER_INFO);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else if (response.code() == 404) {
                    handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_NOT_FOUND_BEER);
                } else {
                    handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_OTHERS);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("BeerListActivity::onFailure:" + e.getLocalizedMessage());
                handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_OTHERS);
            }
        };
    }

    private void showErrorDialog(int messageIdErrDialog) { //에러 발생
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.dialog_error_title);

        if (messageIdErrDialog == MESSAGE_ID_DIALOG_ERROR_NOT_FOUND_BEER) {
            Toast.makeText(BeerListActivity.this, "등록된 맥주가 없습니다.", Toast.LENGTH_SHORT).show(); //TODO
        }
        else {
            builder.setMessage(R.string.dialog_error_server_error);

            builder.setPositiveButton(R.string.text_confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish(); //activity 종료시킴
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


    }

    private Callback getBeerinfoListByIdCallback() {
        return new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println("BeerListActivity::onResponse:" + response);
                if (response.code() == 200) {
                    // Get response
                    String jsonData = response.body().string();
                    System.out.println("serverUrl::" + jsonData);
                    try {
                        BeerListParser beerListParser = new BeerListParser();
                        beerListParser.getItemList(beerList, new JSONObject(jsonData));

                        for(DetailBeerInfo beerInfo:beerList)
                        {
                            for(UserBeerInfo userBeerInfo:userBeerInfoList)
                            {
                                if(beerInfo.getBeerId().equals(userBeerInfo.getBeerId())) {
                                    beerInfo.setUserRating(userBeerInfo.getRating());
                                    break;
                                }
                            }
                        }
                        Collections.sort(beerList);
                        a.clear();
                        a.addAll(beerList);

                        handler.sendEmptyMessage(MESSAGE_ID_BEER_INFO_ID_INFO);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_OTHERS);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("BeerListActivity::onFailure:" + e.getLocalizedMessage());
                handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_OTHERS);
            }
        };
    }
    @Override
    public void onResume() {
        super.onResume();
        if(isUserInfo)
        {
            if(userInfoUpdateRequest) {
                List<UserBeerInfo> userBeerInfoList = new ArrayList<>();

                Intent intent = getIntent();

                userBeerInfoList.addAll(sqLiteManager.getUserBeerList());

                if (userBeerInfoList.isEmpty()) {
                    Toast.makeText(BeerListActivity.this, "저장된 맥주가 없어요!", Toast.LENGTH_SHORT).show();

                    for(int i = 0; i < pager.getAdapter().getItemCount(); i++)
                        pager.getAdapter().notifyItemRemoved(i);
                    pager.getAdapter().notifyItemRangeRemoved(0,pager.getAdapter().getItemCount());

                    handler.sendEmptyMessage(MESSAGE_ID_BEER_INFO_ID_INFO_EMPTY);

                    return;
                }
                else if(userBeerInfoList.equals(this.userBeerInfoList))
                    return;

                beerList.clear();
                this.userBeerInfoList.clear();
                this.userBeerInfoList.addAll(userBeerInfoList);

                for(int i = 0; i < pager.getAdapter().getItemCount(); i++)
                    pager.getAdapter().notifyItemRemoved(i);
                pager.getAdapter().notifyItemRangeRemoved(0,pager.getAdapter().getItemCount());

                String beerInfoSearchParameterById = new String();

                boolean firstFlag = true;
                for(UserBeerInfo userBeerInfo:userBeerInfoList)
                {
                    if(firstFlag)
                        firstFlag = false;
                    else
                        beerInfoSearchParameterById += ";";
                    beerInfoSearchParameterById += userBeerInfo.getBeerId();
                }

                sendData("beerinfo/id", beerInfoSearchParameterById);
            }
            userInfoUpdateRequest = true;
        }
    }
}