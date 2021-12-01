package com.example.urgameguru.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urgameguru.R;
import com.example.urgameguru.bean.Article;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private static final String TAG = "articleAdapter";

    private final List<Article> mData;
    private final LayoutInflater mInflater;
    private final Context mContext;

    private ArticleAdapter.ItemClickListener mClickListener;
    private TextView tvArticleName, tvUserName;

    public ArticleAdapter(Context context, List<Article> data) {
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
    public ArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.viewholder_review_game_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleAdapter.ViewHolder holder, int position) {
        tvArticleName = holder.itemView.findViewById(R.id.tv_review_name);
        tvUserName = holder.itemView.findViewById(R.id.tv_review_user_name);
        Article article = mData.get(position);
        tvArticleName.setText(article.getArticleName());
        tvUserName.setText(article.getUserName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public Article getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ArticleAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }
}
