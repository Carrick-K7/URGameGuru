package com.example.urgameguru.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.urgameguru.R;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ShowImageAdapter extends RecyclerView.Adapter<ShowImageAdapter.ViewHolder> {

    private static final String TAG = "showImageAdapter";

    private final List<StorageReference> mData;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private ItemClickListener mClickListener;

    public ShowImageAdapter(Context context, List<StorageReference> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // Create a view holder class and setup the onclick listener
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mImageView;

        ViewHolder(View v){
            super(v);
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
        View view = mInflater.inflate(R.layout.viewholder_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowImageAdapter.ViewHolder holder, int position) {
        StorageReference reference = mData.get(position);
        Log.d(TAG, reference.toString());

        // Download the video from database and load it into imageview using Glide
        reference.getMetadata().addOnSuccessListener(storageMetadata -> {
            String type = storageMetadata.getContentType();
            if (type.startsWith("image")) {
                Glide.with(mContext)
                        .load(reference)
                        .into(holder.mImageView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public StorageReference getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }

}
