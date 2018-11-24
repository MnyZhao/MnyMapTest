package com.mny.mnymaptest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.LatLonSharePoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.share.ShareSearch;
import com.amap.api.services.share.ShareSearch.ShareFromAndTo;
import com.mny.mnymaptest.testsearch.util.ToastUtil;
import com.mny.mnymaptest.utils.GetSharePathUtil;
import com.mny.mnymaptest.utils.ShareGetUrl;

/**
 * 短串分享  通过 ShareSearch 生成url
 * 或者自己那着经纬度去拼接 详情访问 https://lbs.amap.com/api/uri-api/gettingstarted
 */
public class GetShareUrlActivity extends AppCompatActivity {

    /**
     * 位置Url
     */
    private Button mBtnLocation;
    /**
     * 导航Url
     */
    private Button mBtnNav;
    /**
     * POI_Url
     */
    private Button mBtnPoi;
    /**
     * 路径规划Url  驾车 公交 步行
     */
    private Button mBtnPath;
    private Button mBtnBusPath;
    private Button mBtnWalkPath;
    private TextView mTvShowUrl;
    /**
     * 获取拼接路径方法
     */
    private Button mBtnGetUrl;
    /**
     * 坐标信息
     */
    private LatLonPoint START = new LatLonPoint(39.989646, 116.480864);
    private LatLonPoint END = new LatLonPoint(39.983456, 116.3154950);
    private LatLonPoint POI_POINT = new LatLonPoint(39.989646, 116.480864);
    /**
     * 分享类
     */
    ShareSearch mShareSearch;
    private ProgressDialog mProgDialog = null;// 搜索时进度条

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_share_url);
        mShareSearch = new ShareSearch(this);
//        mShareSearch.setOnShareSearchListener(shareSearchListener);
        initView();
        GetSharePathUtil.getInstance().setOnGetUrlListener(new GetSharePathUtil.onGetUrlListener() {
            @Override
            public void getUrl(String url, int mode) {
                mTvShowUrl.setText(url);
                Log.e("GetShareUrlActivity", "getUrl: " + url);
                switch (mode) {
                    case 1:
                        Log.e("GetShareUrlActivity:", "位置");
                        break;
                    case 2:
                        Log.e("GetShareUrlActivity:", "POI");
                        break;
                    case 3:
                        Log.e("GetShareUrlActivity:", "导航");
                        break;
                    case 4:
                        Log.e("GetShareUrlActivity:", "公交路径规划");
                        break;
                    case 5:
                        Log.e("GetShareUrlActivity:", "步行路径规划");
                        break;
                    case 6:
                        Log.e("GetShareUrlActivity:", "驾车路径规划");
                        break;
                }
            }

            @Override
            public void errorCode(int errorCode) {
                Toast.makeText(GetShareUrlActivity.this, "Error:" + errorCode, Toast.LENGTH_SHORT).show();
            }
        }, mShareSearch);
    }

    private void initView() {
        mBtnLocation = (Button) findViewById(R.id.btn_location);
        mBtnLocation.setOnClickListener(listener);
        mBtnNav = (Button) findViewById(R.id.btn_nav);
        mBtnNav.setOnClickListener(listener);
        mBtnPoi = (Button) findViewById(R.id.btn_poi);
        mBtnPoi.setOnClickListener(listener);
        mBtnPath = (Button) findViewById(R.id.btn_route_path);
        mBtnPath.setOnClickListener(listener);
        mBtnBusPath = findViewById(R.id.btn_route_bus_path);
        mBtnBusPath.setOnClickListener(listener);
        mBtnWalkPath = findViewById(R.id.btn_route_walk_path);
        mBtnWalkPath.setOnClickListener(listener);
        mBtnGetUrl = findViewById(R.id.btn_get_url);
        mBtnGetUrl.setOnClickListener(listener);
        mTvShowUrl = (TextView) findViewById(R.id.tv_show_url);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                default:
                    break;
                case R.id.btn_location:
                    String snippet = "方恒国际中心A座";
//                    shareLocation(snippet);
                    GetSharePathUtil.getInstance().getLocationUrl(POI_POINT, snippet);//封装方式
                    break;
                case R.id.btn_poi:
                    String title = "高德软件有限公司";
                    String snippet1 = "方恒国际中心A座";
//                    sharePOI(title, snippet1);
                    GetSharePathUtil.getInstance().getPoiUrl(null, POI_POINT, title, snippet1);
                    break;
                case R.id.btn_nav:
//                    shareNavi();
                    GetSharePathUtil.getInstance().getNaviUrl(START, END);
                    break;
                case R.id.btn_route_path:
//                    shareRoute();
                    GetSharePathUtil.getInstance().getDriverRoteUrl(START, END);
                    break;
                case R.id.btn_route_bus_path:
//                    shareRouteBus();
                    GetSharePathUtil.getInstance().getBusRoteUrl(START, END);
                    break;
                case R.id.btn_route_walk_path:
//                    shareRouteWalk();
                    GetSharePathUtil.getInstance().getWalkRoteUrl(START, END);
                    break;
                case R.id.btn_get_url:
                    mTvShowUrl.setText(ShareGetUrl.getInstance().getSpliceAllPathBuffer(POI_POINT.getLatitude(),
                            POI_POINT.getLongitude(),
                            "方恒国际",
                            "mnydemo",
                            "gaode",
                            "1"));
                    Log.e("GetShareUrlActivity", mTvShowUrl.getText().toString());
                    break;
            }
        }
    };

    /**
     * 位置转短串分享
     *
     * @param snippet
     */
    private void shareLocation(String snippet) {
        LatLonSharePoint point = new LatLonSharePoint(POI_POINT.getLatitude(),
                POI_POINT.getLongitude(), snippet);
        showProgressDialog();
        mShareSearch.searchLocationShareUrlAsyn(point);
    }

    /**
     * POI转短串分享
     */
    private void sharePOI(String title, String snippet) {
        PoiItem item = new PoiItem(null, POI_POINT, title, snippet);
        showProgressDialog();
        mShareSearch.searchPoiShareUrlAsyn(item);

    }

    /**
     * 驾车路径规划短串分享
     */
    private void shareRoute() {
        ShareSearch.ShareFromAndTo fromAndTo = new ShareSearch.ShareFromAndTo(START, END);
        ShareSearch.ShareDrivingRouteQuery query = new ShareSearch.ShareDrivingRouteQuery(fromAndTo,
                ShareSearch.DrivingDefault);
        showProgressDialog();
        mShareSearch.searchDrivingRouteShareUrlAsyn(query);
    }

    /**
     * 公交路径规划短串分享
     */
    private void shareRouteBus() {
        ShareFromAndTo fromAndTo = new ShareFromAndTo(START, END);
        ShareSearch.ShareBusRouteQuery query = new ShareSearch.ShareBusRouteQuery(fromAndTo,
                ShareSearch.BusDefault);
        showProgressDialog();
        mShareSearch.searchBusRouteShareUrlAsyn(query);
    }

    /**
     * 步行路径规划短串分享
     */
    private void shareRouteWalk() {
        ShareFromAndTo fromAndTo = new ShareFromAndTo(START, END);
        ShareSearch.ShareWalkRouteQuery query = new ShareSearch.ShareWalkRouteQuery(fromAndTo,
                ShareSearch.BusDefault);
        showProgressDialog();
        mShareSearch.searchWalkRouteShareUrlAsyn(query);
    }

    /**
     * 导航短串分享
     */
    private void shareNavi() {
        ShareSearch.ShareFromAndTo fromAndTo = new ShareFromAndTo(START, END);
        ShareSearch.ShareNaviQuery query = new ShareSearch.ShareNaviQuery(fromAndTo,
                ShareSearch.NaviDefault);
        showProgressDialog();
        mShareSearch.searchNaviShareUrlAsyn(query);
        try {
            Log.e("GetShareUrlActivity", mShareSearch.searchNaviShareUrl(query));
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    ShareSearch.OnShareSearchListener shareSearchListener = new ShareSearch.OnShareSearchListener() {
        @Override
        public void onPoiShareUrlSearched(String url, int errorCode) {
            dissmissProgressDialog();
            Log.e("GetShareUrlActivity", url);
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                mTvShowUrl.setText("POI:" + url);
            } else {
                ToastUtil.showerror(GetShareUrlActivity.this, errorCode);
            }
        }

        @Override
        public void onLocationShareUrlSearched(String url, int errorCode) {
            Log.e("GetShareUrlActivity", url);
            dissmissProgressDialog();
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                mTvShowUrl.setText("Location:" + url);
            } else {
                ToastUtil.showerror(GetShareUrlActivity.this, errorCode);
            }
        }

        @Override
        public void onNaviShareUrlSearched(String url, int errorCode) {
            Log.e("GetShareUrlActivity", url);
            dissmissProgressDialog();
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                mTvShowUrl.setText("Nav:" + url);
            } else {
                ToastUtil.showerror(GetShareUrlActivity.this, errorCode);
            }
        }

        @Override
        public void onBusRouteShareUrlSearched(String url, int errorCode) {
            Log.e("GetShareUrlActivity", url);
            //公交路径规划
            dissmissProgressDialog();
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                mTvShowUrl.setText("BUS Route path:" + url);
            } else {
                ToastUtil.showerror(GetShareUrlActivity.this, errorCode);
            }
        }

        @Override
        public void onWalkRouteShareUrlSearched(String url, int errorCode) {
            //步行路径规划
            dissmissProgressDialog();
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                mTvShowUrl.setText("Walk Route path:" + url);
            } else {
                ToastUtil.showerror(GetShareUrlActivity.this, errorCode);
            }
        }

        @Override
        public void onDrivingRouteShareUrlSearched(String url, int errorCode) {
            dissmissProgressDialog();
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                mTvShowUrl.setText("Drivier Route path:" + url);
            } else {
                ToastUtil.showerror(GetShareUrlActivity.this, errorCode);
            }
        }
    };

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (mProgDialog == null) {
            mProgDialog = new ProgressDialog(this);
        }
        mProgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgDialog.setIndeterminate(false);
        mProgDialog.setCancelable(true);
        mProgDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (mProgDialog != null && mProgDialog.isShowing()) {
            mProgDialog.dismiss();
        }
    }
}
