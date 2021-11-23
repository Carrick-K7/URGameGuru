package com.example.urgameguru.game_detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.urgameguru.R;
import com.example.urgameguru.my_expo.Game;
import com.example.urgameguru.show_article.ShowArticleActivity;
import com.example.urgameguru.comment_list.CommentListActivity;
import com.example.urgameguru.utils.GlideApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class GameDetailActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        String name = getIntent().getStringExtra("name");

        TextView tvMoreComments = findViewById(R.id.more_comments);
        tvMoreComments.setOnClickListener(v -> {
            Intent intent = new Intent(this, CommentListActivity.class);
            startActivity(intent);
        });

        TextView tvMoreArticles = findViewById(R.id.more_articles_all);
        tvMoreArticles.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowArticleActivity.class);
            startActivity(intent);
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String ImagePath = "icon/" + name;
        StorageReference imageRef = storageRef.child(ImagePath);

        ImageView icon = findViewById(R.id.iv_game_detail_icon);
        imageRef.getMetadata().addOnSuccessListener(storageMetadata -> {
            String type = storageMetadata.getContentType();
            if (type.startsWith("image")) {
                GlideApp.with(this)
                        .load(imageRef)
                        .into(icon);
            }
        });

        TextView game_name = findViewById(R.id.tv_game_detail_name);
        game_name.setText(name);

        TextView publisher = findViewById(R.id.tv_publisher);
        TextView developer = findViewById(R.id.tv_developer);
        TextView release = findViewById(R.id.tv_release);
        TextView mode = findViewById(R.id.tv_mode);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Game game = dataSnapshot.getValue(Game.class);
                developer.setText(game.developer);
                publisher.setText(game.publisher);
                release.setText(game.release);
                mode.setText(game.mode);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mDatabase.child("games").child(name).addListenerForSingleValueEvent(eventListener);
    }
}