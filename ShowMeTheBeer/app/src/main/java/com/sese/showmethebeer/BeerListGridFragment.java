package com.sese.showmethebeer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import com.sese.showmethebeer.data.DetailBeerInfo;

@SuppressLint("ValidFragment")
public class BeerListGridFragment extends Fragment {
    private GridView mGridView;
    private BeerListAdapter mGridAdapter;
    DetailBeerInfo[] beers = {};
    private Activity activity;

    private boolean isCategory;

    public BeerListGridFragment(DetailBeerInfo[] beers, Activity activity, boolean isCategory){
        this.beers = beers;
        this.activity = activity;
        this.isCategory = isCategory;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view;
        view = inflater.inflate(R.layout.grid_view_beer_list, container, false);
        mGridView = (GridView) view.findViewById(R.id.grid_view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if(activity != null){
            mGridAdapter = new BeerListAdapter(activity, beers, isCategory);

            if(mGridView != null){
                mGridView.setAdapter(mGridAdapter);
            }

            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
                    onGridItemClick((GridView) parent, view, pos, id);
                }
            });
        }
    }

    public void onGridItemClick(GridView g, View v, int pos, long id){
        //Toast.makeText(activity,"Position Clicked:" + pos + " & Text is: " + beers[pos].getName(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getContext(), DetailBeerInfoActivity.class);
        intent.putExtra(Constants.INTENT_KEY_FROM, "BeerListActvity");
        intent.putExtra(Constants.INTENT_KEY_BEERID, beers[pos].getBeerId());
        getContext().startActivity(intent);
    }
}
