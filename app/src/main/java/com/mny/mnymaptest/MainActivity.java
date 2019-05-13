package com.mny.mnymaptest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mny.mnymaptest.path.android_path_record.RecoderPathActivity;
import com.mny.mnymaptest.pin.PinActivity;
import com.mny.mnymaptest.pin.PinTestActivity;
import com.mny.mnymaptest.poisearch.PoiKeywordSearchActivity;
import com.mny.mnymaptest.poisearch.PoiSearchActivity;
import com.mny.mnymaptest.testsearch.TestSearchActivity;


public class MainActivity extends AppCompatActivity {
    private Button mBtnGoMap, mBtnGoMsg, mBtnGoMarker, mBtnGoLocation, mBtnGoGenFence, mBtnSearch, mBtnGuide;
    private Button mBtnPolygon, mBtnGetUrl;
    private Button mBtnXz, mBtnPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void skip(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    private void initView() {
        mBtnGoMap = findViewById(R.id.btn_go_map);
        mBtnGoMap.setOnClickListener(listener);
        mBtnGoMarker = findViewById(R.id.btn_go_marker);
        mBtnGoMarker.setOnClickListener(listener);
        mBtnGoLocation = findViewById(R.id.btn_go_location);
        mBtnGoLocation.setOnClickListener(listener);
        mBtnGoGenFence = findViewById(R.id.btn_go_genfence);
        mBtnGoGenFence.setOnClickListener(listener);
        mBtnSearch = findViewById(R.id.btn_go_search);
        mBtnSearch.setOnClickListener(listener);
        mBtnGuide = findViewById(R.id.btn_go_guide);
        mBtnGuide.setOnClickListener(listener);
        mBtnGoMsg = findViewById(R.id.btn_go_msg);
        mBtnGoMsg.setOnClickListener(listener);
        mBtnPolygon = findViewById(R.id.btn_go_polygon);
        mBtnPolygon.setOnClickListener(listener);
        mBtnGetUrl = findViewById(R.id.btn_go_get_url);
        mBtnGetUrl.setOnClickListener(listener);
        mBtnXz = findViewById(R.id.btn_xz);
        mBtnXz.setOnClickListener(listener);
        mBtnPath = findViewById(R.id.btn_path);
        mBtnPath.setOnClickListener(listener);
        findViewById(R.id.btn_slideup).setOnClickListener(listener);
        findViewById(R.id.btn_go_poi_key_search).setOnClickListener(listener);
        findViewById(R.id.btn_go_poi_slide_search).setOnClickListener(listener);
        findViewById(R.id.btn_map_pin).setOnClickListener(listener);
        findViewById(R.id.btn_map_pin_test).setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_go_map:
                    skip(ShowMapActivity.class);
                    break;
                case R.id.btn_go_msg:
                    skip(MsgActivity.class);
                    break;
                case R.id.btn_go_polygon:
                    skip(DrawPolygonActivity.class);
                    break;
                case R.id.btn_go_marker:
                    skip(ShowMarkerActivity.class);
                    break;
                case R.id.btn_go_location:
                    skip(LocationActivity.class);
                    break;
                case R.id.btn_go_genfence:
                    skip(GeoFenceActivity.class);
                    break;

                case R.id.btn_go_search:
                    skip(TestSearchActivity.class);
                    break;
                case R.id.btn_go_poi_key_search:
                    skip(PoiKeywordSearchActivity.class);
                    break;
                case R.id.btn_go_poi_slide_search:
                    skip(PoiSearchActivity.class);
                    break;
                case R.id.btn_go_guide:
                    skip(AttendanceViewMap.class);
                    break;
                case R.id.btn_go_get_url:
                    skip(GetShareUrlActivity.class);
                    break;
                case R.id.btn_xz:
                    skip(XzActivity.class);
                    break;
                case R.id.btn_path:
                    skip(RecoderPathActivity.class);
                    break;
                case R.id.btn_slideup:
                    skip(DemoSlideuppanelActivity.class);
                    break;
                case R.id.btn_map_pin:
                    skip(PinActivity.class);
                    break;
                case R.id.btn_map_pin_test:
                    skip(PinTestActivity.class);
                    break;


            }
        }
    };
}
