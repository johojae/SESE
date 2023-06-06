package com.sese.showmethebeer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

public class UserInfoActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_info);
        setContentView(R.layout.activity_beer_list);

        linearLayout = new LinearLayout(this);

        Intent intent = new Intent(linearLayout.getContext(), BeerListActivity.class);

        intent.putExtra(Constants.INTENT_KEY_CALLER, Constants.INTENT_VAL_USER_INFO);
        linearLayout.getContext().startActivity(intent);
    }
}