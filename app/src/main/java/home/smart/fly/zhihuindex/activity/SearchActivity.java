package home.smart.fly.zhihuindex.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import net.HttpUtil;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import home.smart.fly.zhihuindex.R;
import home.smart.fly.zhihuindex.database.RecordSQLiteOpenHelper;
import home.smart.fly.zhihuindex.util.GsonTool;
import home.smart.fly.zhihuindex.Problem;

/**
 * Created by zl on 2017/5/5.
 * 搜索界面
 */

public class SearchActivity extends AppCompatActivity {
    private TextView tv_tip;
    private TextView tv_clear;
    private ListView listview;
    private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);
    private SQLiteDatabase db;
    private BaseAdapter adapter;
    private SearchView searchView;
    private String str =null;
    private List<Problem> problemList = new ArrayList<Problem>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //初始化控件
        initView();

        //清空搜索历史
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
                queryData("");
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return true;
            }
        });
        //searchView的文本事件
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length()!=0){
                    //隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    str = query.trim();
                    boolean hasData = (hasData(str));
                    //如果搜索历史不存在，则存储
                    if(!hasData) insertData(str);
                    queryData(str);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                //参见IndexFragment
                                URL url = new URL("http://192.168.1.106:8080/HelloServer/servlet/SerachServlet");
                                Problem problem = new Problem(1,null,str,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),1,1,1,null,true);
                                List<Problem> newProblemList = new ArrayList<Problem>();
                                newProblemList.add(problem);
                                String list = GsonTool.createListJsonString(newProblemList);
                                Log.v("SearchActivity1",list);
                                //获得的答案列表
                                newProblemList = HttpUtil.sendWithHttp(url,list);
                                Log.v("SearchActivity2",GsonTool.createListJsonString(newProblemList));
                                if(newProblemList!=null&&newProblemList.size()> 0){
                                    Log.v("SearchActivity3",list);
                                    Intent intent = new Intent();
                                    String s = GsonTool.createListJsonString(newProblemList);
                                    //传送搜索结果
                                    Bundle bundle = new Bundle();
                                    bundle.putString("SearchActivity",s);
                                    intent.putExtras(bundle);
                                    intent.setClass(SearchActivity.this,AnswerListActivity.class);
                                    startActivity(intent);
                                }
                                else Toast.makeText(SearchActivity.this,"无法搜索！",Toast.LENGTH_SHORT).show();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else Toast.makeText(SearchActivity.this,"搜索内容不能为空",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //未输入时
                if (newText.toString().trim().length() == 0) tv_tip.setText("搜索历史");
                    //已输入
                else tv_tip.setText("搜索结果");
                String tempName = searchView.getQuery().toString();
                // 根据tempName去模糊查询数据库中有没有数据
                queryData(tempName);
                return false;
            }
        });

        //点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                searchView.setQuery(name,true);
            }
        });
        //长按事件
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确定删除？");
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                final String name = textView.getText().toString();
                //positiveButton
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db = helper.getWritableDatabase();
                        db.execSQL("delete from records(name) values('" +name+ "')");
                    }
                });
                //negativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                return false;
            }
        });
    }

    //插入数据
    private void insertData(String tempName){
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" +tempName+ "')");
        db.close();
    }
    //数据查询
    private void queryData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" +
                        tempName + "%' order by id desc ", null);
        // 创建adapter适配器对象
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[] { "name" },
                new int[] { android.R.id.text1 }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    //删除全部数据
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }
    //查找是否存在数据
    private boolean hasData(String tempName){
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    private void initView() {
            searchView=(SearchView)findViewById(R.id.search_view);
            tv_tip=(TextView)findViewById(R.id.tv_tip);
            tv_clear=(TextView)findViewById(R.id.tv_clear);
            listview=(ListView)findViewById(R.id.listView);
            listview.setTextFilterEnabled(true);
    }
}