package home.smart.fly.zhihuindex.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import home.smart.fly.zhihuindex.Constant;
import home.smart.fly.zhihuindex.R;
import home.smart.fly.zhihuindex.Problem;
import home.smart.fly.zhihuindex.util.ScreenUtil;
import home.smart.fly.zhihuindex.widget.ListItemMenu;

/**
 * Created by co-mall on 2016/9/13.
 */
public class IndexRecyclerViewAdapter extends RecyclerView.Adapter<IndexRecyclerViewAdapter.MyViewHolder>{
    //用于区别HeadView
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    //设置HeadView以匹配
    private View headView;

    private List<Problem> problemList = new ArrayList<Problem>();
    private Context mContext;

    private int menuW, menuH;

    private MyItemClickListener mItemClickListener;

    public IndexRecyclerViewAdapter(Context mContext, List<Problem> newProblems) {
        this.problemList = newProblems;
        this.mContext = mContext;
        DisplayMetrics display = new DisplayMetrics();
        Activity mActivity = (Activity) mContext;
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(display);
        //用于显示浮动按钮
        menuW = display.widthPixels / 2;
        menuH = LinearLayout.LayoutParams.WRAP_CONTENT;
    }

    //自定义点击接口
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
    //创建ViewHolder实例
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headView != null && viewType == TYPE_HEADER) {
            return new MyViewHolder(headView,mItemClickListener);
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.index_list_item, null);
        MyViewHolder holder = new MyViewHolder(view,mItemClickListener);
        return holder;
    }

    //加载绑定数据
    //用于对RecycleView数据进行赋值
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            return;
        }
        final int pos = getRealPosition(holder);
        //加载图片
        Glide.with(mContext).load(Constant.headPics.get(pos % 3)).placeholder(R.drawable.profile).into(holder.profile_pic);
        //menu点击事件
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListItemMenu menu = new ListItemMenu(menuW, menuH, mContext);
                menu.update();
                //用于显示悬浮按钮
                int offx = ScreenUtil.dip2px(mContext, 24);
                int offy = ScreenUtil.dip2px(mContext, 24);
                menu.setAnimationStyle(R.style.MenuAnim);
                menu.showAsDropDown(holder.menu, -menuW + offx, -offy);
            }
        });
        //加载
        Problem problem = problemList.get(getRealPosition(holder));
        holder.problemTitleText.setText(problem.getPTitle());
        holder.problemContentText.setText(problem.getPContent());
    }

    @Override
    public int getItemViewType(int position) {
        if (headView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return headView == null ? problemList.size() : problemList.size() + 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private MyItemClickListener mListener;
        //标题
        TextView problemTitleText;
        //内容
        TextView problemContentText;
        //menu
        ImageView menu;
        //头像
        CircleImageView profile_pic;
        LinearLayout normalShell;

        public MyViewHolder(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            if (itemView == headView) return;
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
            problemTitleText = (TextView) itemView.findViewById(R.id.recommend_problem_title);
            menu = (ImageView) itemView.findViewById(R.id.answer_menu);
            profile_pic = (CircleImageView) itemView.findViewById(R.id.answer_profile_image);
            problemContentText=(TextView)itemView.findViewById(R.id.recommend_problem_content);
            normalShell = (LinearLayout) itemView.findViewById(R.id.normalList);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getAdapterPosition()-1);
            }
        }
    }

    public void setHeadView(View view) {
        headView = view;
        notifyItemInserted(0);
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int pos = holder.getLayoutPosition();
        return headView == null ? pos : pos - 1;
    }
}
