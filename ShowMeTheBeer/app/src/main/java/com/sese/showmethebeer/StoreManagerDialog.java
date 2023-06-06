package com.sese.showmethebeer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.daum.mf.map.api.MapPOIItem;

public class StoreManagerDialog extends Dialog {
    private Context context;
    private MapPOIItem poiItem;
    private StoreManagerDialogClickListener storeManagerDialogClickListener;
    public StoreManagerDialog(@NonNull Context context, MapPOIItem poiItem, StoreManagerDialogClickListener storeManagerDialogClickListener){
        super(context);
        this.context = context;
        this.poiItem = poiItem;
        this.storeManagerDialogClickListener = storeManagerDialogClickListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_callout_balloon);

        BeerStoreManager.StoreData storeData  = (BeerStoreManager.StoreData)poiItem.getUserObject();

        ((TextView) findViewById(R.id.custom_callout_balloon_name)).setText(storeData.place_name);

        if (storeData.category_name != null && storeData.category_name.length() > 0) {
            ((TextView)findViewById(R.id.categoryNameTextView)).setText(storeData.category_name);
        } else {
            findViewById(R.id.categoryNameRow).setVisibility(View.GONE);
        }

        if (storeData.phone != null && storeData.phone.length() > 0) {
            ((TextView)findViewById(R.id.phoneNumberTextView)).setText(storeData.phone);
        } else {
            findViewById(R.id.phoneNumberRow).setVisibility(View.GONE);
        }

        if (storeData.road_address_name != null && storeData.road_address_name.length() > 0) {
            ((TextView)findViewById(R.id.addressTextView)).setText(storeData.road_address_name);
        } else {
            findViewById(R.id.addressRow).setVisibility(View.GONE);
        }

        if (storeData.place_url != null && storeData.place_url.length() > 0) {
            TextView linkTextView = ((TextView)findViewById(R.id.linkTextView));
            linkTextView.setText(storeData.place_url);
        } else {
            findViewById(R.id.linkRow).setVisibility(View.GONE);

        }
    }
}
