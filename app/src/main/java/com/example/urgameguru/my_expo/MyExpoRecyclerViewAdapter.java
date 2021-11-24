package com.example.urgameguru.my_expo;

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
import com.example.urgameguru.show_media.ShowImageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MyExpoRecyclerViewAdapter extends RecyclerView.Adapter<MyExpoRecyclerViewAdapter.ViewHolder> {

    public List<String> mData;
    public LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private final Context mContext;


    // data is passed into the constructor
    MyExpoRecyclerViewAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.viewholder_my_expo_game, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = mData.get(position);
        holder.myTextView.setText(name);


        // get icon from database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String IconPath = "icon/" + name;
        StorageReference iconRef = storageRef.child(IconPath);

        Glide.with(mContext)
                .load(iconRef)
                .into(holder.gameIcon);

        // get resources counts from database
        StorageReference imageRef = storageRef.child("image").child(name).child(user.getUid());
        imageRef.listAll().addOnSuccessListener(taskSnapshot -> {
            String cnt_screenshots = Integer.toString(taskSnapshot.getItems().size());
            holder.tvCntScreenshots.setText(cnt_screenshots);
        });

        StorageReference videoRef = storageRef.child("video").child(name).child(user.getUid());
        videoRef.listAll().addOnSuccessListener(taskSnapshot -> {
            String cnt_clips = Integer.toString(taskSnapshot.getItems().size());
            holder.tvCntClips.setText(cnt_clips);
        });

        // TODO update ref when adding new article type
        StorageReference articleRef = storageRef.child("articles").child(name).child("review").child(user.getUid());
        articleRef.listAll().addOnSuccessListener(taskSnapshot -> {
            String cnt_reviews = Integer.toString(taskSnapshot.getItems().size());
            holder.tvCntArticles.setText(cnt_reviews);
        });

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView gameIcon;
        TextView tvCntScreenshots, tvCntClips, tvCntArticles;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tv_my_expo_game_name);
            gameIcon = itemView.findViewById(R.id.iv_my_expo_game_icon);

            tvCntScreenshots = itemView.findViewById(R.id.tv_cnt_screenshots);
            tvCntClips = itemView.findViewById(R.id.tv_cnt_clips);
            tvCntArticles = itemView.findViewById(R.id.tv_cnt_articles);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
