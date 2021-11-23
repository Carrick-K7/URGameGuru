package com.example.urgameguru.game_detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.urgameguru.R;
import com.example.urgameguru.show_article.ShowArticleActivity;
import com.example.urgameguru.comment_list.CommentListActivity;

public class GameDetailActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

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
    }
}