package home.smart.fly.zhihuindex.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.zhihuindex.R;
import home.smart.fly.zhihuindex.adapter.FragmentAdapter;

import static home.smart.fly.zhihuindex.R.id.viewpager;

/**
 * Created by engineer on 2016/9/21.
 */

public class SecondFragment extends Fragment {
    private View rootView;
    private Context mContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_two, container, false);
        InitView();
        return rootView;
    }


    private void InitView() {
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        mViewPager = (ViewPager) rootView.findViewById(viewpager);

        //初始化TabLayout的title
        mTabLayout.addTab(mTabLayout.newTab().setText("工作"));
        mTabLayout.addTab(mTabLayout.newTab().setText("上研"));
        mTabLayout.addTab(mTabLayout.newTab().setText("期末"));
        mTabLayout.addTab(mTabLayout.newTab().setText("四六级"));

        List<String> titles = new ArrayList<>();
        titles.add("工作");
        titles.add("上研");
        titles.add("期末");
        titles.add("四六级");

        //初始化ViewPager的数据集
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new SecondSubFragment().newInstance("Job"));
        fragments.add(new SecondSubFragment().newInstance("Postgraduate"));
        fragments.add(new SecondSubFragment().newInstance("Terminal"));
        fragments.add(new SecondSubFragment().newInstance("CET"));
        //创建ViewPager的adapter
        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        //千万别忘了，关联TabLayout与ViewPager
        //同时也要覆写PagerAdapter的getPageTitle方法，否则Tab没有title
        mTabLayout.setupWithViewPager(mViewPager);
       // mTabLayout.setTabsFromPagerAdapter(adapter);
    }
}
