package com.example.urgameguru.show_media;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.urgameguru.R;

import java.util.List;

public class ShowMediaAdapter extends RecyclerView.Adapter<ShowMediaAdapter.ViewHolder> {
    private final List<String> mData;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private ItemClickListener mClickListener;

    ShowMediaAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextView;
        ImageView mImageView;

        ViewHolder(View v){
            super(v);
            mTextView = v.findViewById(R.id.tvTest);
            mImageView = v.findViewById(R.id.ivTest);
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
        View view = mInflater.inflate(R.layout.viewholder_add_media, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowMediaAdapter.ViewHolder holder, int position) {
        String url = mData.get(position);
        Glide.with(mContext).load(url).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    String getItem(int id) {
        return mData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }

}
