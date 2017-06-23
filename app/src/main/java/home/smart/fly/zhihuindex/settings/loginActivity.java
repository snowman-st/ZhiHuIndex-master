package home.smart.fly.zhihuindex.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import net.HttpUtil;

import java.net.URL;

import home.smart.fly.zhihuindex.R;
import home.smart.fly.zhihuindex.User;
import home.smart.fly.zhihuindex.util.GsonTool;
import home.smart.fly.zhihuindex.activity.MainActivity;

/**
 * Created by snowman on 2017/5/23.
 */

public class loginActivity extends AppCompatActivity{
    String strAccount;
    String strPwd;
    private SharedPreferences sp = null;

    final UserInf useapp = (UserInf)getApplication();
    public void onCreate(Bundle saveinstanceState){

        super.onCreate(saveinstanceState);
        Log.v("loginActivity","snomman");
        setContentView(R.layout.login);

        setTitle("登录");

        final EditText account = (EditText)findViewById(R.id.account);
        final EditText passwd = (EditText)findViewById(R.id.password);
        final TextView fpwd = (TextView)findViewById(R.id.findpawd);
        final Button log = (Button) findViewById(R.id.login);
        final Button res = (Button)findViewById(R.id.regist);

       /* sp = this.getSharedPreferences("userInfo",MODE_PRIVATE);
        account.setText(sp.getString("username",""));
        passwd.setText(sp.getString("passwd",""));
        //实现自动登录
       if(!account.getText().toString().equals("")&&!passwd.getText().toString().equals("")){
            loginto(account.getText().toString(),passwd.getText().toString());

        }*/

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strAccount = account.getText().toString();
                strPwd = passwd.getText().toString();
                loginto(strAccount, strPwd);
            }
        });
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReg = new Intent(loginActivity.this,registerActivity.class);
                startActivity(intentReg);
            }
        });
        fpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.this,findActivity.class);
                intent.putExtra("logname",account.getText().toString());
                startActivity(intent);
            }
        });

    }
    private void loginto(String uname,String passwd){
        User retuser = new User();
        List<User> retUList = new ArrayList<User>();
        List<User> ret = new ArrayList<User>();
        retuser.setUsername(uname);
        retuser.setPassword(passwd);
        retUList.add(retuser);
        String strjson = GsonTool.createUserJsonList(retUList);
        try{
            URL url = new URL("http://192.168.1.107:8080/HelloServer/servlet/LServlet");
            ret = HttpUtil.sendUsernode(url,strjson);
            retuser = ret.get(0);
        }catch(Exception e){
            e.printStackTrace();
        }


        if(retuser.getPassword().equals(passwd)){

//            SharedPreferences.Editor edit = sp.edit();
//            edit.putString("loginstate","1");
////            edit.putString("username",strAccount);
////            edit.putString("passwd",strPwd);
//           edit.commit();
            MainActivity.logstate = true;
            Toast.makeText(loginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else{
            Toast.makeText(loginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
        }
    }
}
