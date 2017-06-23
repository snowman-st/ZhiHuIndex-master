package home.smart.fly.zhihuindex.settings;

import android.app.Application;

import home.smart.fly.zhihuindex.User;

/**
 * Created by snowman on 2017/6/3.
 */

public class UserInf extends Application {
    private static int loginState = 0;                               //表示处于未登录状态
    private User user = new User();
    public int getLoginState(){
        return loginState;
    }
    public User getUser(){
        return user;
    }
    public void setLoginState(int g){
        loginState = g;
    }
    public void setUser(User u){
        this.user = u;
    }
    public void onCreate(){
        super.onCreate();
    }
}
