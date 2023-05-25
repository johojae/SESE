package com.sese.showmethebeer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPager2Adapter extends FragmentStateAdapter {

//    public FgAdapter(@NonNull FragmentActivity fragmentActivity) {
//        super(fragmentActivity);
//    }

    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return FragmentPractice.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
