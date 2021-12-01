package com.example.urgameguru.ui.gurus;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MostRecentAdapter extends RecyclerView.Adapter<MostRecentAdapter.ViewHolder>{

    public List<String> mData;
    public String[] game_rank;
    public LayoutInflater mInflater;
    private MostRecentAdapter.ItemClickListener mClickListener;
    private final Context mContext;


    // data is passed into the constructor
    MostRecentAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public MostRecentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_holder_most_popular, parent, false);
        return new MostRecentAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull MostRecentAdapter.ViewHolder holder, int position) {
        String name = mData.get(position);
        holder.myTextView.setText(name);

        holder.rank.setText(game_rank[position]);

        // get icon from database
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String IconPath = "icon/" + name;
        StorageReference iconRef = storageRef.child(IconPath);

        Glide.with(mContext)
                .load(iconRef)
                .into(holder.gameIcon);

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
        TextView rank;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tv_most_popular_name);
            gameIcon = itemView.findViewById(R.id.iv_most_popular_icon);
            rank = itemView.findViewById(R.id.tv_most_popular_rank);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick2(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(MostRecentAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick2(View view, int position);
    }
}