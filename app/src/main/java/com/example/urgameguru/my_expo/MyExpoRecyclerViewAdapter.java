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
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String ImagePath = "icon/" + name;
        StorageReference imageRef = storageRef.child(ImagePath);

        imageRef.getMetadata().addOnSuccessListener(storageMetadata -> {
            String type = storageMetadata.getContentType();
            if (type.startsWith("image")) {
                Glide.with(mContext)
                        .load(imageRef)
                        .into(holder.gameIcon);
            }
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

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tv_my_expo_game_name);
            gameIcon = itemView.findViewById(R.id.iv_my_expo_game_icon);
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
