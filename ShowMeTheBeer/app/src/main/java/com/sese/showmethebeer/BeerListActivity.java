package com.sese.showmethebeer;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import me.relex.circleindicator.CircleIndicator3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BeerListActivity extends FragmentActivity{
    public CircleIndicator3 mIndicator;
    private ViewPager2 pager;
    private PagerAdapter pm;

    ArrayList<BeerListCategory> beerBeerListCategory;

    String beerNames[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
    "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33",
    "34", "35", "36", "37"};

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list2);


        Log.v("hojae", "onCreate");

        Intent intent = getIntent();

        String called_from = intent.getStringExtra("caller");

        if(called_from != null && called_from.equalsIgnoreCase("category")) {
            String id = intent.getStringExtra("categoryid");

            List<CategoryItem> categoryItemLists = new ArrayList<>();
            BeerCategoryJsonParser beerCategoryJsonParser = new BeerCategoryJsonParser(this);
            categoryItemLists = beerCategoryJsonParser.GetCategoryItemLists();

            String parentCategory = beerCategoryJsonParser.GetParentCategoryName(categoryItemLists, id);
            String detailCategory = beerCategoryJsonParser.GetDetailCategoryName(categoryItemLists, id);

            TextView title_view = (TextView) findViewById(R.id.beer_list_title);

            String title = parentCategory + " > " + detailCategory;
            title_view.setText(title);
        }

        pager = (ViewPager2) findViewById(R.id.pager);
        mIndicator = (CircleIndicator3) findViewById(R.id.pagerIndicator);

        ArrayList<String> a = new ArrayList<String>();

        BeerListCategory m = new BeerListCategory();

        for(int i = 0; i<beerNames.length; i++){
            a.add(i, beerNames[i]);
            m.name = a.get(i);
        }

        beerBeerListCategory = new ArrayList<BeerListCategory>();
        beerBeerListCategory.add(m);

        Iterator<String> it = a.iterator();

        List<BeerListGridFragment> gridFragments = new ArrayList<BeerListGridFragment>();
        it = a.iterator();

        int i = 0;
        while(it.hasNext()){
            ArrayList<BeerModel> beerList = new ArrayList<BeerModel>();

            BeerModel beer = new BeerModel(0, it.next());
            beerList.add(beer);
            i = i+1;

            if(it.hasNext()){
                BeerModel beer1 = new BeerModel(1, it.next());
                beerList.add(beer1);
                i = i+1;
            }

            if(it.hasNext()){
                BeerModel beer2 = new BeerModel(2, it.next());
                beerList.add(beer2);
                i = i+1;
            }

            if(it.hasNext()){
                BeerModel beer3 = new BeerModel(3, it.next());
                beerList.add(beer3);
                i = i+1;
            }

            if(it.hasNext()){
                BeerModel beer4 = new BeerModel(4, it.next());
                beerList.add(beer4);
                i = i+1;
            }

            if(it.hasNext()){
                BeerModel beer5 = new BeerModel(5, it.next());
                beerList.add(beer5);
                i = i+1;
            }

            if(it.hasNext()){
                BeerModel beer6 = new BeerModel(6, it.next());
                beerList.add(beer6);
                i = i+1;
            }

            if(it.hasNext()){
                BeerModel beer7 = new BeerModel(7, it.next());
                beerList.add(beer7);
                i = i+1;
            }

            if(it.hasNext()){
                BeerModel beer8 = new BeerModel(7, it.next());
                beerList.add(beer8);
                i = i+1;
            }

            BeerModel[] bp = {};
            BeerModel[] beerPage = beerList.toArray(bp);
            gridFragments.add(new BeerListGridFragment(beerPage, BeerListActivity.this));
        }

        //pm = new PagerAdapter(getSupportFragmentManager(), gridFragments);
        pm = new PagerAdapter(this, gridFragments);
        pager.setAdapter(pm);

        mIndicator.setViewPager(pager);


        //final float density = getResources().getDisplayMetrics().density;
        //mIndicator.setRadius(4 * density);
        //mIndicator.setFillColor(0xFF8692f7);
        //mIndicator.setPageColor(0xFFFFFFFF);
        //mIndicator.setStrokeColor(0xFFD3D3D3);
        //mIndicator.setStrokeWidth(density);
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

}