package com.sese.showmethebeer;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.daum.mf.map.api.MapView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Fragment_List extends Fragment implements Serializable {
    Activity activity;
    View view;

    RecyclerView recyclerView;
    ArrayList<String> storeListStringArray = new ArrayList<String>();
    List<itemData> itemDataList = new ArrayList<itemData>();
    Adapter dataAdapter = new Adapter(itemDataList);
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

        getParentFragmentManager().setFragmentResultListener("storeList", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                storeListStringArray.clear();
                storeListStringArray.addAll(bundle.getStringArrayList("data"));
                itemDataList.clear();
            }
        });
    }

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_beer_store_list , container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL)); // 구분선

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(dataAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        int sizeOfStringArray = storeListStringArray.size();
        if(sizeOfStringArray == 0)
        {
            itemData data = new itemData(0, "빈 리스트", "");
            itemDataList.add(data);
        }
        else {
            int sizeOfStoreList = Integer.valueOf(storeListStringArray.get(0));

//            List<itemData> tempList = new ArrayList<itemData>();
            if (sizeOfStoreList == (sizeOfStringArray - 1)/3) {
                for (int idx = 0; idx < sizeOfStoreList; idx++) {
                    int distance;
                    String name = storeListStringArray.get(idx * 3 + 2);
                    String phone = storeListStringArray.get(idx * 3 + 3);
                    try{
                        distance = Integer.valueOf(storeListStringArray.get(idx * 3 + 1));
                    }
                    catch(NumberFormatException ex){
                        ex.printStackTrace();
                        distance = 0;
                    }
                    itemData data = new itemData(distance, name, phone);

                    boolean skipFlag = false;
                    for(itemData _d:itemDataList)
                    {
                        if(_d.name.equals(name) || _d.phone.equals(phone))
                        {
                            skipFlag = true;
                            //itemDataList.remove(_d);
                            break;
                        }
                    }
                    if(!skipFlag)
                        itemDataList.add(data);
//                    tempList.add(data);
                }

//                itemDataList.addAll(tempList);
            }
        }

        Collections.sort(itemDataList);

        dataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        recyclerView.removeAllViews();
    }

    public class itemData implements Comparable<itemData> {
        int distance;
        String name;
        String phone;

        ///getter setter 만들기
        public int getDistance() {
            return distance;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        //constructor 만들기
        public itemData(int distance,String name, String phone) {
            this.distance = distance;
            if(name == "")
                this.name = "-";
            else
                this.name = name;
            if(phone == "")
                this.phone = "-";
            else
                this.phone = phone;
            Log.d("ITEM", "! " + distance + "," + name + "," + phone);
        }

        @Override
        public int compareTo(itemData data) {
            if (data.distance < distance) {
                return 1;
            } else if (data.distance > distance) {
                return -1;
            }
            return 0;
        }
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.AdapterViewholder> {

        private List<itemData> itemDataList;

        public Adapter(List<itemData> arrayList) { //Main_class와 연동하기 위한 adapter 파라미터
            this.itemDataList = arrayList;
        }

        @NonNull
        @Override
        public AdapterViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_beer_store_list_item,parent,false);
            AdapterViewholder holder = new AdapterViewholder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterViewholder holder, int position) {
            holder.distance.setText(String.valueOf(itemDataList.get(position).getDistance()));
            holder.name.setText(itemDataList.get(position).getName());
            holder.phone.setText(itemDataList.get(position).getPhone());
        }

        @Override
        public int getItemCount() {
            return (null != itemDataList ? itemDataList.size() :0);
        }

        public class AdapterViewholder extends RecyclerView.ViewHolder {
            TextView distance, name , phone;
            public AdapterViewholder(@NonNull View itemView) {
                super(itemView);

                distance = (TextView) itemView.findViewById(R.id.beerStoreDistance);
                name = (TextView) itemView.findViewById(R.id.beerStoreName);
                phone = (TextView) itemView.findViewById(R.id.beerStorePhone);
            }
        }
    }
}
