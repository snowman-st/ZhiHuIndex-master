package home.smart.fly.zhihuindex.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.zhihuindex.Problem;
import home.smart.fly.zhihuindex.R;

/**
 * Created by zl on 2017/5/21.
 */

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.MyViewHolder> {
    private List<Problem> commentList = new ArrayList<Problem>();
    private Context mContext;
    private IndexRecyclerViewAdapter.MyItemClickListener mItemClickListener;
    private Problem problem;

    public void setItemClickListener(IndexRecyclerViewAdapter.MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }

    public AnswerAdapter(Context mContext, List<Problem> newProblems) {
        this.commentList = newProblems;
        this.mContext = mContext;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.answer_item, null);
        MyViewHolder holder = new MyViewHolder(view,mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
           problem = commentList.get(position);
           holder.answerContent.setText(problem.getPContent());
           holder.replyTime.setText(problem.getpDate());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
         TextView userReply;
         TextView userReplied;
         TextView answerContent;
         TextView replyTime;
        public MyViewHolder(View itemView,IndexRecyclerViewAdapter.MyItemClickListener myItemClickListener) {
              super(itemView);
              userReply = (TextView)itemView.findViewById(R.id.user_reply);
              userReplied = (TextView)itemView.findViewById(R.id.user_replied);
              answerContent = (TextView)itemView.findViewById(R.id.reply_content);
              replyTime = (TextView)itemView.findViewById(R.id.reply_time);
        }
        @Override
        public void onClick(View v) {

        }
    }
}
