package home.smart.fly.zhihuindex.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import home.smart.fly.zhihuindex.R;
import home.smart.fly.zhihuindex.util.ProblemJson;

/**
 * Created by zl on 2017/5/15.
 */

public class ProblemCommitActivity extends AppCompatActivity {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.ask_question:
                //弹出对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(ProblemCommitActivity.this);
                builder.setTitle("提交问题").setMessage("确认提交?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String problem = problem_editText.getText().toString();
                        ProblemJson problemJson = new ProblemJson();
                        problemJson.setContent(problem);
                        Gson gson = new Gson();
                        String commit = gson.toJson(problemJson);
                        //URL url = new URL("http://www.baidu.com");
                        //sendWithHttp(url,commit);
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
        }
        return true;
    }

    private void sendWithHttp(final URL urlAddress, final String jsonProblem){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection =null;
                URL url = urlAddress;
                try{
                     connection = (HttpURLConnection)url.openConnection();
                     connection.setRequestMethod("POST");
                     connection.setDoOutput(true);
                     connection.setRequestProperty("Charset", "UTF-8");
                     DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                     dos.writeBytes(jsonProblem);
                     dos.flush();
                     dos.close();
                     if(connection.getResponseCode()==200){
                         InputStream in = new BufferedInputStream(connection.getInputStream());
                         BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                         StringBuilder sb = new StringBuilder();
                         String response = null;
                         try {
                             while ((response = reader.readLine()) != null)
                                 sb.append(response + "/n");
                         } catch (IOException e) {
                             e.printStackTrace();
                         } finally {
                             try {
                                 in.close();
                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                         }
                     }
                }catch(Exception e){
                    e.printStackTrace();
                }
                finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
