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
    BeerStoreManagerActivity beerStoreManagerActivity;
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













//    public class CollectionDemoFragment extends Fragment {
//        // When requested, this adapter returns a DemoObjectFragment,
//        // representing an object in the collection.
//        DemoCollectionAdapter demoCollectionAdapter;
//        ViewPager2 viewPager;
//
//        @Nullable
//        @Override
//        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                                 @Nullable Bundle savedInstanceState) {
//            return inflater.inflate(R.layout.activity_beer_store_viewpager_manager, container, false);
//        }
//
//        @Override
//        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//
//            demoCollectionAdapter = new DemoCollectionAdapter(this);
//            viewPager = view.findViewById(R.id.pager);
//            viewPager.setAdapter(demoCollectionAdapter);
//
//            TabLayout tabLayout = view.findViewById(R.id.tab_layout);
//            new TabLayoutMediator(tabLayout, viewPager,
//                    (tab, position) -> tab.setText("OBJECT " + (position + 1))
//            ).attach();
//        }
//
////        @Override
////        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////            demoCollectionAdapter = new DemoCollectionAdapter(this);
////            viewPager = view.findViewById(R.id.pager);
////            viewPager.setAdapter(demoCollectionAdapter);
////        }
//    }
//
//    public class DemoCollectionAdapter extends FragmentStateAdapter {
//        public DemoCollectionAdapter(Fragment fragment) {
//            super(fragment);
//        }
//
//        @NonNull
//        @Override
//        public Fragment createFragment(int position) {
//            // Return a NEW fragment instance in createFragment(int)
//            Fragment fragment = new DemoObjectFragment();
//            Bundle args = new Bundle();
//            // Our object is just an integer :-P
//            args.putInt(DemoObjectFragment.ARG_OBJECT, position + 1);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public int getItemCount() {
//            return 100;
//        }
//    }
//
//    // Instances of this class are fragments representing a single
//// object in our collection.
//    public class DemoObjectFragment extends Fragment {
//        public static final String ARG_OBJECT = "object";
//
//        @Nullable
//        @Override
//        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                                 @Nullable Bundle savedInstanceState) {
//            return inflater.inflate(R.layout.test, container, false);
//        }
//
//        @Override
//        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//            Bundle args = getArguments();
//            ((TextView) view.findViewById(android.R.id.text1))
//                    .setText(Integer.toString(args.getInt(ARG_OBJECT)));
//        }
//    }


}
