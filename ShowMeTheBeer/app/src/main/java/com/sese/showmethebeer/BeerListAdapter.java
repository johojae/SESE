package com.sese.showmethebeer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sese.showmethebeer.data.DetailBeerInfo;

public class BeerListAdapter extends BaseAdapter {

    Context context;

    public class ViewHolder{
        public ImageView imageView;
        public TextView textTitle;
    }

    private DetailBeerInfo[] items;
    private LayoutInflater mInflater;

    public BeerListAdapter(Context context, DetailBeerInfo[] locations){
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        items = locations;
    }

    public DetailBeerInfo[] getItems(){
        return items;
    }

    public void setItems (DetailBeerInfo[] items){
        this.items = items;
    }

    @Override
    public int getCount() {
        if(items != null){
            return items.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(items != null && position >=0 && position < getCount()){
            return items[position];
        }
        return null;
    }

    public long getItemId(int position) {
        if(items != null && position >=0 && position < getCount()){
            //return items[position].;
            return position;
        }
       return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if(view == null){
            view = mInflater.inflate(R.layout.grid_view_beer_item_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.icon_id);
            viewHolder.textTitle = (TextView) view.findViewById((R.id.textview));
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        DetailBeerInfo beer = items[position];
        SetCatImage(position, viewHolder, beer.getName(), beer.getThumbnail());

        return view;
    }

    private void SetCatImage(int position, ViewHolder viewHolder, String title, String url){
        if (url != null && url.length() > 0) {
            ImageLoadTask task = new ImageLoadTask(url, viewHolder.imageView);
            task.execute();
        }
        else {
            viewHolder.imageView.setImageResource(R.drawable.beer);
        }
        viewHolder.textTitle.setText(title);
    }

    @Override
    public void notifyDataSetChanged(){
        super.notifyDataSetChanged();
    }
}
