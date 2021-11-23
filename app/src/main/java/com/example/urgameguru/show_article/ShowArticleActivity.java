package com.example.urgameguru.show_article;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.urgameguru.R;
import com.example.urgameguru.add_article.ArticlePageActivity;
import com.example.urgameguru.bean.Article;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowArticleActivity extends AppCompatActivity implements ShowReviewAdapter.ItemClickListener {

    private static final String TAG = "showArticle";

    List<Article> articleList;

    ShowReviewAdapter adapter;
    RecyclerView recyclerView;

    String gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);

        articleList = new ArrayList<>();
        gameName = "Wild Rift";

        setUpRV();
        getReviewListFromFB();

    }

    @Override
    public void onItemClick(View v, int position) {
        // TODO add article data;
        Intent intent = new Intent(this, ArticlePageActivity.class);
        Article article = adapter.getItem(position);
        intent.putExtra("articleUri", article.getArticleUri());
        intent.putExtra("articleName", article.getArticleName());
        startActivity(intent);

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