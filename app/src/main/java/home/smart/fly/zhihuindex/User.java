package home.smart.fly.zhihuindex;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by snowman on 2017/5/26.
 */

public class User {
    private String username;                //用户名
    private String password;                //用户密码
    private String answer;                  //密保问题的答案
    private int userID;                     //用户ID
    private int imageID;                    //头像id
    private int mark;                       //积分
    public static int login = 0;                  //是否登录
    public User(){
        
    }
    public User(String u,String p,String a){
        this.username = u;
        this.password = p;
        this.answer = a;
        this.mark = 0;
    }

    public int getUserID(){
        return this.userID;
    }
    public int getMark(){
        return this.mark;
    }
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public String getAnswer(){
        return this.answer;
    }


    public void setUserID(int id){
        this.userID = id;
    }
    public void setUsername(String u){
        this.username = u;
    }
    public void setPassword(String p){
        this.password = p;
    }
    public void setAnswer(String a){
        this.answer = a;
    }
    public void setImageID(int i){
        this.imageID = i;
    }
    public void setMark(int mark){
        this.mark = mark;
    }
    public void setLogin(int login1){login = login1;}
}
