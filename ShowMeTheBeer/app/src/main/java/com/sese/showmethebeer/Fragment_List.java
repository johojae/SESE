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
import java.util.List;

public class Fragment_List extends Fragment implements Serializable {
    Activity activity;
    View view;

    ArrayList<String> storeListStringArray = new ArrayList<String>();

    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public ArrayList<itemData> items = new ArrayList<>();
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
                // We use a String here, but any type that can be put in a Bundle is supported
//                storeListStringArray = new ArrayList<String>();
                storeListStringArray.addAll(bundle.getStringArrayList("data"));
//                byte[] byteArray = bundle.getByteArray("data");
                // Do something with the result...
//                ObjectInputStream ois = null;
//                ArrayList<BeerStoreManager.StoreData> list = new ArrayList<BeerStoreManager.StoreData>();
//                try {
//                    ois = new ObjectInputStream(new ByteArrayInputStream(byteArray));
//                    try {
//                        list.addAll((ArrayList<BeerStoreManager.StoreData>) ois.readObject());
//                        int sizo = list.size();
//                    } catch (ClassNotFoundException e) {
//                        throw new RuntimeException(e);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    } finally {
//                        ois.close();
//                    }
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                List<BeerStoreManager.StoreData> tempList = new <BeerStoreManager.StoreData> ArrayList(Arrays.asList(byteArray));



            }
        });
    }

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_beer_store_list , container, false);

//        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

//        List<itemData> itemDataList = new ArrayList<itemData>();
//        itemDataList.add(new itemData(10,"nam","pho"));
//
//        adapter = new Adapter(itemDataList);
//        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(mlayoutManager);
//        recyclerView.setAdapter(adapter);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL)); // 구분선

        List<itemData> itemDataList = new ArrayList<itemData>();
        Adapter dataAdapter = new Adapter(itemDataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        int sizeOfStringArray = storeListStringArray.size();
        if(sizeOfStringArray == 0)
        {
            itemData data = new itemData(0, "빈 리스트", "");
            itemDataList.add(data);
        }
        else {
            int sizeOfStoreList = Integer.valueOf(storeListStringArray.get(0));
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
                    itemDataList.add(data);
                }
            }
        }

        recyclerView.setAdapter(dataAdapter);


//        dataAdapter.notifyDataSetChanged();
//
//        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//
//        //recyclerViewList.add(new RecyclerView(activity));
//
////        RecyclerView recyclerView = recyclerViewList.get(0);
////
////        recyclerViewContainer = (ViewGroup) view.findViewById(R.id.recycler_view);
////        recyclerViewContainer.addView(recyclerView);
//
//        //RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//
//////        List<BeerStoreManager.StoreData> storeDataList = fragmentMap.storeDataList;
////        //fragmentMap.mapViewList.get(0).getPOIItems();
////
////        firstShow = true;
////
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL)); // 구분선
//
////        LinearLayoutManager linearLayoutManager;
////        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
////
////        recyclerView.setLayoutManager(linearLayoutManager); // 리사이클러뷰에 set 해준다 .
//
//        List<itemData> itemDataList = new ArrayList<itemData>();
//        Adapter dataAdapter = new Adapter(itemDataList);
//        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(mlayoutManager);
//        recyclerView.setAdapter(dataAdapter);
//
//        int sizeOfStringArray = storeListStringArray.size();
//        if(sizeOfStringArray == 0)
//        {
//            itemData data = new itemData(0, "빈 리스트", "");
//            itemDataList.add(data);
//        }
//        else {
//            int sizeOfStoreList = Integer.valueOf(storeListStringArray.get(0));
//            if(sizeOfStoreList == sizeOfStringArray - 1)
//            {
//                for(int idx = 1; idx < sizeOfStoreList; idx++)
//                {
//                    itemData data = new itemData(Integer.valueOf(storeListStringArray.get(idx)), storeListStringArray.get(idx + 1), storeListStringArray.get(idx + 2));
//                    itemDataList.add(data);
//                }
//            }
//
////        for(BeerStoreManager.StoreData storeData:storeDataList)
////        {
////            itemData data = new itemData(storeData.distance, storeData.place_name, storeData.phone);
////            itemDataList.add(data);
////        }
//
//            dataAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        recyclerView.removeAllViews();
    }

    public class itemData {
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
