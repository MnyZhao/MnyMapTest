package com.mny.mnymaptest;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;

import java.util.List;

public class ShowMapActivity extends AppCompatActivity {
    String TAG="ShowMapActivity";
    private MapView mapView;
    private AMap aMap;
    private Button btnAdd, btnSub;
    /* 地图缩放变量 地图的缩放级别一共分为 17 级，从 3 到 19。数字越大，展示的图面信息越精细。*/
    int zoomi = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        initView();
        mapView = findViewById(R.id.mv_show);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(zoomi));
    }

    private void initView() {
        btnAdd = findViewById(R.id.btn_zoom_add);
        btnAdd.setOnClickListener(listener);
        btnSub = findViewById(R.id.btn_zoom_sub);
        btnSub.setOnClickListener(listener);
    }

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

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_zoom_add:
                    if (zoomi < 19) {
                        zoomi++;
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(zoomi));
                        Log.e(TAG, "onClick:+ "+zoomi );

                    } else {
                        Toast.makeText(ShowMapActivity.this, "已经缩放到最大级别", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_zoom_sub:
                    if (zoomi > 3) {
                        zoomi--;
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(zoomi));
                        Log.e(TAG, "onClick:- "+zoomi );
                    } else {
                        Toast.makeText(ShowMapActivity.this, "已经缩放到最小级别", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
