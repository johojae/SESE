package com.sese.showmethebeer;

import android.annotation.SuppressLint;
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
    ViewGroup textViewContainer;

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

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test , container, false);
        textView = new TextView(activity);

        textViewContainer = (ViewGroup) view.findViewById(R.id.textView);
        textViewContainer.addView(textView);
//
//        textView.setText();
//
//        textViewContainer = (ViewGroup) view.findViewById(R.id.textView);
//
//        TextView textView;
//        ViewGroup textViewContainer;
//
//
//
//        imgView = new ImageView(activity);
//
//        imgViewContainer = (ViewGroup) view.findViewById(R.id.textView.imageView);
////        imgViewContainer.addView(imgView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //textView.onR.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
