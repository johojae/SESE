package com.sese.showmethebeer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class BeerListAdapter extends ArrayAdapter<BeerModel> {

    public BeerListAdapter(@NonNull Context context, ArrayList<BeerModel> beerModels){
        super(context, 0, beerModels);
        Log.v("맥주리스트", "7번");
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        HolderView holderView;

        Log.v("맥주리스트", "3번");

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_view_beer_item_list, parent, false);

            holderView = new HolderView(convertView);
            convertView.setTag(holderView);
        }
        else{
            holderView = (HolderView) convertView.getTag();
        }

        Log.v("맥주리스트", "4번");

        BeerModel model = getItem(position);
        holderView.icons.setImageResource(model.getIconId());
        holderView.tv.setText(model.getName());

        return convertView;
    }

    private static class HolderView{
        private final ImageView icons;
        private final TextView tv;

        public HolderView(View view) {
            icons = view.findViewById(R.id.icon_id);
            tv = view.findViewById(R.id.textview);

        }
    }
}
