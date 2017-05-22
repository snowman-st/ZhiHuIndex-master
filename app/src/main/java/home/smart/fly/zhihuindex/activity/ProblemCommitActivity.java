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
import java.util.Date;

import home.smart.fly.zhihuindex.R;
import home.smart.fly.zhihuindex.util.GsonTool;
import home.smart.fly.zhihuindex.util.Problem;

/**
 * Created by zl on 2017/5/15.
 */

public class ProblemCommitActivity extends AppCompatActivity{
    private EditText problem_editText;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.problem_commit);
        initView();
        setSupportActionBar(toolbar);
    }

    private void initView() {
        problem_editText=(EditText)findViewById(R.id.problem);
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
                        String problemContent = problem_editText.getText().toString();
                        //转换为Json
                        Problem problem = new Problem(0,problemContent,null,new Date(),0,0,0,null,true);
                        String commit = GsonTool.createJsonString(problem);
                        Log.v("ProblemCommitActivity",commit);
                        try{
                            URL url = new URL("http://192.168.1.107:8080/HelloServer/servlet/LoginServlet");
                            HttpUtil httpUtil = new HttpUtil();
                            httpUtil.sendWithHttp(url,commit);
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
