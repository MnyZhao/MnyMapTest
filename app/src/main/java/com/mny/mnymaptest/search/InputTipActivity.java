package com.mny.mnymaptest.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.mny.mnymaptest.R;
import com.mny.mnymaptest.testsearch.InputTipsActivity;
import com.mny.mnymaptest.testsearch.TestSearchActivity;
import com.mny.mnymaptest.testsearch.adapter.InputTipsAdapter;
import com.mny.mnymaptest.testsearch.util.Constants;
import com.mny.mnymaptest.testsearch.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class InputTipActivity extends AppCompatActivity {

    private ImageButton mBack;
    private SearchView mSearchView;
    private ListView mInputListView;
    private InputTipsAdapter mIntipAdapter;
    private List<Tip> mCurrentTipList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_tips);
        initView();
        initSearchView();
    }

    private void initView() {
        mBack = (ImageButton) findViewById(R.id.back);
        mSearchView = (SearchView) findViewById(R.id.keyWord);
        mInputListView = (ListView) findViewById(R.id.inputtip_list);
        mInputListView.setOnItemClickListener(itemClickListener);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputTipActivity.this.finish();
            }
        });
    }
    private AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mCurrentTipList != null) {
                Tip tip = (Tip) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRA_TIP, tip);
                setResult(TestSearchActivity.RESULT_CODE_INPUTTIPS, intent);
                InputTipActivity.this.finish();
            }
        }
    };
    private void initSearchView() {
        mSearchView = (SearchView) findViewById(R.id.keyWord);
        mSearchView.setOnQueryTextListener(queryTextListener);
        //设置SearchView默认为展开显示
        mSearchView.setIconified(false);
        mSearchView.onActionViewExpanded();
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setSubmitButtonEnabled(false);
    }
    SearchView.OnQueryTextListener queryTextListener=new SearchView.OnQueryTextListener() {
        /**
         * 按下确认键触发，本例为键盘回车或搜索键
         *
         * @param query
         * @return
         */
        @Override
        public boolean onQueryTextSubmit(String query) {
            Intent intent = new Intent();
            intent.putExtra(Constants.KEY_WORDS_NAME, query);
            setResult(TestSearchActivity.RESULT_CODE_KEYWORDS, intent);
            InputTipActivity.this.finish();
            return false;
        }

        /**
         * 用户更改查询文本时调用。
         * @param newText
         * @return
         */
        @Override
        public boolean onQueryTextChange(String newText) {
            if (!IsEmptyOrNullString(newText)) {
                InputtipsQuery inputquery = new InputtipsQuery(newText, Constants.DEFAULT_CITY);
                Inputtips inputTips = new Inputtips(InputTipActivity.this.getApplicationContext(), inputquery);
                inputTips.setInputtipsListener(inputtipsListener);
                inputTips.requestInputtipsAsyn();
            } else {
                if (mIntipAdapter != null && mCurrentTipList != null) {
                    mCurrentTipList.clear();
                    mIntipAdapter.notifyDataSetChanged();
                }
            }
            return false;
        }
    };
    /**
     * 查询结果监听
     */
    Inputtips.InputtipsListener inputtipsListener=new Inputtips.InputtipsListener() {
        @Override
        public void onGetInputtips(List<Tip> tipList, int rCode) {
            if (rCode == 1000) {// 正确返回
                mCurrentTipList = tipList;
                List<String> listString = new ArrayList<String>();
                for (int i = 0; i < tipList.size(); i++) {
                    listString.add(tipList.get(i).getName());
                }
                mIntipAdapter = new InputTipsAdapter(
                        getApplicationContext(),
                        mCurrentTipList);
                mInputListView.setAdapter(mIntipAdapter);
                mIntipAdapter.notifyDataSetChanged();
            } else {
                ToastUtil.showerror(InputTipActivity.this, rCode);
            }
        }
    };
    public static boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }
}
