package home.smart.fly.zhihuindex.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import net.HttpUtil;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import home.smart.fly.zhihuindex.R;
import home.smart.fly.zhihuindex.util.GsonTool;
import home.smart.fly.zhihuindex.Problem;

/**
 * Created by zl on 2017/5/15.
 */

public class ProblemCommitActivity extends AppCompatActivity{
    private EditText problemContentEditText;
    private EditText problemTitleEditText;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_commit);
        initView();
        setSupportActionBar(toolbar);
    }

    private void initView() {
        problemContentEditText=(EditText)findViewById(R.id.problem_content);
        problemTitleEditText=(EditText)findViewById(R.id.problem_title);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_commit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.problem_commit:
                //弹出对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(ProblemCommitActivity.this);
                builder.setTitle("提交问题").setMessage("确认提交?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //获得内容
                        String pContent = problemContentEditText.getText().toString();
                        String pTitle = problemTitleEditText.getText().toString();
                        //转换为Json
                        Problem problem = new Problem(0,pContent,pTitle,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),0,0,1,null,false);
                        List<Problem> problemList = new ArrayList<Problem>();
                        problemList.add(problem);
                        String commit = GsonTool.createListJsonString(problemList);
                        Log.v("ProblemCommitActivity",commit);
                        try{
                            URL url = new URL("http://192.168.1.107:8080/HelloServer/servlet/LoginServlet");
                            HttpUtil.sendWithHttp(url,commit);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
        }
        return true;
    }
}
