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
import android.widget.FrameLayout;
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
public class Fragment_Map extends Fragment implements MapView.CurrentLocationEventListener {
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//    private final String TAG = this.getClass().getSimpleName().trim().substring(0,22);
    List<BeerStoreManager.StoreData> storeDataList = new ArrayList<BeerStoreManager.StoreData>();
    Activity activity;
    List<MapView> mapViewList = new ArrayList<MapView>();;
    ViewGroup mapViewContainer;
    View view;

    List<MapPoint> mapPointList = new ArrayList<MapPoint>();

    public static Fragment_Map newInstance(int number) {
        Fragment_Map fp = new Fragment_Map();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fp.setArguments(bundle);
        initMapShow = true;
        fp.storeDataList.clear();
        fp.mapViewList.clear();
        fp.mapPointList.clear();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_beer_store_manager , container, false);

//        mapViewList = new ArrayList<MapView>();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop () {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState (@Nullable Bundle outState){
        super.onSaveInstanceState(outState);
    }

    static boolean firstMapShow;
    static boolean initMapShow;
    @Override
    public void onResume() {
        super.onResume();

        mapViewList.add(new MapView(activity));

        MapView mapView = mapViewList.get(0);

        if (!isPermissionGranted()){
            showDialogForLocationServiceSetting();}
        else{
            checkRunTimePermission();
        }

        mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapView.setZoomLevel(2, true);
        mapViewContainer.addView(mapView);

        mapView.setCurrentLocationEventListener(this);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

        firstMapShow = true;
    }

    MapPoint lastMapPoint;
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        //Log.i(TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
        MapPoint currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);

        // 현재 좌표 저장
        double mCurrentLat = mapPointGeo.latitude;
        double mCurrentLng = mapPointGeo.longitude;

        //이 좌표로 지도 중심 이동
        if(firstMapShow)
        {
            if(initMapShow) {
                mapPointList.add(currentMapPoint);
                mapView.setMapCenterPoint(currentMapPoint, true);
            }
            else
                mapView.setMapCenterPoint(lastMapPoint, true);

            List<BeerStoreManager.StoreData> tempList = new ArrayList<BeerStoreManager.StoreData>();

            try {
                tempList.addAll(new BeerStoreManager2(mCurrentLat, mCurrentLng, 20000).execute().get());
                tempList.removeAll(storeDataList);
                storeDataList.addAll(tempList);
                int idx = 0;
                for (BeerStoreManager.StoreData storeData:storeDataList) {
                    MapMarker(mapView, storeData.place_name, Integer.toString(storeData.distance), storeData.lng, storeData.lat);
                    idx = idx + 1;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        lastMapPoint = mapView.getMapCenterPoint();
        MapPoint.GeoCoordinate centerPt = lastMapPoint.getMapPointGeoCoord();
        mCurrentLat = centerPt.latitude;
        mCurrentLng = centerPt.longitude;
//        Log.d(TAG, "지도위치 => " + mCurrentLat + "  " + mCurrentLng);

        double distance = getDistance(mapPointGeo.latitude, mapPointGeo.longitude, centerPt.latitude, centerPt.longitude);

//        Log.d(TAG, "거리 => " + distance);

        if(distance < 1500)
        {
            boolean addFlag = true;

            for(MapPoint mp:mapPointList)
            {
                if(getDistance(mp.getMapPointGeoCoord().latitude, mp.getMapPointGeoCoord().longitude, centerPt.latitude, centerPt.longitude) < 300)
                    addFlag = false;
            }

            if(addFlag)
            {
                mapPointList.add(lastMapPoint);

                List<BeerStoreManager.StoreData> tempList = new ArrayList<BeerStoreManager.StoreData>();

                int sizeOfPoolOri, sizeOfPoolFin, sizeOfTemp, sizeOfSame;

                try {
                    sizeOfPoolOri = storeDataList.size();

                    tempList.addAll(new BeerStoreManager2(mCurrentLat, mCurrentLng, 20000).execute().get());

                    sizeOfTemp = tempList.size();

                    tempList.removeAll(storeDataList);

                    sizeOfSame = sizeOfTemp - tempList.size();

                    storeDataList.addAll(tempList);

                    sizeOfPoolFin = storeDataList.size();

                    int idx = 0;
                    for (BeerStoreManager.StoreData storeData:tempList) {
                        //Log.d(TAG, "storeData => " + storeData.logString());
                        MapMarker(mapView, storeData.place_name, Integer.toString(storeData.distance), storeData.lng, storeData.lat);
                        idx = idx + 1;
                    }
//                Log.d(TAG, "storeData add => Pool ori " + sizeOfPoolOri + ", Temp " + sizeOfTemp + ", Same " + sizeOfSame + ", Fin " + sizeOfPoolFin);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        firstMapShow = false;
        initMapShow = false;
    }

    public double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat/2)* Math.sin(dLat/2)+ Math.cos(Math.toRadians(lat1))* Math.cos(Math.toRadians(lat2))* Math.sin(dLon/2)* Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = 6371 * c * 1000;    // Distance in m
        return d;
    }

    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    //@Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
//        Log.i(TAG, "onCurrentLocationUpdateFailed");
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
    }

    //@Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
//        Log.i(TAG, "onCurrentLocationUpdateCancelled");
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
    }

    void checkRunTimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED){
            mapViewList.get(0).setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),REQUIRED_PERMISSIONS[0])){
                Toast.makeText(getActivity().getApplicationContext(),"이 앱을 실행하려면 위치 접근 권한이 필요합니다.",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(getActivity(),REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }else{
                ActivityCompat.requestPermissions(getActivity(),REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }
        }
    }
    private void showDialogForLocationServiceSetting(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
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
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == -1) {
                        Intent intent = result.getData();
                        Uri uri = intent.getData();
                    }
                }
            }
    );

    private boolean isPermissionGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (permission.equals(Manifest.permission.ACCESS_BACKGROUND_LOCATION) && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                continue;
            }
            final int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), permission);

            if (PackageManager.PERMISSION_GRANTED != result) {
                return false;
            }
        }
        return true;
    }

    public void MapMarker(MapView mapView, String MakerName, String detail, double startX, double startY) {
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord( startY, startX );
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(MakerName+"("+detail+")");
        marker.setMapPoint( mapPoint );
        marker.setMarkerType( MapPOIItem.MarkerType.RedPin );
        marker.setSelectedMarkerType( MapPOIItem.MarkerType.BluePin );
        mapView.addPOIItem( marker );
    }
    @Override
    public void onPause() {
        super.onPause();

        mapViewList.get(0).onPause();
        mapViewContainer.removeView(mapViewList.get(0));
        mapViewList.remove(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
