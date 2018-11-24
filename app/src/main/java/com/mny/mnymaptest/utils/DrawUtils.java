package com.mny.mnymaptest.utils;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
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
     *                     DOTTEDLINE_TYPE_CIRCLE虚线类型：圆形，值为1；
     *                     DOTTEDLINE_TYPE_SQUARE虚线类型：方形，值为0；
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

    public void clear() {
        aMap.clear();
    }
}
