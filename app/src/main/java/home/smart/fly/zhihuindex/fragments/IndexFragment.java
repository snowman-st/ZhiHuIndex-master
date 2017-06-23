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

import home.smart.fly.zhihuindex.R;
import home.smart.fly.zhihuindex.User;
import home.smart.fly.zhihuindex.activity.AnswerActivity;
import home.smart.fly.zhihuindex.activity.ProblemCommitActivity;
import home.smart.fly.zhihuindex.adapter.IndexRecyclerViewAdapter;
import home.smart.fly.zhihuindex.Problem;
import home.smart.fly.zhihuindex.settings.UserInf;
import home.smart.fly.zhihuindex.settings.loginActivity;
import home.smart.fly.zhihuindex.util.GsonTool;
import home.smart.fly.zhihuindex.activity.MainActivity;

public class IndexFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private  final int UPDATE =1;
    private Context mContext;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private IndexRecyclerViewAdapter adapter;
    //
    private View rootView;
    private FloatingActionMenu fam;
    private FloatingActionButton askQuestion;
    private List<Problem> recommendedProblemList = new ArrayList<Problem>();
    private Problem problem;
    UserInf use =null;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //UI更新
                case UPDATE:
                    adapter.notifyDataSetChanged();
                    msg.what=0;
                    break;
            }
        }
    };
    //重写方法
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
        use = (UserInf)getActivity().getApplication();
        //悬浮按钮
        fam = (FloatingActionMenu) rootView.findViewById(R.id.menu_yellow);
        //提问
        askQuestion =(FloatingActionButton)rootView.findViewById(R.id.ask_question);
        //点击跳转
        askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.logstate){
                    Log.v("IndexFragment","hahha");
                    Intent inten = new Intent(getActivity(),loginActivity.class);
                    startActivity(inten);
                }

                else{
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),ProblemCommitActivity.class);
                    startActivity(intent);
                }

            }
        });
        View headView = LayoutInflater.from(mContext).inflate(R.layout.index_list_headview, null);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        //进度条颜色(总共4种)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        //进度条位置
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) (mContext.getResources().getDisplayMetrics().density * 64));
        //监听器
        swipeRefreshLayout.setOnRefreshListener(this);
        //设置recyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        for (int i = 0; i < 5; i++) {
            problem = new Problem(1,"zl","xidian",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),1,1,1,"happy",true);
            recommendedProblemList.add(problem);
        }
        adapter = new IndexRecyclerViewAdapter(mContext, recommendedProblemList);
        //设置headView
        adapter.setHeadView(headView);
        //设置adapter
        recyclerView.setAdapter(adapter);
        //实现点击数据
        adapter.setItemClickListener(new IndexRecyclerViewAdapter.MyItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                problem = recommendedProblemList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("AnswerActivity",GsonTool.createProblemJsonString(problem));
                intent.setClass(getActivity(), AnswerActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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

    //下拉刷新
    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL("http://192.168.1.107:8080/HelloServer/servlet/LoginServlet");
                    //新建List<Problem>,用于传输数据
                    List<Problem> newProblemList = new ArrayList<Problem>();
                    newProblemList.add(new Problem(1,null,null,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),0,0,0,"home_page",true));
                    //转换为json
                    String list = GsonTool.createListJsonString(newProblemList);
                    //获得反馈
                    newProblemList = HttpUtil.sendWithHttp(url,list);
                    Log.v("IndexFragment",GsonTool.createListJsonString(newProblemList));
                    if(newProblemList!=null&&newProblemList.size()>0){
                        Log.v("IndexFragment",GsonTool.createListJsonString(newProblemList));
                        //加入从服务器获得的数据
                        recommendedProblemList.addAll(0,newProblemList);
                        Message message = new Message();
                        message.what=UPDATE;
                        //发送message对象
                        handler.sendMessage(message);
                        Toast.makeText(getActivity(), "更新了十条数据...", Toast.LENGTH_SHORT).show();
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
