package com.example.urgameguru.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.urgameguru.utils.FirstDrawListener;
import com.example.urgameguru.R;
import com.example.urgameguru.bean.Game;
import com.example.urgameguru.utils.GlideApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyGameDetailActivity extends Activity {
    private final Trace viewLoadTrace = FirebasePerformance.startTrace("MyGameDetailActivity-LoadTime");

    TextView publisher, developer, release, mode;
    TextView game_name;
    ImageView icon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_game_detail);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        View mainView = findViewById(android.R.id.content);
        String name = getIntent().getStringExtra("name");

        // Hide keyboard when the edittext lose focus
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

        icon = findViewById(R.id.iv_my_game_detail_icon);
        GlideApp.with(this)
                .load(imageRef)
                .into(icon);

        game_name = findViewById(R.id.tv_my_game_detail_name);
        game_name.setText(name);

        publisher = findViewById(R.id.tv_publisher);
        developer = findViewById(R.id.tv_developer);
        release = findViewById(R.id.tv_release);
        mode = findViewById(R.id.tv_mode);

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

        FirstDrawListener.registerFirstDrawListener(mainView, new FirstDrawListener.OnFirstDrawCallback() {
            @Override
            public void onDrawingStart() {}

            @Override
            public void onDrawingFinish() {
                viewLoadTrace.stop();
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}