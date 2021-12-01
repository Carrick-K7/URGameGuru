package com.example.urgameguru.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urgameguru.R;
import com.example.urgameguru.bean.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private static final String TAG = "commentAdapter";

    private final List<Comment> mData;
    private final LayoutInflater mInflater;
    private final Context mContext;

    private ShowReviewAdapter.ItemClickListener mClickListener;
    private TextView tvUserName, tvCommentDate, tvCommentContent;

    public CommentAdapter(Context context, List<Comment> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // Create a view holder class and setup the onclick listener
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewHolder(View v){
            super(v);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.viewholder_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        tvCommentContent = holder.itemView.findViewById(R.id.tv_comment_content);
        tvCommentDate = holder.itemView.findViewById(R.id.tv_comment_date);
        tvUserName = holder.itemView.findViewById(R.id.tv_comment_user_name);

        Comment comment = mData.get(position);
        tvUserName.setText(comment.getUserName());
        tvCommentDate.setText(comment.getCommentDate());
        tvCommentContent.setText(comment.getCommentText());

        // Set rating icons
        int rating = Integer.parseInt(comment.getCommentRate());
        ImageView iv_star_1 = holder.itemView.findViewById(R.id.comment_star_1);
        ImageView iv_star_2 = holder.itemView.findViewById(R.id.comment_star_2);
        ImageView iv_star_3 = holder.itemView.findViewById(R.id.comment_star_3);
        ImageView iv_star_4 = holder.itemView.findViewById(R.id.comment_star_4);
        ImageView iv_star_5 = holder.itemView.findViewById(R.id.comment_star_5);
        if (rating < 5) {iv_star_5.setImageResource(R.drawable.star_empty);}
        if (rating < 4) {iv_star_4.setImageResource(R.drawable.star_empty);}
        if (rating < 3) {iv_star_3.setImageResource(R.drawable.star_empty);}
        if (rating < 2) {iv_star_2.setImageResource(R.drawable.star_empty);}
        if (rating < 1) {iv_star_1.setImageResource(R.drawable.star_empty);}
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    Comment getItem(int id) {
        return mData.get(id);
    }

    void setClickListener(ShowReviewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }


}
