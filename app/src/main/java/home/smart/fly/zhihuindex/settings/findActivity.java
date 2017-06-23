package home.smart.fly.zhihuindex.settings;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import net.HttpUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import home.smart.fly.zhihuindex.R;
import home.smart.fly.zhihuindex.User;
import home.smart.fly.zhihuindex.util.GsonTool;

/**
 * Created by snowman on 2017/5/26.
 */

public class findActivity extends Activity {

    private String strUser;
    private String strProve;
    private String answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpwd);
        setTitle("找回密码");
        Intent intent = getIntent();
        strUser = intent.getStringExtra("logname");



       final TextView tcue = (TextView) findViewById(R.id.cue);
        //提示信息，初始显示为：请回答密保问题；后来显示为：”你的密码是：“
        final EditText prove = (EditText) findViewById(R.id.provequestion);
        Button btsub = (Button) findViewById(R.id.submit_answer);

        btsub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                strProve = prove.getText().toString();
                //根据strUser去数据库中查找相应的答案，然后比较
                Intent lastintent = getIntent();
                strUser = lastintent.getStringExtra("logname");
                User retuser = new User();
                List<User> retUList = new ArrayList<User>();
                retuser.setUsername(strUser);
                retUList.add(retuser);
                String strjson = GsonTool.createUserJsonList(retUList);
                try{
                    URL url = new URL("http://192.168.1.107:8080/HelloServer/servlet/LServlet");
                    retUList = HttpUtil.sendUsernode(url,strjson);
                    retuser = retUList.get(0);
                }catch(Exception e){
                    e.printStackTrace();
                }

                answer = retuser.getAnswer();
                Log.v("findActivity","huahhua");
                Log.v("findActivity",answer);
                if(!strProve.equals(answer)){
                    Toast.makeText(findActivity.this,"你的答案有误！",Toast.LENGTH_SHORT).show();
                }
                else{
                    tcue.setText("你的密码是：");
                    prove.setText(retuser.getPassword());

                }
            }
        });

    }

}
