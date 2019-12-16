package com.mny.mnymaptest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.AMapPara;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.mny.mnymaptest.utils.DrawUtils;

import java.util.ArrayList;
import java.util.List;

public class DrawPolygonActivity extends AppCompatActivity {
    /**
     * 用于显示当前的位置
     * <p>
     * 示例中是为了显示当前的位置，在实际使用中，单独的地理围栏可以不使用定位接口
     * </p>
     */
    private AMapLocationClient mlocationClient;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;


    private AMap mAMap;
    /**
     * 显示多边形
     */
    private Button mBtnDrawPayload;
    /**
     * 显示marker
     */
    private Button mBtnDrawMarker;
    /**
     * 清空所有marker 以及图形
     */
    private Button mBtnCLear;
    DrawUtils drawUtils;

    private ArrayList<Marker> mMarkerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_polygon);
        initView();
        setUpMapIfNeeded();
        /*声明帮助类*/
        drawUtils = new DrawUtils(mAMap);
        initLatLngData1();
    }

    List<LatLng> mLatLnglist1;

    private List<LatLng> initLatLngData1() {
        mLatLnglist1 = new ArrayList<>();
        mLatLnglist1.add(new LatLng(39.895244, 116.508027));
        mLatLnglist1.add(new LatLng(39.895935, 116.513005));
        mLatLnglist1.add(new LatLng(39.893762, 116.513091));
        mLatLnglist1.add(new LatLng(39.893597, 116.508027));
        return mLatLnglist1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mAMap == null) {
            mAMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.paylod_map)).getMap();
            UiSettings uiSettings = mAMap.getUiSettings();
            if (uiSettings != null) {
                uiSettings.setRotateGesturesEnabled(false);
                uiSettings.setMyLocationButtonEnabled(true); // 设置默认定位按钮是否显示
            }
            mAMap.setOnMarkerDragListener(dragListener);
            mAMap.setOnMarkerClickListener(clickListener);
            mAMap.setLocationSource(locationSource);// 设置定位监听
            mAMap.setMyLocationStyle(
                    new MyLocationStyle().radiusFillColor(Color.argb(0, 0, 0, 0))
                            .strokeColor(Color.argb(0, 0, 0, 0)).myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.navi_map_gps_locked)));
            mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
            mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        }
    }

    AMap.OnMarkerDragListener dragListener = new AMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {

        }

        @Override
        public void onMarkerDrag(Marker marker) {

        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            // 从地图上删除所有的overlay（marker，circle，polyline 等对象）
            mAMap.clear();
            for (int i = 0; i < mMarkerList.size(); i++) {
                if (marker.getTitle().equals(mMarkerList.get(i).getTitle())) {
                    mLatLnglist1.set(i, marker.getPosition());
                }
            }
            drawUtils.addPolygons(5, Color.parseColor("#050505"), Color.parseColor("#55FBEFDD"), mLatLnglist1);
            mMarkerList = drawUtils.addMarker(true, true, mLatLnglist1);
            // 删除当前marker并销毁Marker的图片等资源
            marker.destroy();
        }
    };
    AMap.OnMarkerClickListener clickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Log.e("Marker", "onMarkerClick: Marker点击");
            return false;
        }
    };

    private void initView() {
        mBtnCLear = findViewById(R.id.btn_clear);
        mBtnCLear.setOnClickListener(listener);
        mBtnDrawPayload = (Button) findViewById(R.id.btn_draw_payload);
        mBtnDrawPayload.setOnClickListener(listener);
        mBtnDrawMarker = (Button) findViewById(R.id.btn_draw_marker);
        mBtnDrawMarker.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                default:
                    break;
                case R.id.btn_draw_payload:
//                    drawUtils.addPolygon(5, Color.parseColor("#050505"),
//                            Color.parseColor("#55FBEFDD"), initLatLngData1());
                    drawUtils.addRectPoly(5, Color.parseColor("#050505"),
                            Color.parseColor("#55FBEFDD"), initLatLngData1(),400);

                    break;
                case R.id.btn_draw_marker:
                    mMarkerList = drawUtils.addMarker(true, true, initLatLngData1());
                    break;
                case R.id.btn_clear:
                    drawUtils.clear();
                    break;
            }
        }
    };
    LocationSource locationSource = new LocationSource() {
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            mListener = onLocationChangedListener;
            if (mlocationClient == null) {
                mlocationClient = new AMapLocationClient(DrawPolygonActivity.this);
                mLocationOption = new AMapLocationClientOption();
                // 设置定位监听
                mlocationClient.setLocationListener(aMapLocationListener);
                // 设置为高精度定位模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                // 设置定位参数
                mlocationClient.setLocationOption(mLocationOption);
                mlocationClient.startLocation();
            }
        }

        @Override
        public void deactivate() {
            mListener = null;
            if (mlocationClient != null) {
                mlocationClient.stopLocation();
                mlocationClient.onDestroy();
            }
            mlocationClient = null;
        }
    };
    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                    mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                    LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    drawUtils.addCircle(false, AMapPara.DOTTEDLINE_TYPE_DEFAULT,latLng, 1000, Color.parseColor("#050505"), Color.parseColor("#55FBEFDD"), 5);
                } else {
                    String errText = "定位失败," + aMapLocation.getErrorCode() + ": "
                            + aMapLocation.getErrorInfo();
                    Log.e("AmapErr", errText);
                }
            } else {
                Log.e("AmapErr", "onLocationChanged: amplocation为空");
            }
        }
    };
}
