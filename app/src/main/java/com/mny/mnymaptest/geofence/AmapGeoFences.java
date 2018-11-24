package com.mny.mnymaptest.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;

import java.util.List;


/**
 * Crate by E470PD on 2018/9/20
 */
public class AmapGeoFences {
    private String TAG = "AmapGeoFences";
    private Context mContext;
    private AMap aMap;
    private OnGeoCallBackListener geoCallBackListener;
    /*地理围栏客户端*/
    private GeoFenceClient mGeoFenceClient;
    //定义接收广播的action字符串
    public static final String GEOFENCE_BROADCAST_ACTION = "com.amap.geofence";

    public AmapGeoFences(Context mContext, AMap aMap, OnGeoCallBackListener listener) {
        this.mContext = mContext;
        this.aMap = aMap;
        this.geoCallBackListener = listener;
        initGeoClient();
        registerGeoReciver();

    }

    private void initGeoClient() {
        mGeoFenceClient = new GeoFenceClient(mContext);
        //设置pendingIntent
        mGeoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        //设置添加围栏监听
        mGeoFenceClient.setGeoFenceListener(geoFenceListener);
        //设置action
        mGeoFenceClient.setActivateAction(GeoFenceClient.GEOFENCE_IN | GeoFenceClient.GEOFENCE_STAYED | GeoFenceClient.GEOFENCE_OUT);
    }

    /**
     * @param dPoint   围栏中心点
     * @param radios   要创建的围栏半径 ，半径无限制，单位米
     * @param customId 与围栏关联的自有业务Id
     */
    public void addGeoFenceCircle(DPoint dPoint, float radios, String customId) {
        initGeoClient();
        mGeoFenceClient.addGeoFence(dPoint, radios, customId);
    }

    /**
     * @param longitude 中心点的经度
     * @param latitude  中心点的维度
     * @param radios    半径
     * @param customId  与围栏业务关联的自有业务id
     */
    public void addGeoFenceCircle(double longitude, double latitude, float radios, String customId) {
        initGeoClient();
        DPoint dPoint = new DPoint();
        dPoint.setLongitude(longitude);
        dPoint.setLatitude(latitude);
        mGeoFenceClient.addGeoFence(dPoint, radios, customId);
    }

    /**
     * 添加多边形 围栏
     *
     * @param dPoints  多边形的边界坐标点，最少传3个
     * @param customId 与围栏业务关联的自有业务id
     */
    public void addPolygonFence(List<DPoint> dPoints, String customId) {
        Log.e(TAG, "addPolygonFence: ");
        initGeoClient();
        mGeoFenceClient.addGeoFence(dPoints, customId);
    }

    GeoFenceListener geoFenceListener = new GeoFenceListener() {
        @Override
        public void onGeoFenceCreateFinished(List<GeoFence> list, int errorCode, String customId) {
            Log.e(TAG, "onGeoFenceCreateFinished" + errorCode + customId);
            if (errorCode == GeoFence.ADDGEOFENCE_SUCCESS) {
                //创建成功
                geoCallBackListener.CallBack(list, customId);
            } else {
                //创建失败
                geoCallBackListener.Error(errorCode);
            }
        }
    };

    /**
     * 注册围栏触发行为广播
     */
    private void registerGeoReciver() {
        IntentFilter filter = new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(GEOFENCE_BROADCAST_ACTION);
        mContext.registerReceiver(mGeoFenceReceiver, filter);
    }

    private void unRegisterGeoReciver() {
        mContext.unregisterReceiver(mGeoFenceReceiver);
    }

    BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
                //解析广播内容
                //获取Bundle
                Bundle bundle = intent.getExtras();
                //获取围栏行为：
                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                //获取自定义的围栏标识：
                String customId = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                //获取围栏ID:
                String fenceId = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID);
                //获取当前有触发的围栏对象：
                GeoFence fence = bundle.getParcelable(GeoFence.BUNDLE_KEY_FENCE);
                //定位错误码
                int locationErrorCode = bundle.getInt(GeoFence.BUNDLE_KEY_LOCERRORCODE);
                switch (status) {
                    case GeoFence.STATUS_LOCFAIL:
                        Log.e(TAG, "定位失败" + locationErrorCode);
                        geoCallBackListener.GeoFenceLocationError("定位失败", locationErrorCode);
                        break;
                    case GeoFence.STATUS_IN:
                        geoCallBackListener.GeoFence(fence, "进入围栏" + fenceId);
                        Log.e(TAG, "进入围栏" + fenceId);
                        break;
                    case GeoFence.STATUS_OUT:
                        Log.e(TAG, "离开围栏" + fenceId);
                        geoCallBackListener.GeoFence(fence, "离开围栏" + fenceId);
                        break;
                    case GeoFence.STATUS_STAYED:
                        Log.e(TAG, "停留在围栏内" + fenceId);
                        geoCallBackListener.GeoFence(fence, "停留在围栏内" + fenceId);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    /**
     * 移除所有的围栏 不解除监听
     */
    public void removeGenFecneAll() {
        try {
            mGeoFenceClient.removeGeoFence();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "removeGenFecneAll: "+e.getMessage());
        }
    }

    /**
     * 移除所有的围栏 并解除监听
     */
    public void removeGenAllUnRegester() {
        try {
            mGeoFenceClient.removeGeoFence();
            unRegisterGeoReciver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除指定的围栏
     *
     * @param geoFence 围栏对象
     */
    public void removeGenFecne(GeoFence geoFence) {
        mGeoFenceClient.removeGeoFence(geoFence);
    }

    /**
     * 将添加围栏的状态发送给
     */
    public interface OnGeoCallBackListener {
        /**
         * 创建围栏后使用 用来传递创建状态
         *
         * @param geoFences 多边形的边界坐标点，最少传3个
         * @param customID  与围栏业务关联的自有业务id
         */
        void CallBack(List<GeoFence> geoFences, String customID);

        /**
         * 创建围栏后使用 用来传递创建状态
         *
         * @param errorCode 错误码
         */
        void Error(int errorCode);

        /**
         * 判断位置是否在围栏内调用
         *
         * @param fence 返回围栏
         * @param Msg   返回是否在围栏状态信息
         */
        void GeoFence(GeoFence fence, String Msg);

        /**
         * 判断位置是否在围栏内调用
         *
         * @param msg               定位失败
         * @param locationErrorCode 定位错误代码
         */
        void GeoFenceLocationError(String msg, int locationErrorCode);
    }
}
