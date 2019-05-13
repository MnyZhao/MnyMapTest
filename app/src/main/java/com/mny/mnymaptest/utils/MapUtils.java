package com.mny.mnymaptest.utils;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

import java.util.ArrayList;

/**
 * Crate by E470PD on 2018/11/19
 */
public class MapUtils {
    private static MapUtils instance = null;

    private MapUtils() {

    }

    public static MapUtils getInstance() {
        if (instance == null) {
            instance = new MapUtils();
        }
        return instance;
    }

    /**
     * 移动到视图中心
     * LatLonPoint
     *
     * @param aMap         地图对象
     * @param centerLatLng 中心点
     * @param zoomLevel    缩放级别  地图缩放变量 地图的缩放级别一共分为 17 级，从 3 到 19。数字越大，展示的图面信息越精细。
     */
    public void moveCenter(AMap aMap, LatLonPoint centerLatLng, float zoomLevel) {
        if (centerLatLng != null) {
            aMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(new LatLng(centerLatLng.getLatitude(), centerLatLng.getLongitude()), zoomLevel));
        }
    }

    /**
     * 移动到视图中心
     * LatLng
     *
     * @param aMap         地图对象
     * @param centerLatLng 中心点
     * @param zoomLevel    缩放级别  地图缩放变量 地图的缩放级别一共分为 17 级，从 3 到 19。数字越大，展示的图面信息越精细。
     */
    public void moveCenter(AMap aMap, LatLng centerLatLng, float zoomLevel) {
        if (centerLatLng != null) {
            aMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(centerLatLng, zoomLevel));
        }
    }

    /**
     * 根据km数获取合适的缩放比例
     *
     * @param fKm
     * @return
     */
    public float getZoom(float fKm) {
        if (0.01 <= fKm && fKm < 0.025) {
            return 19;
        } else if (0.025 <= fKm && fKm < 0.05) {
            return 18;
        } else if (0.05 <= fKm && fKm < 0.1) {
            return 17;
        } else if (0.1 <= fKm && fKm < 0.2) {
            return 16;
        } else if (0.2 <= fKm && fKm < 0.5) {
            return 15;
        } else if (0.5 <= fKm && fKm < 1) {
            return 14;
        } else if (1 <= fKm && fKm < 2) {
            return 13;
        } else if (2 <= fKm && fKm < 5) {
            return 12;
        } else if (5 <= fKm && fKm < 10) {
            return 11;
        } else if (10 <= fKm && fKm < 20) {
            return 10;
        } else if (20 <= fKm && fKm < 30) {
            return 9;
        } else if (30 <= fKm && fKm < 50) {
            return 8;
        } else if (50 <= fKm && fKm < 100) {
            return 7;
        } else if (100 <= fKm && fKm < 200) {
            return 6;
        } else if (200 <= fKm && fKm < 500) {
            return 5;
        } else if (500 <= fKm && fKm < 1000) {
            return 4;
        } else if (1000 <= fKm) {
            return 3;
        }
        return 13;
    }

    /**
     * 将String数组坐标集合转换成List坐标集合 当前分隔符为; 根据需求自己写
     *
     * @param latLngs
     * @return
     */
    public ArrayList<LatLng> stringLatLngToList(String[] latLngs) {
        ArrayList<LatLng> lngArrayList = new ArrayList<>();
        for (String str : latLngs) {
            String[] lat = str.split(";");
            for (String latstr : lat) {
                String[] lats = latstr.split(",");
                lngArrayList.add(new LatLng(Double
                        .parseDouble(lats[1]), Double
                        .parseDouble(lats[0])));
            }
        }
        return lngArrayList;
    }
}
