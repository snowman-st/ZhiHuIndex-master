package home.smart.fly.zhihuindex.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.zhihuindex.R;
import home.smart.fly.zhihuindex.adapter.IndexRecyclerViewAdapter;
import home.smart.fly.zhihuindex.util.GsonTool;
import home.smart.fly.zhihuindex.Problem;

/**
 * Created by zl on 2017/5/11.
 */

public class AnswerListActivity extends AppCompatActivity {
    private Context mContext;
    private RecyclerView recyclerView;
    private IndexRecyclerViewAdapter adapter;

    private List<Problem> problemList = new ArrayList<Problem>();
    //存储问题的标题和具体内容（AnswerActivity需要）
    private String []problem = new String[2];
    //搜索结果
    private String jsonStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_list);
        Bundle bundle = this.getIntent().getExtras();
        jsonStr = bundle.getString("SearchActivity");
        initView();
    }

    private void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.answer_list_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        problemList = GsonTool.getList(jsonStr);
        adapter = new IndexRecyclerViewAdapter(this,problemList);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new IndexRecyclerViewAdapter.MyItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }
}
