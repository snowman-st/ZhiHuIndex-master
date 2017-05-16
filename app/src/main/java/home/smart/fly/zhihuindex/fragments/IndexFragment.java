package home.smart.fly.zhihuindex.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.zhihuindex.R;
import home.smart.fly.zhihuindex.activity.ProblemCommitActivity;
import home.smart.fly.zhihuindex.adapter.IndexRecyclerViewAdapter;

public class IndexFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private Context mContext;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private IndexRecyclerViewAdapter adapter;
    //
    private View rootView;
    private FloatingActionMenu fam;
    private FloatingActionButton askQuestion;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_index, null);
        InitView();
        return rootView;
    }

    private void InitView() {
        //悬浮按钮
        fam = (FloatingActionMenu) rootView.findViewById(R.id.menu_yellow);
        //提问
        askQuestion =(FloatingActionButton)rootView.findViewById(R.id.ask_question);
        askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),ProblemCommitActivity.class);
                startActivity(intent);
            }
        });

        View headView = LayoutInflater.from(mContext).inflate(R.layout.index_list_headview, null);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        //进度条颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        //进度条位置
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) (mContext.getResources().getDisplayMetrics().density * 64));
        //监听器
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add("This is item " + i);
        }
        adapter = new IndexRecyclerViewAdapter(mContext, datas);
        adapter.setHeadView(headView);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //悬浮按钮，上滑隐藏，下滑显现
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > 5) {
                    //隐藏
                    if (dy > 0) fam.hideMenu(true);
                    //显示
                    else fam.showMenu(true);
                }
            }
        });


    }


    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新时间2s，
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);

    }


}
