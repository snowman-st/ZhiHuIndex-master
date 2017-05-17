package home.smart.fly.zhihuindex.util;

import java.util.Date;

/**
 * Created by zl on 2017/5/16.
 */

public class Problem {
    //问题的Id
    private int qId;
    //问题内容
    private String qContent;
    //问题标题
    private String pTitle;
    //问题日期
    private Date pDate;
    //父节点
    private int parentId;
    //根节点
    private int rootId;
    //用户节点
    private int belongUserId;
    //问题类型
    private String pType;
    //标志
    private boolean refresh_flag;
    public Problem(int qId, String content, String pTitle,Date pDate, int parentId, int rootId, int userId, String pType, boolean flag){
           this.qId =qId;
           this.qContent=content;
           this.pTitle=pTitle;
           this.pDate =pDate;
           this.parentId =parentId;
           this.rootId =rootId;
           this.belongUserId =userId;
           this.refresh_flag=flag;
    }


    public int getQId(){
        return qId;
    }

    public void setQId(int qId){
        this.qId =qId;
    }

    public String getPContent(){
        return qContent;
    }

    public void setPContent(String content){
        this.qContent=content;
    }

    public String getPTitle(){
        return pTitle;
    }

    public void setPTitle(String pTitle){
        this.pTitle =  pTitle;
    }

    public Date getpDate(){
        return pDate;
    }

    public void setPDate(Date pDate){this.pDate =pDate;}

    public int getParentId(){
        return parentId;
    }

    public void setParentId(int parentId){
        this.parentId = parentId;
    }

    public int getRootId(){
        return rootId;
    }

    public void setRootId(int rootId){
        this.rootId =rootId;
    }

    public int getUserId(){
        return belongUserId;
    }

    public void setUserId(int userId){
        this.belongUserId =userId;
    }

    public String getPType(){
        return pType;
    }

    public void setpType(String pType){
        this.pType=pType;
    }

    public boolean getRefresh_Flag(){
        return refresh_flag;
    }

    public void setRefresh_Flag(boolean refresh_flag){
        this.refresh_flag=refresh_flag;
    }
}
