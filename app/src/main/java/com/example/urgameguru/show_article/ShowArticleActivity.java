package com.example.urgameguru.show_article;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.urgameguru.R;
import com.example.urgameguru.add_article.ArticlePageActivity;
import com.example.urgameguru.bean.Article;
import com.example.urgameguru.utils.GlideApp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ShowArticleActivity extends AppCompatActivity implements ShowReviewAdapter.ItemClickListener {

    private static final String TAG = "showArticle";

    List<Article> articleList;

    ShowReviewAdapter adapter;
    RecyclerView recyclerView;
    TextView tvGameName;

    String gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);

        articleList = new ArrayList<>();
        gameName = getIntent().getStringExtra("name");

        findViewById(R.id.fab_add_article).setOnClickListener(v -> {
            Intent intent = new Intent(this, ArticlePageActivity.class);
            intent.putExtra("name",gameName);
            startActivity(intent);
        });

        setUpTitle(gameName);
        setUpRV();
        getReviewListFromFB();

    }

    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent(this, ArticlePageActivity.class);
        Article article = adapter.getItem(position);
        intent.putExtra("name", gameName);
        intent.putExtra("articleUri", article.getArticleUri());
        intent.putExtra("articleName", article.getArticleName());
        intent.putExtra("userName", article.getUserName());
        startActivity(intent);
    }

    private void setUpTitle(String gameName) {
        tvGameName = findViewById(R.id.tv_show_article_game_name);
        tvGameName.setText(gameName);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String ImagePath = "icon/" + gameName;
        StorageReference imageRef = storageRef.child(ImagePath);

        ImageView icon = findViewById(R.id.iv_show_article_icon);
        imageRef.getMetadata().addOnSuccessListener(storageMetadata -> {
            String type = storageMetadata.getContentType();
            if (type.startsWith("image")) {
                GlideApp.with(this)
                        .load(imageRef)
                        .into(icon);
            }
        });
    }

    private void setUpRV() {
        recyclerView = findViewById(R.id.rv_review);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void getReviewListFromFB() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        Query userReviewQuery = databaseRef
                .child("articles")
                .child(gameName)
                .child("review")
                .child(uid);

        userReviewQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                articleList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Article article = dataSnapshot.getValue(Article.class);
                    articleList.add(article);
                    Log.d(TAG, article.getArticleName());
                }

                adapter = new ShowReviewAdapter(ShowArticleActivity.this, articleList);
                adapter.setClickListener(ShowArticleActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

}