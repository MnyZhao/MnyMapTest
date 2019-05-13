package com.mny.mnymaptest.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.mny.mnymaptest.LocationActivity;
import com.mny.mnymaptest.R;

/**
 * Crate by E470PD on 2019/4/8
 */
public class LocationUtils implements LocationSource {
    private AMap mAMap;
    private Context context;
    //是否高精度定位 若是则不显示精度圈
    private boolean isHightAccuracy;
    //是否单次定位
    private boolean isOnceLocation;
    //位置发生改变的监听接口
    LocationSource.OnLocationChangedListener mListener;

    public LocationUtils(Context context) {
        this.context = context;
    }

    public LocationUtils(Context context, AMap mAMap) {
        this.context = context;
        this.mAMap = mAMap;
    }

    /**
     * @param isHightAccuracy 是否高精度定位 若是则不显示精度圈
     * @param isOnceLocation  是否单次定位 若locationStyle 设置多次定位则要设置成false
     * @param locationStyle   定位小蓝点样式
     */
    public void initLocation(boolean isHightAccuracy, boolean isOnceLocation, MyLocationStyle locationStyle) {
        //设置之后就与右上角定位按钮结合到一起
        mAMap.setLocationSource(this);//设置定位源 不设置则getMyLocation为空 也可以不设置 直接用start
        mAMap.setMyLocationStyle(locationStyle);
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        this.isHightAccuracy = isHightAccuracy;
        this.isOnceLocation = isOnceLocation;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        //激活定位源。
        mListener = onLocationChangedListener;
        startlocation(this.isHightAccuracy, this.isOnceLocation);
    }

    @Override
    public void deactivate() {
        //弃用地图
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    /**
     * 开始定位。
     */
    AMapLocationClient mLocationClient;
    AMapLocationClientOption mLocationOption;

    /**
     * @param isHightAccuracy 是否高精度定位 若是则不显示精度圈
     * @param isOnceLocation  是否单次定位
     */
    private void startlocation(boolean isHightAccuracy, boolean isOnceLocation) {

        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(context);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mLocationClient.setLocationListener(locationListener);
            if (isHightAccuracy) {
                // 设置为高精度定位模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            } //设置为单次定位
            mLocationOption.setOnceLocation(isOnceLocation);
            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        } else {
            mLocationClient.startLocation();
        }
    }

    //可以获取到位置信息 经纬度
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                LatLng mylocation = new LatLng(aMapLocation.getLatitude(),
                        aMapLocation.getLongitude());
                //动画平滑移动到位置点 不旋转
                mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 19));
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": "
                        + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    };

    /**
     * 弃用地图调用 一般再OnPause中调用
     */
    public void deleteMap() {
        deactivate();
    }

    /**
     * 销毁LocationClient 再ondestory中调用
     */
    public void destoryLocationClient() {
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
        }
    }
}
