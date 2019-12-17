package com.mny.mnymaptest.utils;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Crate by E470PD on 2018/9/20
 */
public class DrawUtils {

    AMap aMap;

    public DrawUtils() {
    }

    public DrawUtils(AMap aMap) {
        this.aMap = aMap;
    }

    /**
     * @param markerIsVisible Marker是否可见
     * @param markerIsMove    Marker是否可拖拽
     * @param latLnglist      marker的做标集合
     */
    public ArrayList<Marker> addMarker(boolean markerIsVisible, boolean markerIsMove, List<LatLng> latLnglist) {
        ArrayList<MarkerOptions> options = new ArrayList<>();
        for (int i = 0; i < latLnglist.size(); i++) {
            // 在地图上添一组图片标记（marker）对象，并设置是否改变地图状态以至于所有的marker对象都在当前地图可视区域范围内显示
            MarkerOptions markerOptions = new MarkerOptions();
            // 设置Marker覆盖物的位置坐标。Marker经纬度坐标不能为Null，坐标无默认值
            markerOptions.position(latLnglist.get(i));
            // 设置Marker覆盖物是否可见
            markerOptions.visible(markerIsVisible);
            // 设置Marker覆盖物是否可拖拽
            markerOptions.draggable(markerIsMove);
            markerOptions.title(i + "");
            options.add(markerOptions);
        }
        // 在地图上添一组图片标记（marker）对象，并设置是否改变地图状态以至于所有的marker对象都在当前地图可视区域范围内显示
        return aMap.addMarkers(options, true);
    }

    /**
     * 多边形
     *
     * @param width       边框宽度
     * @param strokeColor 颜色不能传R.color.id  getResuse().getColor(R.color.id);
     * @param fillColor   颜色不能传R.color.id  getResuse().getColor(R.color.id);
     * @param latLnglist
     */
    public void addPolygons(float width, int strokeColor, int fillColor, List<LatLng> latLnglist) {

        // 声明 多边形参数对象
        PolygonOptions polygonOptions = new PolygonOptions();
        // 添加 多边形的每个顶点（顺序添加）
        /*for (LatLng latLng : latLnglist) {
            polygonOptions.add(latLng);
        }*/
        polygonOptions.addAll(latLnglist);//添加所有
        polygonOptions.strokeWidth(width); // 多边形的边框宽度
        polygonOptions.strokeColor(strokeColor); // 边框颜色
        polygonOptions.fillColor(fillColor);   // 多边形的填充色
        // 在地图上添加一个多边形（polygon）对象
        aMap.addPolygon(polygonOptions);
    }
    /**
     * 多边形
     *
     * @param width       边框宽度
     * @param strokeColor 颜色不能传R.color.id  getResuse().getColor(R.color.id);
     * @param fillColor   颜色不能传R.color.id  getResuse().getColor(R.color.id);
     * @param latLnglist
     */
    public Polygon addPolygon(float width, int strokeColor, int fillColor, List<LatLng> latLnglist) {

        // 声明 多边形参数对象
        PolygonOptions polygonOptions = new PolygonOptions();
        // 添加 多边形的每个顶点（顺序添加）
        /*for (LatLng latLng : latLnglist) {
            polygonOptions.add(latLng);
        }*/
        polygonOptions.addAll(latLnglist);//添加所有
        polygonOptions.strokeWidth(width); // 多边形的边框宽度
        polygonOptions.strokeColor(strokeColor); // 边框颜色
        polygonOptions.fillColor(fillColor);   // 多边形的填充色
        // 在地图上添加一个多边形（polygon）对象
        return aMap.addPolygon(polygonOptions);
    }
    /**
     * @param isStrokeType 是否开启虚线 开启后要选择虚线类型
     * @param strokeType   边框线类型 AMapPara.DOTTEDLINE_TYPE_DEFAULT:不绘制虚线（默认）
     *                     AMapPara.DOTTEDLINE_TYPE_SQUARE:虚线类型方形；
     *                     AMapPara.DOTTEDLINE_TYPE_CIRCLE：虚线类型圆形；
     * @param latLng       中心点坐标
     * @param radius       半径
     * @param strokeColor  边框颜色 颜色不能传R.color.id  要传getResuse().getColor(R.color.id);
     * @param fillColor    边框颜色 颜色不能传R.color.id  要传getResuse().getColor(R.color.id);
     * @param strokeWidth  边框宽度
     */
    public void addCircle(boolean isStrokeType, int strokeType, LatLng latLng, float radius, int strokeColor,
                          int fillColor, float strokeWidth) {
      /*  Circle circle = aMap.addCircle(new CircleOptions().
                center(latLng).
                radius(radius).
                fillColor(fillColor).
                strokeColor(strokeColor).
                setStrokeDottedLineType(strokeType).
                strokeWidth(strokeWidth));*/
        CircleOptions options = new CircleOptions();
        options.center(latLng);
        options.radius(radius);
        options.fillColor(fillColor);
        options.strokeColor(strokeColor);
        options.strokeWidth(strokeWidth);
        if (isStrokeType) {
            options.setStrokeDottedLineType(strokeType);
        }
        aMap.addCircle(options);
    }

    /**
     * @param color        边框颜色 颜色不能传R.color.id  要传getResuse().getColor(R.color.id);
     * @param width        线的宽度
     * @param latLnglist   坐标集合
     * @param isStrokeType 是否虚线
     * @param strokeType   线的类型
     *                     AMapPara.DOTTEDLINE_TYPE_CIRCLE虚线类型：圆形，值为1；
     *                     AMapPara.DOTTEDLINE_TYPE_SQUARE虚线类型：方形，值为0；
     */
    public void addPolyline(int color, float width, List<LatLng> latLnglist,
                            boolean isStrokeType, int strokeType) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(latLnglist);
        polylineOptions.width(width); // 多边形的边框宽度
        polylineOptions.color(color); // 边框颜色
        polylineOptions.setDottedLine(isStrokeType);
        if (isStrokeType) {
            polylineOptions.setDottedLineType(strokeType);
        }
        // 多边形的填充色
        aMap.addPolyline(polylineOptions);
    }


    /**
     * 某个点是否在区域内
     *
     * @param aMap       地图元素
     * @param latLngList 区域坐标合集
     * @param latLng     需要判断的点
     * @return
     */
    public static boolean polygonCon(AMap aMap, List<LatLng> latLngList, LatLng latLng) {
        PolygonOptions options = new PolygonOptions();
        for (LatLng i : latLngList) {
            options.add(i);
        }
        options.visible(false); //设置区域是否显示
        Polygon polygon = aMap.addPolygon(options);
        boolean contains = polygon.contains(latLng);
        polygon.remove();
        return contains;
    }


    /**
     * 某个点是否在区域内无map的情况下
     *
     * @param
     * @param
     * @param latLng 需要判断的点
     * @return
     */
    public static boolean isMapPolygonContainsPoint(Context context, LatLng latLng, List<LatLng> latLngs) {
        MapView mapView = new MapView(context);
        AMap aMap = mapView.getMap();
        PolygonOptions options = new PolygonOptions();
        for (LatLng i : latLngs) {
            options.add(i);
        }
        options.visible(false); //设置区域是否显示
        Polygon polygon = aMap.addPolygon(options);
        boolean contains = polygon.contains(latLng);
        polygon.remove();
        //mapView.onDestroy();
        return contains;
    }

    /**
     * 若要以多边形为中心 则要将地图	MyLocationStyle.myLocationType设置为
     *                              LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER
     *                              LOCATION_TYPE_FOLLOW_NO_CENTER
     *                              LOCATION_TYPE_MAP_ROTATE_NO_CENTER
     *                              同时去掉所有的缩放->moveCamera
     * @param width       边框宽度
     * @param strokeColor 颜色不能传R.color.id  getResuse().getColor(R.color.id);
     * @param fillColor   颜色不能传R.color.id  getResuse().getColor(R.color.id);
     * @param latLnglist  坐标点区域
     * @param padding     设置经纬度范围和mapView边缘的空隙，单位像素。这个值适用于区域的四个边。
     */
    public void addRectPoly(float width, int strokeColor, int fillColor, List<LatLng> latLnglist, int padding) {
        // 声明 多边形参数对象
        PolygonOptions polygonOptions = new PolygonOptions();
        //get bundle 添加 多边形的每个顶点（顺序添加）
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (LatLng latLng : latLnglist) {
            b.include(latLng);
        }
        polygonOptions.addAll(latLnglist);//添加所有
        polygonOptions.strokeWidth(width); // 多边形的边框宽度
        polygonOptions.strokeColor(strokeColor); // 边框颜色
        polygonOptions.fillColor(fillColor);   // 多边形的填充色
        // 在地图上添加一个多边形（polygon）对象
        aMap.addPolygon(polygonOptions);
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(b.build(), padding));
    }

    public void clear() {
        aMap.clear();
    }

}
