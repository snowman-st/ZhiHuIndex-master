package home.smart.fly.zhihuindex.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.HttpUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import home.smart.fly.zhihuindex.*;

import home.smart.fly.zhihuindex.R;
import home.smart.fly.zhihuindex.util.GsonTool;

/**
 * Created by snowman on 2017/5/23.
 */

public class registerActivity extends Activity {
    private boolean isNotified = false;
    private int plcFlag = 0;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.register);

        setTitle("注册");
        //获取控件
        final EditText username = (EditText)findViewById(R.id.username);
        final EditText pwd = (EditText)findViewById(R.id.pwd);
        final EditText pwdrp = (EditText)findViewById(R.id.pwdrp);
        final EditText mail = (EditText)findViewById(R.id.mail);
        final Button register = (Button) findViewById(R.id.register);
        final Button reset = (Button) findViewById(R.id.reset);
        //事件监听
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUser = username.getText().toString();
                String strPwd =  pwd.getText().toString();
                String strPwdRp = pwdrp.getText().toString();
                String strMail = mail.getText().toString();
                if(strUser.equals("")){
                    Toast.makeText(registerActivity.this,"用户名不能为空！",
                            Toast.LENGTH_SHORT).show();

                }
                if(!strPwd.equals(strPwdRp)){
                    Toast.makeText(registerActivity.this,"两次输入的密码不一致！",
                            Toast.LENGTH_SHORT).show();

                }
                if(strMail.equals("")){
                    Toast.makeText(registerActivity.this,"密保不能为空！",
                            Toast.LENGTH_SHORT).show();

                }
                //添加一个查询数据库用户名的操作
                regist(strUser,strPwd,strMail);
                toLogin();

            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username.setText("");
                pwd.setText("");
                pwdrp.setText("");
                mail.setText("");
            }
        });


    }
    private void toLogin(){
        this.finish();
    }
    public void regist(String uname,String pwd,String ans){
        User user = new User(uname,pwd,ans);
        List<User> usList = new ArrayList<User>();
        usList.add(user);
        String jsonUser = GsonTool.createUserJsonList(usList);
        Log.v("registerActivity",jsonUser);
        try{
            URL url =new URL("http://192.168.1.107:8080/HelloServer/servlet/RServlet");
            HttpUtil.sendUsernode(url,jsonUser);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
