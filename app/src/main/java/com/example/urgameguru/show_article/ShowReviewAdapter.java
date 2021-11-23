package com.example.urgameguru.show_article;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.urgameguru.R;
import com.example.urgameguru.bean.Article;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ShowReviewAdapter extends RecyclerView.Adapter<ShowReviewAdapter.ViewHolder> {

    private static final String TAG = "showReviewAdapter";

    private final List<Article> mData;
    private final LayoutInflater mInflater;
    private final Context mContext;

    private ItemClickListener mClickListener;
    private TextView mTextView;

    ShowReviewAdapter(Context context, List<Article> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

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
        mTextView = holder.itemView.findViewById(R.id.tv_item);
        Article article = mData.get(position);
        Log.d(TAG, article.toString());
        // TODO Handle article object
        mTextView.setText(article.getArticleName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    Article getItem(int id) {
        return mData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }

}
