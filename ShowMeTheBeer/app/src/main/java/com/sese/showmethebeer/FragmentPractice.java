package com.sese.showmethebeer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentPractice extends Fragment {
    static int pageNumber = -1;

    public static FragmentPractice newInstance(int number) {
        FragmentPractice fp = new FragmentPractice();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fp.setArguments(bundle);
        pageNumber = number;
        return fp;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        int resource;
        switch(pageNumber)
        {
            case 0:
                resource = R.layout.activity_beer_store_manager;
                break;
            case 1:
            case 2:
                resource = R.layout.test;
                break;
            default:
                resource = 0;
                break;
        }

        return inflater.inflate(resource, container, false);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            int num = getArguments().getInt("number");
        }
    }

//    @Nullable
//    //@Override
//    public View OnCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return LayoutInflater.from(inflater.getContext()).inflate(R.layout.activity_beer_store_manager,container,false);
//    }
}
