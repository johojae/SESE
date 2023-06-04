package com.sese.showmethebeer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.view.PagerAdapter;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.sese.showmethebeer.data.DetailBeerInfo;
import com.sese.showmethebeer.manager.DetailBeerInfoHelper;
import com.sese.showmethebeer.serverConn.ServerManager;
import com.sese.showmethebeer.sqlite.SQLiteManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.relex.circleindicator.CircleIndicator3;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BeerListActivity extends FragmentActivity{
    public CircleIndicator3 mIndicator;
    private ViewPager2 pager;
    private PagerAdapter pm;

    private SQLiteManager sqLiteManager;

    Context context;

    ArrayList<BeerListCategory> beerBeerListCategory;

    List<DetailBeerInfo> beerList;

    ArrayList<DetailBeerInfo> a;
    BeerListCategory m;

    String beerNames[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
    "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33",
    "34", "35", "36", "37"};

    private static final int MESSAGE_ID_CATEGORY_BEER_INFO = 0;

    private static final int MESSAGE_ID_RECOMMEND_BEER_INFO = 1;

    final Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg){
            int messageId = msg.what;

            switch (messageId) {
                case MESSAGE_ID_CATEGORY_BEER_INFO:
                    for(int i = 0; i<beerList.size(); i++){
                        a.add(i, beerList.get(i));

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

                        DetailBeerInfo[] bp = {};
                        DetailBeerInfo[] beerPage = tempBeerList.toArray(bp);
                        gridFragments.add(new BeerListGridFragment(beerPage, BeerListActivity.this));
                    }

                    pm = new PagerAdapter(BeerListActivity.this, gridFragments);
                    pager.setAdapter(pm);

                    mIndicator.setViewPager(pager);

                    break;
                case MESSAGE_ID_RECOMMEND_BEER_INFO:
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list2);

        context = this;
        App app = (App)getApplication();
        sqLiteManager = app.getSQLiteManager();

        Intent intent = getIntent();

        a = new ArrayList<DetailBeerInfo>();


        m = new BeerListCategory();

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
                    Log.v("hojae", "thread");
                    //파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                    App app = (App)getApplication();
                    ServerManager manager = app.getServerMngr();
                    manager.send(ServerManager.SUB_API_INFO_BY_CATEGORY_BEER_LIST + parameter, getCategoryListCallback());
                }
            }.start();
        }
    }

    private Callback getCategoryListCallback() {
        return new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println("BeerListActivity::onResponse:" + response);
                Log.v("hojae", "response");
                if (response.code() == 200) {
                    // Get response
                    String jsonData = response.body().string();
                    System.out.println("serverUrl::" + jsonData);
                    try {
                        BeerListParser beerListParser = new BeerListParser();
                        beerList = beerListParser.getItemList(new JSONArray((jsonData)));

                        handler.sendEmptyMessage(MESSAGE_ID_CATEGORY_BEER_INFO);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {

                }

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("BeerListActivity::onFailure:" + e.getLocalizedMessage());
            }
        };
    }

}