package com.example.urgameguru.my_game_detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.urgameguru.R;
import com.example.urgameguru.add_article.AddArticleActivity;
import com.example.urgameguru.show_media.ShowMediaActivity;
import com.example.urgameguru.article_list.ArticleListActivity;
import com.example.urgameguru.game_detail.GameDetailActivity;
import com.example.urgameguru.upload_media.UploadMediaActivity;

public class MyGameDetailActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_game_detail);

        EditText etMyShowBoard = findViewById(R.id.et_my_show_board);
        etMyShowBoard.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        ImageView ivAddArticle = findViewById(R.id.add_articles);
        ivAddArticle.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddArticleActivity.class);
            startActivity(intent);
        });

        LinearLayout llTitle = findViewById(R.id.wild_rift_title);
        llTitle.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameDetailActivity.class);
            startActivity(intent);
        });

        TextView tvMoreArticles = findViewById(R.id.more_articles);
        tvMoreArticles.setOnClickListener(v -> {
            Intent intent = new Intent(this, ArticleListActivity.class);
            startActivity(intent);
        });

        ImageView ivAddMedia = findViewById(R.id.add_media);
        ivAddMedia.setOnClickListener(v -> {
            Intent intent = new Intent(this, UploadMediaActivity.class);
            startActivity(intent);
        });

        TextView tvShowMedia = findViewById(R.id.tv_screenshot1);
        tvShowMedia.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowMediaActivity.class);
            startActivity(intent);
        });


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}