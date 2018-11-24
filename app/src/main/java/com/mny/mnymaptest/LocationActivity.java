package com.mny.mnymaptest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.mny.mnymaptest.utils.MarkerUtils;
import com.mny.mnymaptest.utils.MyLocationStyleUtils;
import com.mny.mnymaptest.utils.SensorEventHelper;

public class LocationActivity extends AppCompatActivity {
    public String TAG = "ShowMarkerActivity";
    private MapView mapView;
    private AMap mAMap;
    LatLng markerPosition = new LatLng(39.993308, 116.473258);
    LatLng position = new LatLng(160.992308, 126.413258);
    private MarkerUtils markerUtils;
    private boolean isInit2;
    //根据方向旋转
    SensorEventHelper mSensorHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        mapView = findViewById(R.id.mv_show);
        mapView.onCreate(savedInstanceState);
        markerUtils = new MarkerUtils();
        /*根据方向旋转*/
        mSensorHelper = new SensorEventHelper(this);
//        init();
        init2();
    }


    /**
     * 初始化AMap对象
     * 并定位1 通过setOnMyLocationChangeListener
     */
    private void init() {
        isInit2 = false;
        if (mAMap == null) {
            mAMap = mapView.getMap();
            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(markerPosition));
        }
        MyLocationStyleUtils locationStyleUtils = new MyLocationStyleUtils();
        MyLocationStyle locationStyle = locationStyleUtils.getStyleType(R.drawable.ic_position, 1000, MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER, true);
        locationStyle.radiusFillColor(R.color.colorPrimaryDark);//设置填充色
        locationStyle.strokeWidth(1);//设置边框宽度
        locationStyle.strokeColor(R.color.colorAccent);//边框颜色
        mAMap.setMyLocationStyle(locationStyle);
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                // 定位回调监听
                if (location != null) {
                   /* LatLng mylocation = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 19));*/
                    Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
                    Bundle bundle = location.getExtras();
                    if (bundle != null) {
                        int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                        String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                        // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                        int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
                        Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
                    } else {
                        Log.e("amap", "定位信息， bundle is null ");

                    }

                } else {
                    Log.e("amap", "定位失败");
                }
            }
        });
    }

    private void init2() {
        isInit2 = true;
        if (mAMap == null) {
            mAMap = mapView.getMap();
            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(markerPosition));
        }
        MyLocationStyleUtils locationStyleUtils = new MyLocationStyleUtils();
        //移动到中心
        MyLocationStyle locationStyle = locationStyleUtils.getStyleType(R.drawable.ic_position, 1000, MyLocationStyle.LOCATION_TYPE_LOCATE, false);
        //设置之后就与右上角定位按钮结合到一起
        mAMap.setLocationSource(locationSource);//设置定位源 不设置则getMyLocation为空 也可以不设置 直接用start
        mAMap.setMyLocationStyle(locationStyle);
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(LocationActivity.this,latLng.latitude+":"+latLng.longitude,Toast.LENGTH_SHORT).show();
            }
        });
        startlocation();
    }

    //位置发生改变的监听接口
    LocationSource.OnLocationChangedListener mListener;
    LocationSource locationSource = new LocationSource() {

        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            //onLocationChangedListener 位置改变监听的接口
            //激活定位源。
            mListener = onLocationChangedListener;
            Log.e(TAG, "activate: 定位源启动成功");
            startlocation();
        }

        @Override
        public void deactivate() {
            //停止定位。
            if (isInit2) {
                deleteMap();
            }
        }
    };
    /**
     * 开始定位。
     */
    AMapLocationClient mLocationClient;
    AMapLocationClientOption mLocationOption;

    private void startlocation() {

        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mLocationClient.setLocationListener(locationListener);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置为单次定位
            mLocationOption.setOnceLocation(true);
            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        } else {
            mLocationClient.startLocation();
        }
    }

    boolean mFirstFix;
    private Marker mLocMarker;
    //可以获取到位置信息 经纬度
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                LatLng mylocation = new LatLng(aMapLocation.getLatitude(),
                        aMapLocation.getLongitude());
                //动画平滑移动到位置点 不旋转
//                mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 19));
//                addLocationMarker(aMapLocation);

                if (!mFirstFix) {//添加点跟随方向旋转
                    mFirstFix = true;
                    mLocMarker = addMarker(mylocation);//添加定位图标
                    mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
                    mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 18));
                } else {

                    mLocMarker.setPosition(mylocation);
                    mAMap.moveCamera(CameraUpdateFactory.changeLatLng(mylocation));
                }
                Log.e(TAG, "onLocationChanged: " + aMapLocation.toString());
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": "
                        + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    };

    /**
     * 添加 marker图
     *
     * @param aMapLocation
     */
    private void addLocationMarker(AMapLocation aMapLocation) {
        LatLng mylocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        addMarker(mylocation);
    }

    private Marker addMarker(LatLng point) {
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.navi_map_gps_locked);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        Marker marker = mAMap.addMarker(new MarkerOptions().position(point).icon(des)
                .anchor(0.5f, 0.5f));
        return marker;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        /*根据方向旋转监听注册*/
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocMarker != null) {
            mLocMarker.destroy();
        }
        mapView.onDestroy();

    }

    private void deleteMap() {
        //弃用地图
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        if (isInit2) {
            deleteMap();
        }
        /*根据方向旋转监听解除注册*/
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        mFirstFix = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }
}
