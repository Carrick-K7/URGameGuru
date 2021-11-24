package com.example.urgameguru.my_game_detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.urgameguru.R;
import com.example.urgameguru.add_article.ArticlePageActivity;
import com.example.urgameguru.my_expo.AddGameDetailActivity;
import com.example.urgameguru.my_expo.Game;
import com.example.urgameguru.show_media.ShowImageActivity;
import com.example.urgameguru.show_article.ShowArticleActivity;
import com.example.urgameguru.game_detail.GameDetailActivity;
import com.example.urgameguru.show_media.ShowVideoActivity;
import com.example.urgameguru.upload_media.UploadMediaActivity;
import com.example.urgameguru.utils.GlideApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyGameDetailActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_game_detail);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        String name = getIntent().getStringExtra("name");

        findViewById(R.id.et_my_show_board).setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        findViewById(R.id.tv_add_article).setOnClickListener(v -> {
            Intent intent = new Intent(this, ArticlePageActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        });

        findViewById(R.id.ll_game_title).setOnClickListener(v -> {
            Intent intent = new Intent(this, GameDetailActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        });

        findViewById(R.id.bt_article_list).setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowArticleActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        });

        findViewById(R.id.tv_add_media).setOnClickListener(v -> {
            Intent intent = new Intent(this, UploadMediaActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        });

        findViewById(R.id.bt_screenshot_list).setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowImageActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        });

        findViewById(R.id.bt_clip_list).setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowVideoActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        });

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String ImagePath = "icon/" + name;
        StorageReference imageRef = storageRef.child(ImagePath);

        ImageView icon = findViewById(R.id.iv_my_game_detail_icon);
        GlideApp.with(this)
                .load(imageRef)
                .into(icon);

        TextView game_name = findViewById(R.id.tv_my_game_detail_name);
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}