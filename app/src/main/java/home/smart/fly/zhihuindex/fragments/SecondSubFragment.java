package home.smart.fly.zhihuindex.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import net.HttpUtil;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import home.smart.fly.zhihuindex.Problem;
import home.smart.fly.zhihuindex.R;
import home.smart.fly.zhihuindex.activity.ProblemCommitActivity;
import home.smart.fly.zhihuindex.adapter.IndexRecyclerViewAdapter;
import home.smart.fly.zhihuindex.adapter.SubRecyclerViewAdapter;
import home.smart.fly.zhihuindex.util.GsonTool;

public class SecondSubFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final int UPDATE =1;
    private Context mContext;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
//    private SubRecyclerViewAdapter adapter;
    //
    private View rootView;
    private FloatingActionMenu fam;
    private FloatingActionButton askQuestion;
    private IndexRecyclerViewAdapter adapter;

    private Problem problem;
    private List<Problem> problemList = new ArrayList<Problem>();
    private String pType;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE:
                     adapter.notifyDataSetChanged();
                     msg.what=0;
                     break;
            }
        }
    };
    //用于获得类型
    public static SecondSubFragment newInstance(String pType) {
        SecondSubFragment myFragment = new SecondSubFragment();
        Bundle bundle = new Bundle();
        bundle.putString("pType",pType);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pType  = getArguments().getString("pType");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_second_sub, null);
        InitView();
        return rootView;
    }

    private void InitView() {
        //悬浮按钮
        fam = (FloatingActionMenu) rootView.findViewById(R.id.menu_yellow);
        //提问
        askQuestion =(FloatingActionButton)rootView.findViewById(R.id.ask_question);
        //点击跳转
        askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),ProblemCommitActivity.class);
                startActivity(intent);
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) (mContext.getResources().getDisplayMetrics().density * 64));
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        for (int i = 0; i < 5; i++) {
            problem = new Problem(1,"zl","xidian",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),0,0,0,"happy",true);
            problemList.add(problem);
        }
        adapter = new IndexRecyclerViewAdapter(mContext,problemList);
        adapter.setHeadView(null);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > 5) {
                    if (dy > 0) fam.hideMenu(true);
                    else fam.showMenu(true);
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL("http://192.168.1.106:8080/HelloServer/servlet/ClassificationServlet");
                    //新建List<Problem>,用于传输数据
                    List<Problem> newProblemList = new ArrayList<Problem>();
                    newProblemList.add(new Problem(1,null,null,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),1,1,1,pType,true));
                    //转换为json
                    String list = GsonTool.createListJsonString(newProblemList);
                    //获得反馈
                    newProblemList = HttpUtil.sendWithHttp(url,list);
                    Log.v("SecondSubFragment",GsonTool.createListJsonString(newProblemList));
                    if(newProblemList!=null&&newProblemList.size()>0){
                        Log.v("IndexFragment",GsonTool.createListJsonString(newProblemList));
                        //加入从服务器获得的数据
                        problemList.addAll(0,newProblemList);
                        Message message = new Message();
                        message.what=UPDATE;
                        //发送message对象
                        handler.sendMessage(message);
                        Toast.makeText(getActivity(), "已为你更新", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getActivity(), "无法更新", Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    e.printStackTrace();
                }
                finally {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }

}
