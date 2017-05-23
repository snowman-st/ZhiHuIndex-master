package home.smart.fly.zhihuindex.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zl on 2017/5/16.
 */

public class StringUtil {
    //将输入流转化为String
    public static String StreamToString(InputStream in){
        String str =null;
        StringBuilder sb = new StringBuilder();
        BufferedReader bReader = new BufferedReader(new InputStreamReader(in));
        try{
            while((str=bReader.readLine())!=null)
                 sb.append(str+"/n");
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                in.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
