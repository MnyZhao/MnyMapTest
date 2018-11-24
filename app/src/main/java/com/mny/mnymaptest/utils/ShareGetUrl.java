package com.mny.mnymaptest.utils;

/**
 * Crate by E470PD on 2018/10/26
 * 获取高德地图位置分享链接 拼接方式
 */
public class ShareGetUrl {
    private static ShareGetUrl instance;

    public static ShareGetUrl getInstance() {
        if (instance == null) {
            instance = new ShareGetUrl();
        }
        return instance;
    }

    private ShareGetUrl() {
    }

    /**
     * @param lon        必要 经度
     * @param lat        必要 维度
     * @param name       非必要 用户自定义显示名称
     * @param src        非必要 使用方来源信息 非必要
     * @param coordinate 非必要 坐标系参数coordinate=gaode,表示高德坐标(gcj02坐标)
     *                   coordinate=wgs84,表示wgs84坐标（GPS原始坐标）
     * @param callnative 非必要 是否尝试调起高德地图APP并在APP中查看，0表示不调起，1表示调起, 默认值为0
     * @return
     * @deprecated
     */
    public String getSpliceAllPath(double lon, double lat, String name, String src, String coordinate, String callnative) {
        String basePath = "https://uri.amap.com/marker?";
        String path = "position=" + lon + "," + lat;
        if (name != null) {
            path += ("&name=" + name);
        }
        if (src != null) {
            path += ("&src=" + src);
        }
        if (coordinate != null) {
            path += ("&coordinate=" + coordinate);
        }
        if (callnative != null) {
            path += ("&callnative" + callnative);
        }
        return basePath + path;
    }

    /**
     * @param lon        必要 经度
     * @param lat        必要 维度
     * @param name       非必要 用户自定义显示名称
     * @param src        非必要 使用方来源信息 非必要
     * @param coordinate 非必要 坐标系参数coordinate=gaode,表示高德坐标(gcj02坐标)
     *                   coordinate=wgs84,表示wgs84坐标（GPS原始坐标）
     * @param callnative 非必要 是否尝试调起高德地图APP并在APP中查看，0表示不调起，1表示调起, 默认值为0
     * @return
     */
    public String getSpliceAllPathBuffer(double lon, double lat, String name, String src, String coordinate, String callnative) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("https://uri.amap.com/marker?");
        buffer.append("position=" + lon + "," + lat);
        if (name != null) {
            buffer.append("&name=" + name);
        }
        if (src != null) {
            buffer.append("&src=" + src);
        }
        if (coordinate != null) {
            buffer.append("&coordinate=" + coordinate);
        }
        if (callnative != null) {
            buffer.append("&callnative" + callnative);
        }
        return buffer.toString();
    }


    /**
     * @param lon 经度 必要
     * @param lat 维度 必要
     * @return
     */
    public String getSplicePathBuffer(double lon, double lat) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("https://uri.amap.com/marker?");
        buffer.append("position=" + lon + "," + lat);
        return buffer.toString();
    }


}
