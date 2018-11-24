package com.mny.mnymaptest;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.AMapPara;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.mny.mnymaptest.utils.MarkerUtils;

public class ShowMarkerActivity extends AppCompatActivity {
    public String TAG = "ShowMarkerActivity";
    private MapView mapView;
    private AMap mAMap;
    LatLng markerPosition = new LatLng(39.993308, 116.473258);
    private MarkerUtils markerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        mapView = findViewById(R.id.mv_show);
        mapView.onCreate(savedInstanceState);
        markerUtils = new MarkerUtils();
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (mAMap == null) {
            mAMap = mapView.getMap();
            mAMap.setOnMarkerClickListener(listener);
            //移动到中心点
            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(markerPosition));
        }
        mAMap.setOnMarkerDragListener(dragListener);
        /*添加上浮效果marker*/
        markerUtils.addGrowMarker(mAMap, true, markerPosition, this, R.drawable.icon_openmap_mark, 1000);
        /*添加普通marker*/
        markerUtils.addMarker(mAMap, markerPosition, this, R.drawable.icon_openmap_mark);
        Toast.makeText(this.getApplicationContext(), "点击marker查看动画效果", Toast.LENGTH_LONG).show();
    }

    AMap.OnMarkerDragListener dragListener = new AMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {
            Log.e(TAG, "onMarkerDragStart:");
        }

        @Override
        public void onMarkerDrag(Marker marker) {
            Log.e(TAG, "onMarkerDrag: ");
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            Log.e(TAG, "onMarkerDragEnd: ");
        }
    };
    AMap.OnMarkerClickListener listener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Log.e("", "onMarkerClick:" + marker.getId());
            marker.startAnimation();
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }
}
