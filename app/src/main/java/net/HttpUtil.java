package net;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import home.smart.fly.zhihuindex.util.GsonTool;
import home.smart.fly.zhihuindex.Problem;
import home.smart.fly.zhihuindex.util.StringUtil;
import home.smart.fly.zhihuindex.User;

/**
 * Created by zl on 2017/5/16.
 */

public class HttpUtil {
    private static InputStream in = null;
    private static List<Problem> problemList=null;
    private static List<User> userList = null;
    private static HttpURLConnection connection = null;

    //发送post请求，并获得一些数据
    public static List<Problem> sendWithHttp(final URL urlAddress,final String problemJson){
          new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = urlAddress;
                try{
                    String problemStr = URLEncoder.encode(problemJson,"UTF-8");
                    //打开链接
                    connection = (HttpURLConnection)url.openConnection();
                    //POST请求
                    connection.setRequestMethod("POST");
                    //允许此链接输入输出
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestProperty("Accept-Charset", "utf-8");
                    connection.setRequestProperty("contentType", "utf-8");
                    connection.connect();
                    //获得输出流
                    DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                    //传送Json数据
                    Log.v("HttpUtil1",problemStr);
                    Log.v("HttpUtil1", URLDecoder.decode(problemStr,"UTF-8"));
                    dos.writeBytes(problemStr);
                    dos.flush();
                    dos.close();
                    //如果成功
                    if(connection.getResponseCode()==200){
                          String str =getResponse();
                        Log.v("HttpUtil123",str);
                          //获得返回数据
                          problemList = GsonTool.getList(str);
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
           return problemList;
       }
        public static List<User> sendUsernode(final URL urlAddress,final String userJson){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    URL url = urlAddress;
                    try{
                        String userStr = URLEncoder.encode(userJson,"UTF-8");
                        //打开链接
                        connection = (HttpURLConnection)url.openConnection();
                        //POST请求
                        connection.setRequestMethod("POST");
                        //允许此链接输入输出
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.setRequestProperty("Charset", "UTF-8");
                        connection.connect();
                        //获得输出流
                        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                        //传送Json数据
                        dos.writeBytes(userJson);
                        dos.flush();
                        dos.close();
                        //如果成功
                        if(connection.getResponseCode()==200){
                           String str =getResponse();
                            //获得返回数据
                            userList = GsonTool.getuList(str);
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
            return userList;
                }

        private static String getResponse(){
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
