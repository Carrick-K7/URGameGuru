package com.example.urgameguru.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.urgameguru.R;
import com.example.urgameguru.bean.Article;

import java.util.List;

public class ShowReviewAdapter extends RecyclerView.Adapter<ShowReviewAdapter.ViewHolder> {

    private static final String TAG = "showReviewAdapter";

    private final List<Article> mData;
    private final LayoutInflater mInflater;
    private final Context mContext;

    private ItemClickListener mClickListener;
    private TextView mArticleName;

    public ShowReviewAdapter(Context context, List<Article> data) {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.viewholder_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowReviewAdapter.ViewHolder holder, int position) {
        mArticleName = holder.itemView.findViewById(R.id.tv_item);
        Article article = mData.get(position);
        Log.d(TAG, article.toString());
        mArticleName.setText(article.getArticleName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public Article getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }

}
