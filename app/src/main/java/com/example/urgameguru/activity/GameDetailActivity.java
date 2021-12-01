package com.example.urgameguru.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urgameguru.utils.FirstDrawListener;
import com.example.urgameguru.R;
import com.example.urgameguru.bean.Article;
import com.example.urgameguru.bean.Comment;
import com.example.urgameguru.bean.Game;
import com.example.urgameguru.adapter.ArticleAdapter;
import com.example.urgameguru.adapter.CommentAdapter;
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

import java.util.ArrayList;
import java.util.List;

public class GameDetailActivity extends Activity implements ArticleAdapter.ItemClickListener {

    private static final String TAG = "gameDetail";

    DatabaseReference mDatabase;

    List<Comment> commentList;
    List<Article> articleList;

    CommentAdapter commentAdapter;
    ArticleAdapter articleAdapter;
    RecyclerView rvComment, rvArticles;
    ImageView ivIcon, iv_star_1, iv_star_2, iv_star_3, iv_star_4, iv_star_5;
    TextView tvRate;
    TextView tvGameName, tvPublisher, tvDeveloper, tvRelease, tvMode;

    Button btAddComment;

    double rating;
    String name;

    private final Trace viewLoadTrace = FirebasePerformance.startTrace("GameDetailActivity-LoadTime");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        View mainView = findViewById(android.R.id.content);
        mDatabase = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        name = getIntent().getStringExtra("name");

        ivIcon = findViewById(R.id.iv_game_detail_icon);
        tvPublisher = findViewById(R.id.tv_publisher);
        tvDeveloper = findViewById(R.id.tv_developer);
        tvRelease = findViewById(R.id.tv_release);
        tvMode = findViewById(R.id.tv_mode);
        tvGameName = findViewById(R.id.tv_game_detail_name);

        tvRate = findViewById(R.id.tv_game_detail_rating);

        btAddComment = findViewById(R.id.bt_add_comment);
        btAddComment.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCommentActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        });

        iv_star_1 = findViewById(R.id.iv_game_detail_star_1);
        iv_star_2 = findViewById(R.id.iv_game_detail_star_2);
        iv_star_3 = findViewById(R.id.iv_game_detail_star_3);
        iv_star_4 = findViewById(R.id.iv_game_detail_star_4);
        iv_star_5 = findViewById(R.id.iv_game_detail_star_5);

        commentList = new ArrayList<>();
        articleList = new ArrayList<>();

        setUpGameInfo(name);
        setUpRV();
        getCommentListFromFB(name);
        getArticleListFromFB(name);

        FirstDrawListener.registerFirstDrawListener(mainView, new FirstDrawListener.OnFirstDrawCallback() {
            @Override
            public void onDrawingStart() {}

            @Override
            public void onDrawingFinish() {
                viewLoadTrace.stop();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCommentListFromFB(name);
    }

    private void setUpGameInfo(String name) {
        // Load game icon
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String ImagePath = "icon/" + name;
        StorageReference imageRef = storageRef.child(ImagePath);
        imageRef.getMetadata().addOnSuccessListener(storageMetadata -> {
            String type = storageMetadata.getContentType();
            if (type.startsWith("image")) {
                GlideApp.with(this)
                        .load(imageRef)
                        .into(ivIcon);
            }
        });

        // Load game info
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Game game = dataSnapshot.getValue(Game.class);
                tvPublisher.setText(game.publisher);
                tvDeveloper.setText(game.developer);
                tvRelease.setText(game.release);
                tvMode.setText(game.mode);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mDatabase.child("games").child(name).addListenerForSingleValueEvent(eventListener);

        tvGameName.setText(name);

    }

    private void setUpRV() {
        rvComment = findViewById(R.id.rv_comment);
        rvComment.setLayoutManager(new LinearLayoutManager(this));

        rvArticles = findViewById(R.id.rv_articles);
        rvArticles.setLayoutManager(new LinearLayoutManager(this));
    }

    public void getCommentListFromFB(String name) {
        mDatabase = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        DatabaseReference commentRef = mDatabase.child("comment").child(name);
        commentRef.get().addOnSuccessListener(dataSnapshot -> {
            commentList.clear();
            int totalRating = 0;
            int cnt = 0;
            for (DataSnapshot commentSnapshot:dataSnapshot.getChildren()) {
                Comment comment = commentSnapshot.getValue(Comment.class);
                commentList.add(comment);
                totalRating += Integer.parseInt(comment.getCommentRate());
                cnt += 1;
            }

            commentAdapter = new CommentAdapter(this, commentList);
            rvComment.setAdapter(commentAdapter);

            // Calculate the game rating locally
            rating = (double) totalRating / (double) cnt;
            Log.d(TAG, String.valueOf(totalRating));
            Log.d(TAG, String.valueOf(cnt));
            Log.d(TAG, String.valueOf(rating));
            tvRate.setText(String.format("%.1f", rating));

            // Show the correct stars
            if (rating < 5) {iv_star_5.setImageResource(R.drawable.star_empty);}
            if (rating < 4) {iv_star_4.setImageResource(R.drawable.star_empty);}
            if (rating < 3) {iv_star_3.setImageResource(R.drawable.star_empty);}
            if (rating < 2) {iv_star_2.setImageResource(R.drawable.star_empty);}
            if (rating < 1) {iv_star_1.setImageResource(R.drawable.star_empty);}

        });
    }

    public void getArticleListFromFB(String name) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        DatabaseReference articleRef = databaseRef.child("articles").child(name).child("review");
        articleRef.get().addOnSuccessListener(dataSnapshot -> {
            articleList.clear();
            for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                for (DataSnapshot articleSnapshot: userSnapshot.getChildren()) {
                    Article article = articleSnapshot.getValue(Article.class);
                    articleList.add(article);
                }
            }

            articleAdapter = new ArticleAdapter(this, articleList);
            articleAdapter.setClickListener(GameDetailActivity.this);
            rvArticles.setAdapter(articleAdapter);
        });
    }

    @Override
    public void onItemClick(View v, int position) {
        // Start new activity with existed data
        Intent intent = new Intent(this, ArticlePageActivity.class);
        Article article = articleAdapter.getItem(position);
        intent.putExtra("name", name);
        intent.putExtra("articleUri", article.getArticleUri());
        intent.putExtra("articleName", article.getArticleName());
        intent.putExtra("userName", article.getUserName());
        startActivity(intent);
    }
}