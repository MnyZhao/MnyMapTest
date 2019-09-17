package com.mny.mnymaptest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.share.ShareSearch;
import com.mny.mnymaptest.utils.GeoLocationSearch;

/**
 * 根据经纬度获取位置信息 逆地理编码
 */
public class MsgActivity extends AppCompatActivity {
    public String TAG = MsgActivity.class.getName();
    private Button mBtnGetMSG, mBtnShare;
    private TextView mTvShow;
    private EditText mEtLeft, mEtRight;
    //查询类
    private GeocodeSearch geocodeSearch;
    //查询对象
    RegeocodeQuery query;
    private String msg = null;
    LatLonPoint latLonPoint;
    LatLonPoint POI_POINT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        initView();
        geocodeSearch = new GeocodeSearch(this);

        double v1 = Double.parseDouble(mEtLeft.getText().toString());
        double v2 = Double.parseDouble(mEtRight.getText().toString());
        latLonPoint = new LatLonPoint(v1, v2);
        POI_POINT = new LatLonPoint(39.989646, 116.480864);
        /**
         * id - POI 的标识。
         * point - 该POI的位置。
         * title - 该POI的名称。
         * snippet - POI的地址。
         */

        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        query = new RegeocodeQuery(latLonPoint, 20, GeocodeSearch.AMAP);

        //查询监听
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int resuleId) {
                if (resuleId == 1000) {
                    Log.e(TAG, "onRegeocodeSearched: " + "查询成功");
                    RegeocodeAddress ads = regeocodeResult.getRegeocodeAddress();
                    msg = "国家：" + ads.getCountry() +
                            "\n 城市：" + ads.getCity() +
                            "\n 所在省或者直辖市：" + ads.getProvince() +
                            "\n 所在社区名称：" + ads.getNeighborhood() +
                            "\n 乡镇街道编码：" + ads.getTowncode() +
                            "\n 乡镇名称:" + ads.getTownship() +
                            "\n 建筑物名称:" + ads.getBuilding() +
                            "\n AOI（面状数据）的数据:" + ads.getAois().get(0).getAoiName() +
                            "\n 返回商圈对象列表:" + ads.getBusinessAreas().get(0).getName() +
                            "\n 逆地理编码返回的格式化地址:" + ads.getFormatAddress();
                    Log.e(TAG, "onRegeocodeSearched: " + msg);
                    mTvShow.setText(msg);
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                geocodeResult.getGeocodeAddressList();
            }
        });

    }

    private void skip(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    private void initView() {
        mTvShow = findViewById(R.id.tv_show);
        mBtnGetMSG = findViewById(R.id.btn_getmsg);
        mBtnShare = findViewById(R.id.btn_share);
        mBtnShare.setOnClickListener(listener);
        mBtnGetMSG.setOnClickListener(listener);
        mEtLeft = findViewById(R.id.et_left);
        mEtRight = findViewById(R.id.et_right);


    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_getmsg:
                    //搜索1 逆地理编码
                    geocodeSearch.getFromLocationAsyn(query);
                    //搜索2 获取格式化地址
                    GeoLocationSearch.getInstance().searchLocation(MsgActivity.this,
                            query, new GeoLocationSearch.searchLocationListener() {
                        @Override
                        public void searchLocation(RegeocodeResult regeocodeResult) {
                            regeocodeResult.getRegeocodeAddress().getFormatAddress();
                        }
                    });
                    break;

            }
        }
    };
}
