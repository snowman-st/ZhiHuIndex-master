package home.smart.fly.zhihuindex;

/**
 * Created by zl on 2017/5/16.
 */

public class Problem {
    //用户节点
    private int belongUserId;
    //问题内容
    private String pContent;
    //问题日期
    private String pDate;
    //问题的Id
    private int pId;
    //问题标题
    private String pTitle;
    //问题类型
    private String pType;
    //父节点
    private int parentId;
    //标志
    private boolean refresh_flag;
    //根节点
    private int rootId;
    public Problem(int pId,String pTitle, String content, String pDate, int parentId, int rootId, int belongUserId, String pType, boolean refresh_flag){
           this.pId = pId;
           this.pTitle=pTitle;
           this.pContent=content;
           this.pDate =pDate;
           this.parentId =parentId;
           this.rootId =rootId;
           this.pType=pType;
           this.belongUserId =belongUserId;
           this.refresh_flag=refresh_flag;
    }

    public int getQId(){
        return pId;
    }

    public void setQId(int qId){
        this.pId =qId;
    }

    public String getPContent(){
        return pContent;
    }

    public void setPContent(String content){
        this.pContent=content;
    }

    public String getPTitle(){
        return pTitle;
    }

    public void setPTitle(String pTitle){
        this.pTitle =  pTitle;
    }

    public String getpDate(){
        return pDate;
    }

    public void setPDate(String pDate){this.pDate =pDate;}

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
