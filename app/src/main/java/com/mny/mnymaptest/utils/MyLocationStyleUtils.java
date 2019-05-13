package com.mny.mnymaptest.utils;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.mny.mnymaptest.R;

/**
 * Crate by E470PD on 2018/9/19
 * 生成定位样式
 */
public class MyLocationStyleUtils {
    /**
     * 定位样式
     *
     * @param time             连续模式下定位时间间隔 只有在连续定位模式下有效 随便写个2000 ms
     * @param type             设置定位模式 一般设置定位一次 视角跟随
     *                         myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
     *                         myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
     *                         myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
     *                         myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
     *                         myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
     *                         //以下三种模式从5.1.0版本开始提供
     *                         myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
     *                         myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
     *                         myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
     *                         或者通过AMap.调用
     * @param isShowMylocation 是否显示小蓝点 true 显示 false 不显示
     * @param id               要替换的图
     *                         可以增加设置 颜色不能直接传id 通过getResuse().getColor();
     *                         locationStyle.radiusFillColor(R.color.colorPrimaryDark);//设置填充色
     *                         locationStyle.strokeWidth(1);//设置边框宽度
     *                         locationStyle.strokeColor(R.color.colorAccent);//边框颜色
     * @return
     */
    public MyLocationStyle getStyleType(int id, long time, int type, boolean isShowMylocation,int fillColor,int storkWidth,int storkColor) {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(id));
        myLocationStyle.interval(time); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(type);//设置定位模式 默认跟随
        //用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        myLocationStyle.showMyLocation(isShowMylocation);
        myLocationStyle.radiusFillColor(fillColor);
        myLocationStyle.strokeColor(storkColor);
        myLocationStyle.strokeWidth(storkWidth);
        return myLocationStyle;
    }
}
