package com.mny.mnymaptest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import com.slidelib.SlidingUpPanelLayout;
import com.slidelib.SlidingUpPanelLayout.PanelSlideListener;
import com.slidelib.SlidingUpPanelLayout.PanelState;

public class DemoSlideuppanelActivity extends AppCompatActivity {
    private static final String TAG = "DemoActivity";

    private SlidingUpPanelLayout mLayout;
    private TextView name;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    List<String> your_array_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_slideuppanel);

        ListView lv = (ListView) findViewById(R.id.list);
        name = findViewById(R.id.name);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DemoSlideuppanelActivity.this,
                        "onItemClick" + your_array_list.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        your_array_list = Arrays.asList(
                "This",
                "Is",
                "An",
                "Example",
                "ListView",
                "That",
                "You",
                "Can",
                "Scroll",
                "SlidingUpPanelLayout"
        );
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list);

        lv.setAdapter(arrayAdapter);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelState(PanelState.COLLAPSED);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //当有数据的时候在锚点显示比较好
                initSlidlayout();
            }
        }, 3000);
        /*监听状态变化*/
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                switch (newState) {
                    case EXPANDED:
                    case DRAGGING:
                    case ANCHORED:
                        name.setText("已经为你找到 %$ 家");
                        break;
                    case COLLAPSED:
                        name.setText("上拉显示更多");
                        break;
                }
            }
        });
    }

    public void initSlidlayout() {
        if (mLayout.getAnchorPoint() == 1.0f) {
            mLayout.setAnchorPoint(0.5f);//锚点展示比例
            mLayout.setPanelState(PanelState.ANCHORED); // 设置锚点模式
        }
    }

    ;
}
