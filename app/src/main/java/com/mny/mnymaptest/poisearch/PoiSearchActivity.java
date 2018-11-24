package com.mny.mnymaptest.poisearch;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.mny.mnymaptest.R;
import com.mny.mnymaptest.testsearch.overlay.PoiOverlay;
import com.mny.mnymaptest.testsearch.util.ToastUtil;
import com.mny.mnymaptest.utils.AMapUtil;
import com.slidelib.SlidingUpPanelLayout;
import com.slidelib.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;
import java.util.List;

public class PoiSearchActivity extends AppCompatActivity implements View.OnClickListener {
    public String TAG = this.getClass().getName();

    private AMap aMap;
    /**
     * 请输入关键字
     */
    private AutoCompleteTextView mActv;
    /**
     * 北京
     */
    private EditText mCity;
    /**
     * 搜索
     */
    private Button mBtnSearch;
    private TextView mTvMsg;
    private ListView mLvInfo;
    private SlidingUpPanelLayout mSlidupLayout;

    private ProgressDialog progDialog = null;// 搜索时进度条
    private PoiResult poiResult; // poi返回的结果
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private int currentPage = 0;// 当前页面，从0开始计数
    public String keyWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);
        initView();
    }

    private void initView() {
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.ps_map)).getMap();
            setUpMap();
        }
    }

    /**
     * 设置页面监听
     */
    private void setUpMap() {
        mActv = (AutoCompleteTextView) findViewById(R.id.actv);
        mCity = (EditText) findViewById(R.id.city);
        mBtnSearch = (Button) findViewById(R.id.btn_search);
        mBtnSearch.setOnClickListener(this);
        mTvMsg = (TextView) findViewById(R.id.tv_msg);
        mLvInfo = (ListView) findViewById(R.id.lv_info);
        mSlidupLayout = (SlidingUpPanelLayout) findViewById(R.id.slidup_layout);
        initSldeup();
        mBtnSearch.setOnClickListener(this);
        mActv.addTextChangedListener(tvwarcher);// 添加文本输入框监听事件
        mCity = (EditText) findViewById(R.id.city);
        aMap.setOnMarkerClickListener(markerClickListener);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(infoWindowAdapter);// 添加显示infowindow监听事件
    }

    private void initSldeup() {
        mSlidupLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        /*监听状态变化*/
        mSlidupLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                switch (newState) {
                    case EXPANDED:
                    case DRAGGING:
                    case ANCHORED:
                        mTvMsg.setText("已经为你找到 %$ 家");
                        break;
                    case COLLAPSED:
                        mTvMsg.setText("上拉显示更多");
                        break;
                }
            }
        });
    }


    TextWatcher tvwarcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            marker.showInfoWindow();
            return false;
        }
    };
    AMap.InfoWindowAdapter infoWindowAdapter = new AMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(final Marker marker) {
            View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
                    null);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(marker.getTitle());

            TextView snippet = (TextView) view.findViewById(R.id.snippet);
            snippet.setText(marker.getSnippet());
            ImageButton button = (ImageButton) view
                    .findViewById(R.id.start_amap_app);
            // 调起高德地图app
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAMapNavi(marker);
                }
            });
            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_search:
                keyWord = AMapUtil.checkEditText(mActv);
                if ("".equals(keyWord)) {
                    ToastUtil.show(PoiSearchActivity.this, "请输入搜索关键字");
                    return;
                } else {
                    doSearchQuery();
                }
                break;
        }
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        showProgressDialog();// 显示进度框
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", mCity.getText().toString());// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(searchListener);
        poiSearch.searchPOIAsyn();
    }

    PoiSearch.OnPoiSearchListener searchListener = new PoiSearch.OnPoiSearchListener() {
        /**

         * POI信息查询回调方法
         */
        @Override
        public void onPoiSearched(PoiResult result, int rCode) {
            dissmissProgressDialog();// 隐藏对话框
            if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getQuery() != null) {// 搜索poi的结果
                    if (result.getQuery().equals(query)) {// 是否是同一条
                        poiResult = result;
                        // 取得搜索到的poiitems有多少页
                        List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < poiItems.size(); i++) {
                            list.add(poiItems.get(i).getSnippet());
                        }
                        setListView(list);
                        mTvMsg.setText("已经为您找到" + list.size() + "家");
                        mSlidupLayout.setAnchorPoint(0.4f);
                        mSlidupLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                        if (poiItems != null && poiItems.size() > 0) {
                            aMap.clear();// 清理之前的图标
                            PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                            poiOverlay.removeFromMap();
                            poiOverlay.addToMap();
                            poiOverlay.zoomToSpan();
                        } else {
                            ToastUtil.show(PoiSearchActivity.this,
                                    R.string.no_result);
                        }
                    }
                } else {
                    ToastUtil.show(PoiSearchActivity.this,
                            R.string.no_result);
                }
            } else {
                ToastUtil.showerror(PoiSearchActivity.this, rCode);
            }

        }

        @Override
        public void onPoiItemSearched(PoiItem item, int rCode) {
            // TODO Auto-generated method stub

        }
    };

    /**
     * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
     */
    public void startAMapNavi(Marker marker) {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(marker.getPosition());
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);

        // 调起高德地图导航
        try {
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (com.amap.api.maps.AMapException e) {

            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());

        }
    }

    private void setListView(List<String> your_array_list) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list);

        mLvInfo.setAdapter(arrayAdapter);
    }
}
