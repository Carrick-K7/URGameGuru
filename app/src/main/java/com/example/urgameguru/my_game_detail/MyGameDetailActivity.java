package com.example.urgameguru.my_game_detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;

import com.example.urgameguru.R;
import com.example.urgameguru.add_article.ArticlePageActivity;
import com.example.urgameguru.show_media.ShowImageActivity;
import com.example.urgameguru.show_article.ShowArticleActivity;
import com.example.urgameguru.game_detail.GameDetailActivity;
import com.example.urgameguru.show_media.ShowVideoActivity;
import com.example.urgameguru.upload_media.UploadMediaActivity;

public class MyGameDetailActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_game_detail);

        findViewById(R.id.et_my_show_board).setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        findViewById(R.id.tv_add_article).setOnClickListener(v -> {
            Intent intent = new Intent(this, ArticlePageActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.ll_game_title).setOnClickListener(v -> {
            Intent intent = new Intent(this, GameDetailActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.tv_more_articles).setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowArticleActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.tv_add_media).setOnClickListener(v -> {
            Intent intent = new Intent(this, UploadMediaActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.tv_screenshot_list).setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowImageActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.tv_clip_list).setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowVideoActivity.class);
            startActivity(intent);
        });


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}