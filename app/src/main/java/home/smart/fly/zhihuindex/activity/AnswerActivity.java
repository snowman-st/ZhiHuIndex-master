package home.smart.fly.zhihuindex.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.HttpUtil;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import home.smart.fly.zhihuindex.R;
import home.smart.fly.zhihuindex.Problem;
import home.smart.fly.zhihuindex.adapter.AnswerAdapter;
import home.smart.fly.zhihuindex.adapter.IndexRecyclerViewAdapter;
import home.smart.fly.zhihuindex.util.GsonTool;

/**
 * Created by zl on 2017/5/9.
 */

public class AnswerActivity extends AppCompatActivity {
    private final int UPDATE =1;

    private Context mContext;
    private View view;
    private TextView pTitle;
    private TextView pContent;
    private EditText messageText;
    private Button sendButton; 
    private LinearLayout message;
    //标题，内容
    private Problem problem;
    //被评论
    private Problem commentReferenced;
    //评论
    private Problem comment;
    private List<Problem> answerList = new ArrayList<Problem>();
    private AnswerAdapter adapter;
    private RecyclerView recyclerView;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                //UI更新
                case UPDATE:
                    adapter.notifyDataSetChanged();
                    msg.what=0;
                    Toast.makeText(AnswerActivity.this, "已发表评论", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        Bundle bundle = this.getIntent().getExtras();
        //获得数据
        problem = GsonTool.getProblem(bundle.getString("AnswerActivity"));
        Log.v("AnswerActivity1",bundle.getString("AnswerActivity"));
        mContext = this;
        initView();
    }

    private void initView() {
        view = LayoutInflater.from(this).inflate(R.layout.answer_head,null);
        pTitle = (TextView)view.findViewById(R.id.answer_title);
        Log.v("AnswerActivity2",problem.getPTitle());
        pTitle.setText(problem.getPTitle());
        pContent = (TextView)view.findViewById(R.id.answer_content);
        Log.v("AnswerActivity3",problem.getPContent());
        pContent.setText(problem.getPContent());
        message=(LinearLayout)findViewById(R.id.message);
        messageText=(EditText)findViewById(R.id.user_comment_msg);
        sendButton=(Button)findViewById(R.id.user_comment_send);
        //发送按钮的点击事件
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Answerjcsnsdnc","dcnsdlvlsdnvsdlvnsldvn");
                String str = messageText.getText().toString();
                if(str!=null){
                    update(str);
                    comment(false);
                }
                else Toast.makeText(AnswerActivity.this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
            }
        });
        //设置RecycleView
        recyclerView=(RecyclerView)findViewById(R.id.comment_list);
        LinearLayoutManager manager = new LinearLayoutManager(AnswerActivity.this);
        recyclerView.setLayoutManager(manager);
        //获取评论
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL("http://192.168.1.106:8080/HelloServer/servlet/ClickServlet");
                    answerList.clear();
                    answerList.add(problem);
                    String list = GsonTool.createListJsonString(answerList);
                    answerList = HttpUtil.sendWithHttp(url,list);
                    if(answerList!=null){
                        Log.v("AnswerActivity4",GsonTool.createListJsonString(answerList));
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        //获得adapter
        adapter = new AnswerAdapter(mContext,answerList);
        //设置
        recyclerView.setAdapter(adapter);
        //adapter的点击事件
        adapter.setItemClickListener(new IndexRecyclerViewAdapter.MyItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                        commentReferenced = answerList.get(position);
                        comment(true);
            }
        });
    }

    private void update(final String comment) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try{
                        URL url = new URL("http://192.168.1.107:8080/HelloServer/servlet/LoginServlet");
                        List<Problem> newProblemList = new ArrayList<Problem>();
                        newProblemList.add(commentReferenced);
                        Problem newComment = new Problem(1,null,comment,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),0,0,0,"happy",true);
                        newProblemList.add(newComment);
                        String list = GsonTool.createListJsonString(newProblemList);
                        newProblemList =  HttpUtil.sendWithHttp(url,list);
                        if(newProblemList!=null&&newProblemList.size()!=0){
                            Log.v("AnswerActivity",GsonTool.createListJsonString(newProblemList));
                            answerList.addAll(0,newProblemList);
                            Message message = new Message();
                            message.what =UPDATE;

                        }
                        else Toast.makeText(AnswerActivity.this,"无法发表评论",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
    }

    private void comment(boolean flag) {
        if(flag){
            message.setVisibility(View.VISIBLE);
            onFocusChange(flag);
        }
    }

    //调用输入法
    private void onFocusChange(boolean flag) {
        final boolean isFocus = flag;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    messageText.setFocusable(true);
                    messageText.requestFocus();
                } else {
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(messageText.getWindowToken(), 0);
                }
            }
        }, 100);
    }
}
