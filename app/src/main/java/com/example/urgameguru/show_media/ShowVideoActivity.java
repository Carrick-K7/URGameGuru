package com.example.urgameguru.show_media;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urgameguru.FirstDrawListener;
import com.example.urgameguru.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ShowVideoActivity extends AppCompatActivity implements ShowVideoAdapter.ItemClickListener {
    private static final String TAG = "showVideo";

    List<StorageReference> videoList;

    ShowVideoAdapter adapter;
    RecyclerView recyclerView;

    String gameName;

    private final Trace viewLoadTrace = FirebasePerformance.startTrace("ShowVideoActivity-LoadTime");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);

        View mainView = findViewById(android.R.id.content);
        gameName = getIntent().getStringExtra("name");

        setUpRV();
        getVideoListFromFB();
    }

    @Override
    public void onItemClick(View v, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    private void setUpRV() {
        recyclerView = findViewById(R.id.rv_test);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getVideoListFromFB() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String userVideoPath = "video/" + gameName + "/" + user.getUid();
        StorageReference videoRef = storageRef.child(userVideoPath);

        videoRef.listAll().addOnSuccessListener(listResult -> {
            videoList = new ArrayList<>();
            for (StorageReference item : listResult.getItems()) {
                videoList.add(item);
            }

            Log.d(TAG, videoList.toString());
            Log.d(TAG, String.valueOf(videoList.size()));

            adapter = new ShowVideoAdapter(this, videoList);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
            viewLoadTrace.stop();
        });
    }

}
