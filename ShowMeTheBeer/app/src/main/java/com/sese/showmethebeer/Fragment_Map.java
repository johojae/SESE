package com.sese.showmethebeer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;


import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import net.daum.mf.map.api.MapPoint;
import android.net.Uri;
import net.daum.mf.map.api.MapPOIItem;
import java.util.ArrayList;
import java.util.List;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.os.Build;
public class Fragment_Map extends Fragment{
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
//    private static final int PERMISSIONS_REQUEST_CODE = 100;
//    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//    String APIKey = "c902cfe962c6fe2b91572930b36db204";
//    private final String TAG = this.getClass().getSimpleName().trim().substring(0,22);
//    List<BeerStoreManager.StoreData> storeDataList = new ArrayList<BeerStoreManager.StoreData>();
    Activity activity;
    MapView mapView;
    ViewGroup mapViewContainer;
    View view;
    public static Fragment_Map newInstance(int number) {
        Fragment_Map fp = new Fragment_Map();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fp.setArguments(bundle);
        return fp;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

//        if(getArguments() != null) {
//            int num = getArguments().getInt("number");
//        }
    }

    // map view

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_beer_store_manager , container, false);

        mapView = new MapView(activity);

//        if (!isPermissionGranted()){
//            showDialogForLocationServiceSetting();}
//        else{
//            checkRunTimePermission();
//        }

        MapPoint currentMapPoint;
        try {
            BeerStoreManager3 beerStoreManager3 = new BeerStoreManager3(this.getContext());

            double latitude = beerStoreManager3.getLatitude();
            double longitude = beerStoreManager3.getLongitude();
            currentMapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
            mapView.setMapCenterPoint(currentMapPoint, true);
        }
        catch (Exception e){
            e.printStackTrace();
            currentMapPoint = mapView.getMapCenterPoint();
        }

//        mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapView.setZoomLevel(2, true);
        mapViewContainer.addView(mapView);

//        try {
//            storeDataList.addAll(new BeerStoreManager2(currentMapPoint.getMapPointGeoCoord().latitude, currentMapPoint.getMapPointGeoCoord().longitude, 20000).execute().get());
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        for (BeerStoreManager.StoreData storeData:storeDataList) {
//            Log.d(TAG, "storeData => " + storeData.logString());
//            MapMarker(mapView, storeData.place_name, Integer.toString(storeData.distance), storeData.lng, storeData.lat);
//        }

        return view;
    }
//    void checkRunTimePermission() {
//        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this.getContext(),Manifest.permission.ACCESS_FINE_LOCATION);
//        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED){
//            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
//        }else{
//            if(ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),REQUIRED_PERMISSIONS[0])){
//                Toast.makeText(this.getContext(),"이 앱을 실행하려면 위치 접근 권한이 필요합니다.",Toast.LENGTH_LONG).show();
//                ActivityCompat.requestPermissions(this.getActivity(),REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
//            }else{
//                ActivityCompat.requestPermissions(this.getActivity(),REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
//            }
//        }
//    }
    private void showDialogForLocationServiceSetting(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해 위치 서비스가 필요합니다.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activityResultLauncher.launch(callGPSSettingIntent);
            }
        });
        builder.create().show();
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), null
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if(result.getResultCode() == RESULT_OK) {
//                        Intent intent = result.getData();
//                        Uri uri = intent.getData();
//                    }
//                }
//            }
    );

//    private boolean isPermissionGranted() {
//        for (String permission : REQUIRED_PERMISSIONS) {
//            if (permission.equals(Manifest.permission.ACCESS_BACKGROUND_LOCATION) && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//                continue;
//            }
//            final int result = ContextCompat.checkSelfPermission(this.getContext(), permission);
//
//            if (PackageManager.PERMISSION_GRANTED != result) {
//                return false;
//            }
//        }
//        return true;
//    }

//    public void MapMarker(MapView mapView, String MakerName, String detail, double startX, double startY) {
//        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord( startY, startX );
//        //mapView.setMapCenterPoint( mapPoint, true );
//        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
//        MapPOIItem marker = new MapPOIItem();
//        marker.setItemName(MakerName+"("+detail+")"); // 마커 클릭 시 컨테이너에 담길 내용
//        marker.setMapPoint( mapPoint );
//        // 기본으로 제공하는 BluePin 마커 모양.
//        marker.setMarkerType( MapPOIItem.MarkerType.RedPin );
//        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
//        marker.setSelectedMarkerType( MapPOIItem.MarkerType.BluePin );
//        mapView.addPOIItem( marker );
//    }
//    public void CenterMarker(MapView mapView, double startX, double startY) {
//        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord( startY, startX );
//        //mapView.setMapCenterPoint( mapPoint, true );
//        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
//        MapPOIItem marker = new MapPOIItem();
//        marker.setItemName("내 위치"); // 마커 클릭 시 컨테이너에 담길 내용
//        marker.setMapPoint( mapPoint );
//        // 기본으로 제공하는 BluePin 마커 모양.
//        marker.setMarkerType( MapPOIItem.MarkerType.YellowPin );
//        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
//        marker.setSelectedMarkerType( MapPOIItem.MarkerType.YellowPin );
//        mapView.addPOIItem( marker );
//    }


//    @Override
//    public void onReverseGeoCoderFoundAddress(@NonNull MapReverseGeoCoder mapReverseGeoCoder, String s) {
//        //Log.d(TAG, "MapFragment:onReverseGeoCoderFoundAddress()");
//        mapReverseGeoCoder.toString();
//        Toast.makeText(activity, "Reverse Geo-coding: " + s, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) { }
//
//    @Override
//    public void onCurrentLocationUpdate(MapView mapView, @NonNull MapPoint mapPoint, float v) {
//        //Log.d(TAG, "MapFragment:onCurrentLocationUpdate()");
//        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
//        //Log.d(TAG, String.format("(%f, %f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, v));
//    }
//
//    @Override
//    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) { }
//
//    @Override
//    public void onCurrentLocationUpdateFailed(MapView mapView) { }
//
//    @Override
//    public void onCurrentLocationUpdateCancelled(MapView mapView) { }

    @Override
    public void onStart() {
        super.onStart();
//        mapView.onStart();
    }

    @Override
    public void onStop () {
        super.onStop();
//        mapView.onStartTemporaryDetach()//.onStop();
    }

    @Override
    public void onSaveInstanceState (@Nullable Bundle outState){
        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();



        //mapView.onResume();
//      mapView.getHolder().setKeepScreenOn(true);
        //getSurface();
        //mapView.init();
//     mapView.setVisibility(View.VISIBLE);

//        mapView = new MapView(activity);
//
//        mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
//        mapViewContainer.addView(mapView);
    }

    @Override
    public void onPause() {
        super.onPause();


//      mapView.getHolder().setKeepScreenOn(false);
        //mapView.onPause();
        //mapView.onSurfaceDestroyed();
//        mapView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mapView.onLowMemory();
    }
}
