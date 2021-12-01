package com.example.urgameguru.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.urgameguru.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;

import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;

import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ShowVideoAdapter extends RecyclerView.Adapter<ShowVideoAdapter.ViewHolder> {

    private static final String TAG = "showVideoAdapter";

    private final List<StorageReference> mData;
    private final LayoutInflater mInflater;
    private final Context mContext;

    private ItemClickListener mClickListener;
    private PlayerView mPlayerView;

    public ShowVideoAdapter(Context context, List<StorageReference> data) {
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
        View view = mInflater.inflate(R.layout.viewholder_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowVideoAdapter.ViewHolder holder, int position) {
        StorageReference reference = mData.get(position);
        // Download the video from database and load it into exo player
        reference.getDownloadUrl().addOnSuccessListener(uri -> {
            try {
                mPlayerView = holder.itemView.findViewById(R.id.pv_item);
                ExoPlayer player = new ExoPlayer.Builder(mContext.getApplicationContext()).build();
                DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();
                MediaItem mediaItem = MediaItem.fromUri(uri);
                MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
                mPlayerView.setPlayer(player);
                player.setMediaSource(mediaSource);
                player.prepare();
                player.setPlayWhenReady(false);
            } catch (Exception e) {
                Log.e(TAG, "exoplayer error:" + e);
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

