package home.smart.fly.zhihuindex.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.zhihuindex.R;
import home.smart.fly.zhihuindex.adapter.FragmentTabAdapter;
import home.smart.fly.zhihuindex.fragments.FourFragment;
import home.smart.fly.zhihuindex.fragments.IndexFragment;
import home.smart.fly.zhihuindex.fragments.SecondFragment;

/**
 * Created by co-mall on 2016/9/13.
 */
public class MainActivity extends FragmentActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;
    private FrameLayout content;
    /*AppBarLayout继承自LinearLayout，布局方向为垂直方向。
      AppBarLayout是在LinearLayou上加了一些材料设计的概念，
      它可以让你定制当某个可滚动View的滚动手势发生变化时，其内部的子View实现何种动作。
      在此为上滑出现，下滑隐藏
    * */
    private AppBarLayout index_app_bar;

    private List<Fragment> fragments = new ArrayList<>();

    //View
    private RadioGroup rgs;
    //底部栏
    private RadioButton index_tab;
    private int currentIndex = 0;

    //搜索栏
    private EditText editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        InitView();
    }

    private void InitView() {
        content = (FrameLayout) findViewById(R.id.content);
        index_app_bar = (AppBarLayout) findViewById(R.id.index_app_bar);
        rgs = (RadioGroup) findViewById(R.id.tabs_rg);
        index_tab = (RadioButton) findViewById(R.id.home_tab);

        editText =(EditText)findViewById(R.id.editText);
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Log.d("Activity","11111111111111111111111111111111111111");
                intent.setClass(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
       /* editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });*/


        fragments.add(new IndexFragment());
        fragments.add(new SecondFragment());
        fragments.add(new FourFragment());

        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.content, rgs);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                super.OnRgsExtraCheckedChanged(radioGroup, checkedId, index);
                Log.e("CheckedChanged", "-----" + index);
                currentIndex = index;
                resetView();
                switch (index) {
                    case 0:
                        index_app_bar.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        break;

                }

            }
        });




    }

    private void resetView() {
        index_app_bar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

        if (currentIndex != 0) {
            index_tab.setChecked(true);
        } else {
            super.onBackPressed();
        }
    }
}
