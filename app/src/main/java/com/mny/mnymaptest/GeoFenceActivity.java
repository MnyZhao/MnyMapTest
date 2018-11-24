package com.mny.mnymaptest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.fence.GeoFence;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polygon;
import com.mny.mnymaptest.geofence.AmapGeoFences;
import com.mny.mnymaptest.utils.DrawUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 先添加围栏  成功后添加 矩形 绘制完矩形之后清除围栏以及矩形再次按照新坐标添加围栏成功后在添加矩形
 */
public class GeoFenceActivity extends AppCompatActivity {
    public String TAG = GeoFenceActivity.class.getName();
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
     * 添加围栏
     */
    private Button mBtnAddGeo;
    /**
     * 移除所有围栏
     */
    private Button mBtnRemoveAll;
    /**
     * 移除指定围栏
     */
    private Button mBtnRemove;
    DrawUtils drawUtils;

    private ArrayList<Marker> mMarkerList;
    AmapGeoFences amapGeoFences;
    //    拖拽
    private boolean isDrag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence);
        initView();
        setUpMapIfNeeded();
        /*声明帮助类*/
        drawUtils = new DrawUtils(mAMap);
        amapGeoFences = new AmapGeoFences(this, mAMap, geoListenger);
        initLatLngData1();
    }

    /*116.507805,39.898349
        mLatLnglist1.add(new LatLng(39.895244, 116.508027));
        116.512654,39.898513
        mLatLnglist1.add(new LatLng(39.895935, 116.513005));
        116.513813,39.891961
        mLatLnglist1.add(new LatLng(39.893762, 116.513091));
        116.505273,39.892027
        mLatLnglist1.add(new LatLng(39.893597, 116.508027));*/
    List<LatLng> mLatLnglist1;

    private List<LatLng> initLatLngData1() {
        mLatLnglist1 = new ArrayList<>();
        mLatLnglist1.add(new LatLng(39.898349, 116.507805));
        mLatLnglist1.add(new LatLng(39.898513, 116.512654));
        mLatLnglist1.add(new LatLng(39.891961, 116.513813));
        mLatLnglist1.add(new LatLng(39.892027, 116.505273));
        drawUtils.addCircle(false,0,
                new LatLng(39.898349, 116.507805),
                1000f,
                getResources().getColor(R.color.geofence_stork_color),
                getResources().getColor(R.color.geofence_fill_color),
                1f);
        return mLatLnglist1;
    }

    private List<DPoint> initDpoint(List<LatLng> latlngs) {
        List<DPoint> dPoints = new ArrayList<>();
        for (int i = 0; i < latlngs.size(); i++) {
            DPoint point = new DPoint();
            point.setLatitude(latlngs.get(i).latitude);
            point.setLongitude(latlngs.get(i).longitude);
            dPoints.add(point);
        }
        return dPoints;
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
                uiSettings.setRotateGesturesEnabled(false);//禁止旋转手势
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
            isDrag = true;
            amapGeoFences.removeGenFecneAll();
            // 从地图上删除所有的overlay（marker，circle，polyline 等对象）
            mAMap.clear();
            for (int i = 0; i < mMarkerList.size(); i++) {
                if (marker.getTitle().equals(mMarkerList.get(i).getTitle())) {
                    mLatLnglist1.set(i, marker.getPosition());
                }
            }
            //移动成功之后 要先添加围栏
            amapGeoFences.addPolygonFence(initDpoint(mLatLnglist1), "id");
          /*  drawUtils.drawPolygon(5, Color.parseColor("#050505"), Color.parseColor("#55FBEFDD"), mLatLnglist1);
            mMarkerList = drawUtils.addMarker(true, true, mLatLnglist1);
            // 删除当前marker并销毁Marker的图片等资源
            marker.destroy();*/
            marker.destroy();
            Log.e(TAG, "onMarkerDragEnd: " + mMarkerList.size() + isDrag);
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
        mBtnAddGeo = findViewById(R.id.btn_add_geo);
        mBtnAddGeo.setOnClickListener(listener);
        mBtnRemoveAll = (Button) findViewById(R.id.btn_clear_allgeo);
        mBtnRemoveAll.setOnClickListener(listener);
        mBtnRemove = (Button) findViewById(R.id.btn_clear_geo);
        mBtnRemove.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                default:
                    break;
                case R.id.btn_add_geo:
                    /*添加围栏*/
                    amapGeoFences.addPolygonFence(initDpoint(initLatLngData1()), "id");
                    break;
                case R.id.btn_clear_allgeo:
                    drawUtils.clear();
                    amapGeoFences.removeGenFecneAll();
                    break;
                case R.id.btn_clear_geo:
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
                mlocationClient = new AMapLocationClient(GeoFenceActivity.this);
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
    AmapGeoFences.OnGeoCallBackListener geoListenger = new AmapGeoFences.OnGeoCallBackListener() {
        @Override
        public void CallBack(List<GeoFence> geoFences, String customID) {
            Toast.makeText(GeoFenceActivity.this, "添加围栏成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "CallBack: 添加围栏成功");
            if (isDrag) {
                Polygon polygon = drawUtils.addPolygon(5f,
                        Color.parseColor("#050505"),
                        Color.parseColor("#55FBEFDD"),
                        mLatLnglist1);
                Log.e(TAG, "PolygonID: " + polygon.getId());
                mMarkerList = drawUtils.addMarker(true, true, mLatLnglist1);
            } else {
                /*添加矩形*/
                drawUtils.addPolygon(5, Color.parseColor("#050505"),
                        Color.parseColor("#55FBEFDD"), initLatLngData1());
                mMarkerList = drawUtils.addMarker(true, true, initLatLngData1());

            }
        }

        @Override
        public void Error(int errorCode) {
            Toast.makeText(GeoFenceActivity.this, "添加围栏失败" + errorCode, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "CallBack: 添加围栏失败");
        }

        @Override
        public void GeoFence(GeoFence fence, String Msg) {
            Toast.makeText(GeoFenceActivity.this, Msg, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void GeoFenceLocationError(String msg, int locationErrorCode) {
            Toast.makeText(GeoFenceActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        amapGeoFences.removeGenAllUnRegester();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isDrag = false;
        amapGeoFences.removeGenAllUnRegester();
    }
}
