package com.sese.showmethebeer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

public class BeerStoreViewpagerManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_store_viewpager_manager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        ViewPager2 viewPager2 = findViewById(R.id.pager);

        ViewPager2Adapter fgAdapter = new ViewPager2Adapter(this);

        viewPager2.setAdapter(fgAdapter);
        viewPager2.setUserInputEnabled(false);

        final List<String> tabElement = Arrays.asList("Map","List","Detail");

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView textView = new TextView(BeerStoreViewpagerManagerActivity.this);
                textView.setText(tabElement.get(position));
                tab.setCustomView(textView);
            }
        }).attach();
    }
}
