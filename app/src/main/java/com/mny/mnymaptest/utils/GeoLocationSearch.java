package com.mny.mnymaptest.utils;

import android.content.Context;
import android.util.Log;

import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.mny.mnymaptest.GeoFenceActivity;

/**
 * Crate by E470PD on 2019/4/15
 */
public class GeoLocationSearch {
    private static GeoLocationSearch instance;

    private GeoLocationSearch() {
    }

    ;

    public static GeoLocationSearch getInstance() {
        if (instance == null) {
            instance = new GeoLocationSearch();
        }
        return instance;
    }

    public void searchLocation(Context context, RegeocodeQuery query, final searchLocationListener listener) {
        GeocodeSearch geocodeSearch = new GeocodeSearch(context);
        //查询监听
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int resuleId) {
                if (resuleId == 1000) {
                    /*msg = "国家：" + ads.getCountry() +
                            "\n 城市：" + ads.getCity() +
                            "\n 所在省或者直辖市：" + ads.getProvince() +
                            "\n 所在社区名称：" + ads.getNeighborhood() +
                            "\n 乡镇街道编码：" + ads.getTowncode() +
                            "\n 乡镇名称:" + ads.getTownship() +
                            "\n 建筑物名称:" + ads.getBuilding() +
                            "\n AOI（面状数据）的数据:" + ads.getAois().get(0).getAoiName() +
                            "\n 返回商圈对象列表:" + ads.getBusinessAreas().get(0).getName() +
                            "\n 逆地理编码返回的格式化地址:" + ads.getFormatAddress();
                            */
                    RegeocodeAddress ads = regeocodeResult.getRegeocodeAddress();
                    if (listener != null) {
                        listener.searchLocation(regeocodeResult);
                    }
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                geocodeResult.getGeocodeAddressList();
            }
        });
        geocodeSearch.getFromLocationAsyn(query);
    }

    public interface searchLocationListener {
        void searchLocation(RegeocodeResult regeocodeResult);
    }
}
