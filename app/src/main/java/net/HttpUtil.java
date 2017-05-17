package net;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import home.smart.fly.zhihuindex.util.Problem;
import home.smart.fly.zhihuindex.util.StringUtil;

/**
 * Created by zl on 2017/5/16.
 */

public class HttpUtil {
       private  HttpURLConnection connection = null;
       private  URL url = null;
       private  InputStream in = null;
       private  BufferedReader bReader = null;
       private  String response;

       //发送请求，并获得一些数据
       public void sendWithHttp(final URL urlAddress, final String problem){
          new Thread(new Runnable() {
            @Override
            public void run() {
                url = urlAddress;
                try{
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Charset", "UTF-8");
                    DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                    dos.writeBytes(problem);
                    dos.flush();
                    dos.close();
                    //如果成功
                    if(connection.getResponseCode()==200){
                          getResponse();
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

        //获得数据
        public String getWithHttp(final URL urlAddress){
           new Thread(new Runnable() {
               @Override
               public void run() {
                   url = urlAddress;
                   try{
                       connection = (HttpURLConnection)url.openConnection();
                       connection.setRequestMethod("GET");
                       if(connection.getResponseCode()==200){
                          response=getResponse();
                       }
                   }catch(Exception e){
                       e.printStackTrace();
                   }
               }
           });
            return response;
        }

        private String getResponse(){
            String str =null;
            try{
                in = new BufferedInputStream(connection.getInputStream());
                str = StringUtil.StreamToString(in);
            }catch (Exception e){
                  e.printStackTrace();
            }
            return str;
        }
}
