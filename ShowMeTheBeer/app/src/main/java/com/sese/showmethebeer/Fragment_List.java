package com.sese.showmethebeer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.daum.mf.map.api.MapView;

public class Fragment_List extends Fragment {
    Activity activity;
    ImageView imgView;
    ViewGroup imgViewContainer;

    public static Fragment_List newInstance(int number) {
        Fragment_List fp = new Fragment_List();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fp.setArguments(bundle);
        return fp;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test , container, false);
        imgView = new ImageView(activity);

//        imgViewContainer = (ViewGroup) view.findViewById(R.id.imageView);
//        imgViewContainer.addView(imgView);

        return view;
    }
}
