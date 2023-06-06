package com.sese.showmethebeer;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;


import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

public class BeerStoreManagerActivity extends AppCompatActivity implements  MapView.CurrentLocationEventListener, MapView.POIItemEventListener{

    ViewGroup mMapViewContainer;
    private MapView mMapView;

    static boolean firstMapShow;
    static boolean initMapShow = true;

    private static final int GPS_ENABLE_REQUEST_CODE=2001;
    private static final int PERMISSIONS_REQUEST_CODE=100;
    String[]REQUIRED_PERMISSIONS={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
//    private final String TAG = this.getClass().getSimpleName().trim().substring(0,22);
    List<BeerStoreManager.StoreData>storeDataList = new ArrayList<BeerStoreManager.StoreData>();
    View view;
    private static final int MESSAGE_ID_SHOW_CUSTOM_DIALOG = 0;

    final Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg){
            int messageId = msg.what;
            switch (messageId) {
                case MESSAGE_ID_SHOW_CUSTOM_DIALOG:
                    showCustomDialog((MapPOIItem)(msg.obj));
                    break;
            }
        }
    };

    List<MapPoint> mapPointList=new ArrayList<MapPoint>();

    /*class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
            Log.i(Constants.TAG, "CustomCalloutBalloonAdapter::" + mCalloutBalloon);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            Log.i(Constants.TAG, "CustomCalloutBalloonAdapter::getCalloutBalloon : " + poiItem);

            BeerStoreManager.StoreData storeData  = (BeerStoreManager.StoreData)poiItem.getUserObject();

            ((TextView) mCalloutBalloon.findViewById(R.id.custom_callout_balloon_name)).setText(storeData.place_name);

            if (storeData.category_name != null && storeData.category_name.length() > 0) {
                ((TextView) mCalloutBalloon.findViewById(R.id.categoryNameTextView)).setText(storeData.category_name);
            } else {
                mCalloutBalloon.findViewById(R.id.categoryNameRow).setVisibility(View.GONE);
            }

            if (storeData.phone != null && storeData.phone.length() > 0) {
                ((TextView) mCalloutBalloon.findViewById(R.id.phoneNumberTextView)).setText(storeData.phone);
            } else {
                mCalloutBalloon.findViewById(R.id.phoneNumberRow).setVisibility(View.GONE);
            }

            if (storeData.road_address_name != null && storeData.road_address_name.length() > 0) {
                ((TextView) mCalloutBalloon.findViewById(R.id.addressTextView)).setText(storeData.road_address_name);
            } else {
                mCalloutBalloon.findViewById(R.id.addressRow).setVisibility(View.GONE);
            }

            if (storeData.place_url != null && storeData.place_url.length() > 0) {
                TextView linkTextView = ((TextView) mCalloutBalloon.findViewById(R.id.linkTextView));
                linkTextView.setText(storeData.place_url);

                linkTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(storeData.place_url));
                        startActivity(myIntent);
                    }
                });
            } else {
                mCalloutBalloon.findViewById(R.id.linkRow).setVisibility(View.GONE);

            }
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            Log.i(Constants.TAG, "CustomCalloutBalloonAdapter::getPressedCalloutBalloon : " + poiItem);
            return null;
        }
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("BeerStoreManagerActivity");

        setContentView(R.layout.activity_beer_store_manager_act);
        getSupportActionBar().setTitle("주변 찾기");
        mMapView = new MapView(this);
        mMapViewContainer = findViewById(R.id.map_mv_mapcontainer);

        mMapView.setZoomLevel(2, true);

        mMapView.setCurrentLocationEventListener(this);
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
        //mMapView.setPOIItemEventListener(this);

        firstMapShow = true;
        mMapViewContainer.addView(mMapView);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isPermissionGranted()){
            showDialogForLocationServiceSetting();}
        else {
            checkRunTimePermission();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        System.out.println("onPOIItemSelected -- send Handler");
        Message message = Message.obtain();
        message.what = MESSAGE_ID_SHOW_CUSTOM_DIALOG;
        message.obj = mapPOIItem;
        handler.sendMessage(message);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        System.out.println("onCalloutBalloonOfPOIItemTouched");


    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        System.out.println("onCalloutBalloonOfPOIItemTouched -->> long");
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
        System.out.println("onDraggablePOIItemMoved");

    }

   private void showCustomDialog(MapPOIItem poiItem) {
        StoreManagerDialog octDialog = new StoreManagerDialog(BeerStoreManagerActivity.this, poiItem, new StoreManagerDialogClickListener() {
            @Override
            public void onAddressClick() {
            }

            @Override
            public void onUrlClicked() {
            }
        });
        octDialog.setCanceledOnTouchOutside(true);
        octDialog.setCancelable(true);
        octDialog.getWindow().setGravity(Gravity.CENTER);
        //octDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        octDialog.show();
    }
    MapPoint lastMapPoint;

    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        //Log.i(TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
        MapPoint currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);

        // 현재 좌표 저장
        double mCurrentLat = mapPointGeo.latitude;
        double mCurrentLng = mapPointGeo.longitude;

        System.out.println("firstMapShow:" + firstMapShow + ", initMapShow: " + initMapShow + " , " + lastMapPoint);
        //이 좌표로 지도 중심 이동
        if (firstMapShow) {
            if (initMapShow) {
                mapPointList.add(currentMapPoint);
                mapView.setMapCenterPoint(currentMapPoint, true);
            } else
                if (lastMapPoint == null) {
                    mapPointList.add(currentMapPoint);
                    mapView.setMapCenterPoint(currentMapPoint, true);
                } else {
                    mapView.setMapCenterPoint(lastMapPoint, true);
                }

                List<BeerStoreManager.StoreData> tempList = new ArrayList<BeerStoreManager.StoreData>();

                try {
                    tempList.addAll(new BeerStoreManagerAsyncTask(mCurrentLat, mCurrentLng, 20000).execute().get());
                    tempList.removeAll(storeDataList);
                    storeDataList.addAll(tempList);
                    int idx = 0;
                    for (BeerStoreManager.StoreData storeData : storeDataList) {
                        MapMarker(mapView, storeData);
                        idx = idx + 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            lastMapPoint = mapView.getMapCenterPoint();

            MapPoint.GeoCoordinate centerPt = lastMapPoint.getMapPointGeoCoord();
            mCurrentLat = centerPt.latitude;
            mCurrentLng = centerPt.longitude;

            double distance = getDistance(mapPointGeo.latitude, mapPointGeo.longitude, centerPt.latitude, centerPt.longitude);

            if (distance < 1500) {
                boolean addFlag = true;

                for (MapPoint mp : mapPointList) {
                    if (getDistance(mp.getMapPointGeoCoord().latitude, mp.getMapPointGeoCoord().longitude, centerPt.latitude, centerPt.longitude) < 300)
                        addFlag = false;
                }

                if (addFlag) {
                    mapPointList.add(lastMapPoint);

                    List<BeerStoreManager.StoreData> tempList = new ArrayList<BeerStoreManager.StoreData>();

                    int sizeOfPoolOri, sizeOfPoolFin, sizeOfTemp, sizeOfSame;

                    try {
                        sizeOfPoolOri = storeDataList.size();

                        tempList.addAll(new BeerStoreManagerAsyncTask(mCurrentLat, mCurrentLng, 20000).execute().get());

                        sizeOfTemp = tempList.size();

                        tempList.removeAll(storeDataList);

                        sizeOfSame = sizeOfTemp - tempList.size();

                        storeDataList.addAll(tempList);

                        sizeOfPoolFin = storeDataList.size();

                        int idx = 0;
                        for (BeerStoreManager.StoreData storeData : tempList) {
                            //Log.d(TAG, "storeData => " + storeData.logString());
                            MapMarker(mapView, storeData);
                            idx = idx + 1;
                        }
                    } catch (Exception e) {
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

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
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
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

            final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
            if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("정상적인 동작을 위해 위치 정보 활성화가 필요합니다. 위치 정보를 활성화 시키겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(getApplicationContext(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BeerStoreManagerActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해 위치 서비스가 필요합니다.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
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
                    if (result.getResultCode() == -1) {
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
            final int result = ContextCompat.checkSelfPermission(getApplicationContext(), permission);

            if (PackageManager.PERMISSION_GRANTED != result) {
                return false;
            }
        }
        return true;
    }

    public void MapMarker(MapView mapView, BeerStoreManager.StoreData storeData) {
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(storeData.lat, storeData.lng);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(storeData.place_name);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setUserObject(storeData);
        mapView.addPOIItem( marker );
        //mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        mapView.setPOIItemEventListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        ArrayList<String> tStr = new ArrayList<String>();
        tStr.add(String.valueOf(storeDataList.size()));
        for (BeerStoreManager.StoreData storeData : storeDataList) {
            tStr.add(String.valueOf(storeData.distance));
            tStr.add(storeData.place_name);
            tStr.add(storeData.phone);
        }

        Bundle result = new Bundle();
        result.putStringArrayList("data", tStr);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}