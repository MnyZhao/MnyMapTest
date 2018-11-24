package com.mny.mnymaptest.utils;

import android.util.Log;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.LatLonSharePoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.share.ShareSearch;

/**
 * 获取分享路径url
 * {@link #setOnGetUrlListener(onGetUrlListener, ShareSearch)} 设置监听事件
 * 方式一
 * 获取短串分享url 均为异步所以再调用方法之后要考虑延迟时间
 * 要先调用ShareSearchGetUrl.getInstance(context).setOnGetUrlListener(listener);
 * {@link #getLocationUrl(LatLonPoint, String)}  位置分享
 * {@link #getPoiUrl(String, LatLonPoint, String, String)}   poi分享
 * {@link #getNaviUrl(LatLonPoint, LatLonPoint)} 导航
 * {@link #getDriverRoteUrl(LatLonPoint, LatLonPoint) }驾车路径规划
 * {@link #getBusRoteUrl(LatLonPoint, LatLonPoint) }公交路径规划
 * {@link #getWalkRoteUrl(LatLonPoint, LatLonPoint)} 步行路径规划
 * 方式二
 * 拼接路径方法 不需要设置监听事件setOnGetUrlListener();
 * {@link #getSpliceAllPathBuffer(double, double, String, String, String, String)} 拼接全路径
 * {@link #getSplicePathBuffer(double, double)} 拼接位置路径
 */
public class GetSharePathUtil {
    private String TAG = "GetSharePathUtil";
    //位置url
    public static final int LOCATION = 1;
    //POI url
    public static final int POI = 2;
    //导航url
    public static final int NAVI = 3;
    //公交规划url
    public static final int BUS_ROUTE = 4;
    //步行规划url
    public static final int WALK_ROUTE = 5;
    //驾车规划url
    public static final int DRIVER_ROUTE = 6;
    /**
     * 分享类
     */
    private ShareSearch mShareSearch;
    private static GetSharePathUtil instance;

    private GetSharePathUtil() {

    }

    public static GetSharePathUtil getInstance() {
        if (instance == null) {
            instance = new GetSharePathUtil();
        }
        return instance;
    }

    /**
     * 位置
     *
     * @param locationPoint  分享的位置
     * @param sharePointName 分享位置名称
     */
    public void getLocationUrl(LatLonPoint locationPoint, String sharePointName) {
        LatLonSharePoint point = new LatLonSharePoint(locationPoint.getLatitude(),
                locationPoint.getLongitude(), sharePointName);
        //异步
        mShareSearch.searchLocationShareUrlAsyn(point);
    }

    /**
     * poi
     *
     * @param id      POI的id 可以为空
     * @param point   该POI的位置。
     * @param title   该POI的名称。
     * @param snippet POI的地址。 可以为"";
     *                示例getPoiUrl(null,point,"高德软件公司","方恒国际中心A")
     *                示例getPoiUrl(null,point,"高德软件公司","")
     */
    public void getPoiUrl(String id, LatLonPoint point, String title, String snippet) {
        PoiItem item = new PoiItem(null, point, title, snippet);
        //异步
        mShareSearch.searchPoiShareUrlAsyn(item);
    }

    /**
     * Navi 导航url
     *
     * @param start 开始位置
     * @param end   结束位置
     *              ShareSearch.NaviDefault 默认最佳路线
     */
    public void getNaviUrl(LatLonPoint start, LatLonPoint end) {
        ShareSearch.ShareFromAndTo fromAndTo = new ShareSearch.ShareFromAndTo(start, end);
        ShareSearch.ShareNaviQuery query = new ShareSearch.ShareNaviQuery(fromAndTo,
                ShareSearch.NaviDefault);
        //  异步
        mShareSearch.searchNaviShareUrlAsyn(query);
    }

    /**
     * 公交路径规划
     *
     * @param start 开始位置
     * @param end   结束位置
     */
    public void getBusRoteUrl(LatLonPoint start, LatLonPoint end) {
        ShareSearch.ShareFromAndTo fromAndTo = new ShareSearch.ShareFromAndTo(start, end);
        ShareSearch.ShareBusRouteQuery query = new ShareSearch.ShareBusRouteQuery(fromAndTo,
                ShareSearch.BusDefault);
        //异步
        mShareSearch.searchBusRouteShareUrlAsyn(query);
    }

    /**
     * 步行路径规划
     *
     * @param start
     * @param end
     */
    public void getWalkRoteUrl(LatLonPoint start, LatLonPoint end) {
        ShareSearch.ShareFromAndTo fromAndTo = new ShareSearch.ShareFromAndTo(start, end);
        ShareSearch.ShareWalkRouteQuery query = new ShareSearch.ShareWalkRouteQuery(fromAndTo,
                RouteSearch.WalkDefault);
        //异步
        mShareSearch.searchWalkRouteShareUrlAsyn(query);
    }

    /**
     * 驾车路径规划
     *
     * @param start 开始位置
     * @param end   结束位置
     */
    public void getDriverRoteUrl(LatLonPoint start, LatLonPoint end) {
        ShareSearch.ShareFromAndTo fromAndTo = new ShareSearch.ShareFromAndTo(start, end);
        ShareSearch.ShareDrivingRouteQuery query = new ShareSearch.ShareDrivingRouteQuery(fromAndTo,
                ShareSearch.DrivingDefault);
        //异步
        mShareSearch.searchDrivingRouteShareUrlAsyn(query);
    }

    ShareSearch.OnShareSearchListener searchListener = new ShareSearch.OnShareSearchListener() {
        @Override
        public void onPoiShareUrlSearched(String url, int errorCode) {
            if (getUrlListener != null) {
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    getUrlListener.getUrl(url, POI);
                } else {
                    getUrlListener.errorCode(errorCode);
                }
            } else {
                Log.e(TAG, "请调用setOnGetUrlListener()设置url监听事件");
            }
        }

        @Override
        public void onLocationShareUrlSearched(String url, int errorCode) {
            if (getUrlListener != null) {
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    getUrlListener.getUrl(url, LOCATION);
                } else {
                    getUrlListener.errorCode(errorCode);
                }
            } else {
                Log.e(TAG, "请调用setOnGetUrlListener()设置url监听事件");
            }
        }

        @Override
        public void onNaviShareUrlSearched(String url, int errorCode) {
            if (getUrlListener != null) {
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    getUrlListener.getUrl(url, NAVI);
                } else {
                    getUrlListener.errorCode(errorCode);
                }
            } else {
                Log.e(TAG, "请调用setOnGetUrlListener()设置url监听事件");
            }
        }

        @Override
        public void onBusRouteShareUrlSearched(String url, int errorCode) {
            if (getUrlListener != null) {
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    getUrlListener.getUrl(url, BUS_ROUTE);
                } else {
                    getUrlListener.errorCode(errorCode);
                }
            } else {
                Log.e(TAG, "请调用setOnGetUrlListener()设置url监听事件");
            }
        }

        @Override
        public void onWalkRouteShareUrlSearched(String url, int errorCode) {
            if (getUrlListener != null) {
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    getUrlListener.getUrl(url, WALK_ROUTE);
                } else {
                    getUrlListener.errorCode(errorCode);
                }
            } else {
                Log.e(TAG, "请调用setOnGetUrlListener()设置url监听事件");
            }
        }

        @Override
        public void onDrivingRouteShareUrlSearched(String url, int errorCode) {
            if (getUrlListener != null) {
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    getUrlListener.getUrl(url, DRIVER_ROUTE);
                } else {
                    getUrlListener.errorCode(errorCode);
                }
            } else {
                Log.e(TAG, "请调用setOnGetUrlListener()设置url监听事件");
            }
        }
    };
    private onGetUrlListener getUrlListener;

    /**
     * 设置获取url的监听
     *
     * @param listener
     * @param mShareSearch 分享类
     */
    public void setOnGetUrlListener(onGetUrlListener listener, ShareSearch mShareSearch) {
        this.getUrlListener = listener;
        this.mShareSearch = mShareSearch;
        this.mShareSearch.setOnShareSearchListener(searchListener);
    }

    /**
     * url监听
     */
    public interface onGetUrlListener {
        /**
         * @param url  最终url
         * @param mode url类型 {@link #LOCATION {@link #POI}{@link #NAVI}{@link #BUS_ROUTE}
         *             {@link #WALK_ROUTE}{@link #DRIVER_ROUTE}}
         */
        void getUrl(String url, int mode);

        /**
         * 错误码
         *
         * @param errorCode
         */
        void errorCode(int errorCode);
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
