package com.sese.showmethebeer;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sese.showmethebeer.data.DetailBeerInfo;
import com.sese.showmethebeer.manager.ImageLoadTask;

import java.util.ArrayList;
import java.util.List;

public class BeerListAdapter extends BaseAdapter {

    Context context;

    public class ViewHolder{
        public ImageView imageView;

        public View imageViewNew;

        public TextView textCategoryTag;
        public TextView textCategory;

        public TextView textCountry;
        public TextView textName;

        public TextView textAlcoholicity;
    }

    private DetailBeerInfo[] items;
    private LayoutInflater mInflater;

    private boolean isCategory;

    public BeerListAdapter(Context context, DetailBeerInfo[] locations, boolean isCategory){
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        items = locations;
        this.isCategory = isCategory;
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
            viewHolder.textCategoryTag = (TextView) view.findViewById(R.id.grid_beer_category_tag);
            viewHolder.textCategory = (TextView) view.findViewById(R.id.grid_beer_category);
            viewHolder.textCountry = (TextView) view.findViewById(R.id.grid_beer_country);
            viewHolder.textAlcoholicity = (TextView) view.findViewById(R.id.grid_beer_alcoholicity);
            viewHolder.textName = (TextView) view.findViewById(R.id.grid_beer_name);

            viewHolder.textCategoryTag.setVisibility(View.GONE);

            if(isCategory == true)
            {
                viewHolder.textCategoryTag.setVisibility(View.INVISIBLE);
                viewHolder.textCategoryTag.setVisibility(View.GONE);
                viewHolder.textCategory.setVisibility(View.INVISIBLE);
                viewHolder.textCategory.setVisibility(View.GONE);
            }

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        DetailBeerInfo beer = items[position];

        List<CategoryItem> categoryItemLists = new ArrayList<>();
        BeerCategoryJsonParser beerCategoryJsonParser = new BeerCategoryJsonParser(view.getContext());
        categoryItemLists = beerCategoryJsonParser.GetCategoryItemLists();

        String parentCategory = beerCategoryJsonParser.GetParentCategoryName(categoryItemLists, beer.getCategoryId());
        String detailCategory = beerCategoryJsonParser.GetDetailCategoryName(categoryItemLists, beer.getCategoryId());
        String categoryText = parentCategory +" > " + detailCategory;

        SetCatImage(position, viewHolder, categoryText, beer.getCountry(), beer.getAlcoholicity(), beer.getName(), beer.getThumbnail());



        if(beer.isNew()){
            viewHolder.imageViewNew = (View)view.findViewById(R.id.icon_id_view);
            viewHolder.imageViewNew.setVisibility(View.VISIBLE);
        }

        viewHolder.textCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //beerList activity 띄우기 TODO
                Intent intent = new Intent(view.getContext(), BeerListActivity.class);
                intent.putExtra(Constants.INTENT_KEY_CALLER, Constants.INTENT_VAL_CATEGORY);
                intent.putExtra(Constants.INTENT_KEY_CATEGORY_ID, beer.getCategoryId());
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }

    private void SetCatImage(int position, ViewHolder viewHolder, String categoryText, String country, String alcoholicity, String name, String url){
        if (url != null && url.length() > 0) {
            ImageLoadTask task = new ImageLoadTask(url, viewHolder.imageView, R.drawable.beer);
            task.execute();
        }

        if (categoryText != null && !categoryText.isEmpty()) {
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            SpannableStringBuilder append = ssb.append(categoryText);
            ssb.setSpan(new URLSpan("#"), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.textCategory.setText(ssb, TextView.BufferType.SPANNABLE);
        }

        viewHolder.textCountry.setText(country + " | ");
        viewHolder.textAlcoholicity.setText(alcoholicity);
        viewHolder.textName.setText(name);
    }

    @Override
    public void notifyDataSetChanged(){
        super.notifyDataSetChanged();
    }
}
