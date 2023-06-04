package com.sese.showmethebeer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPager2Adapter extends FragmentStateAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        switch(position)
        {
            case 0:
                return Fragment_Map.newInstance(position);
            default:
                return FragmentPractice.newInstance(position);
        }
    }
    @Override
    public int getItemCount() {
        return 3;
    }

}
