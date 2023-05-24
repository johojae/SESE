package com.sese.showmethebeer;

import android.util.Log;

import java.util.ArrayList;

public class BeerModelArrayList {

    public ArrayList<BeerModel> setListData()
    {
        ArrayList<BeerModel> arrayList = new ArrayList<>();
        arrayList.add(new BeerModel(R.drawable.beer,"Apple"));
        arrayList.add(new BeerModel(R.drawable.beer,"Bird"));
        arrayList.add(new BeerModel(R.drawable.beer,"Banana"));
        arrayList.add(new BeerModel(R.drawable.beer,"Monkey"));

        return arrayList;
    }
}
