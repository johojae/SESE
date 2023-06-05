package com.sese.showmethebeer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;


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
public class BeerStoreManagerActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener {
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    String APIKey = "c902cfe962c6fe2b91572930b36db204";
    private MapView mapView;

    boolean firstMapShow;
    private final String TAG = this.getClass().getSimpleName().trim().substring(0,22);
    List<BeerStoreManager.StoreData> storeDataList = new ArrayList<BeerStoreManager.StoreData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_store_manager_frag1_map);

        mapView = new MapView(this);

        if (!isPermissionGranted()){
            showDialogForLocationServiceSetting();}
        else{
            checkRunTimePermission();
        }

        MapPoint currentMapPoint;
        try {
            BeerStoreManager3 beerStoreManager3 = new BeerStoreManager3(this);

            double latitude = beerStoreManager3.getLatitude();
            double longitude = beerStoreManager3.getLongitude();
            currentMapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
            mapView.setMapCenterPoint(currentMapPoint, true);
        }
        catch (Exception e){
            e.printStackTrace();
            currentMapPoint = mapView.getMapCenterPoint();
        }

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapView.setZoomLevel(2, true);
        mapViewContainer.addView(mapView);

        try {
            storeDataList.addAll(new BeerStoreManager2(currentMapPoint.getMapPointGeoCoord().latitude, currentMapPoint.getMapPointGeoCoord().longitude, 20000).execute().get());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        for (BeerStoreManager.StoreData storeData:storeDataList) {
            Log.d(TAG, "storeData => " + storeData.logString());
            MapMarker(mapView, storeData.place_name, Integer.toString(storeData.distance), storeData.lng, storeData.lat);
        }
    }

    // 현재 위치 업데이트 setCurrentLocationEventListener
    //@Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
        MapPoint currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);

        //이 좌표로 지도 중심 이동
        if(firstMapShow)
            mapView.setMapCenterPoint(currentMapPoint, true);

        firstMapShow = false;

        //전역변수로 현재 좌표 저장
        double mCurrentLat = mapPointGeo.latitude;
        double mCurrentLng = mapPointGeo.longitude;
        Log.d(TAG, "현재위치 => " + mCurrentLat + "  " + mCurrentLng);

        MapPoint.GeoCoordinate centerPt = mapView.getMapCenterPoint().getMapPointGeoCoord();
        mCurrentLat = centerPt.latitude;
        mCurrentLng = centerPt.longitude;
        Log.d(TAG, "지도위치 => " + mCurrentLat + "  " + mCurrentLng);

        String resultText = "[NULL]";

        try {
           // resultText = new BeerStoreManager2(mCurrentLat, mCurrentLng, 10000).execute().get();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Log.d(TAG, "resultText => " + resultText);

        try {
            storeDataList.addAll(new BeerStoreManager().makeStoreList(resultText));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        int idx = 0;
        for (BeerStoreManager.StoreData storeData:storeDataList) {
            Log.d(TAG, "storeData => " + storeData.logString());
                MapMarker(mapView, "MapMarker", "test", storeData.lng, storeData.lat);
            idx = idx + 1;
        }



    }

    //@Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    //@Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
        Log.i(TAG, "onCurrentLocationUpdateFailed");
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
    }

    //@Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
        Log.i(TAG, "onCurrentLocationUpdateCancelled");
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
    }

    void checkRunTimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(BeerStoreManagerActivity.this,Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED){
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(BeerStoreManagerActivity.this,REQUIRED_PERMISSIONS[0])){
                Toast.makeText(BeerStoreManagerActivity.this,"이 앱을 실행하려면 위치 접근 권한이 필요합니다.",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(BeerStoreManagerActivity.this,REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }else{
                ActivityCompat.requestPermissions(BeerStoreManagerActivity.this,REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }
        }
    }
    private void showDialogForLocationServiceSetting(){
        AlertDialog.Builder builder = new AlertDialog.Builder(BeerStoreManagerActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해 위치 서비스가 필요합니다.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivityForResult(callGPSSettingIntent,GPS_ENABLE_REQUEST_CODE);

                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activityResultLauncher.launch(callGPSSettingIntent);

//                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
//                startActivityIntent.launch(chooserIntent);
            }
        });
        builder.create().show();
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
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
            final int result = ContextCompat.checkSelfPermission(this, permission);

            if (PackageManager.PERMISSION_GRANTED != result) {
                return false;
            }
        }
        return true;
    }

    public void MapMarker(MapView mapView, String MakerName, String detail, double startX, double startY) {
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord( startY, startX );
        //mapView.setMapCenterPoint( mapPoint, true );
        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(MakerName+"("+detail+")"); // 마커 클릭 시 컨테이너에 담길 내용
        marker.setMapPoint( mapPoint );
        // 기본으로 제공하는 BluePin 마커 모양.
        marker.setMarkerType( MapPOIItem.MarkerType.RedPin );
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setSelectedMarkerType( MapPOIItem.MarkerType.BluePin );
        mapView.addPOIItem( marker );
    }
    public void CenterMarker(MapView mapView, double startX, double startY) {
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord( startY, startX );
        //mapView.setMapCenterPoint( mapPoint, true );
        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("내 위치"); // 마커 클릭 시 컨테이너에 담길 내용
        marker.setMapPoint( mapPoint );
        // 기본으로 제공하는 BluePin 마커 모양.
        marker.setMarkerType( MapPOIItem.MarkerType.YellowPin );
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setSelectedMarkerType( MapPOIItem.MarkerType.YellowPin );
        mapView.addPOIItem( marker );
    }
}