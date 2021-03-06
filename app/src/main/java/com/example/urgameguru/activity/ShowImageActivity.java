package com.example.urgameguru.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.urgameguru.R;
import com.example.urgameguru.adapter.ShowImageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ShowImageActivity extends AppCompatActivity implements ShowImageAdapter.ItemClickListener {
    private static final String TAG = "showImage";

    List<StorageReference> imageList;

    ShowImageAdapter adapter;
    RecyclerView recyclerView;

    String gameName;

    private final Trace viewLoadTrace = FirebasePerformance.startTrace("ShowImageActivity-LoadTime");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        gameName = getIntent().getStringExtra("name");

        setUpRV();
        getImageListFromFB();
    }

    @Override
    public void onItemClick(View v, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    private void setUpRV() {
        recyclerView = findViewById(R.id.rv_test);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getImageListFromFB() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String userImagePath = "image/" + gameName + "/" + user.getUid();
        Log.d(TAG, userImagePath);
        StorageReference imageRef = storageRef.child(userImagePath);

        imageRef.listAll().addOnSuccessListener(listResult -> {
            imageList = new ArrayList<>();
            imageList.addAll(listResult.getItems());

            Log.d(TAG, imageList.toString());
            Log.d(TAG, String.valueOf(imageList.size()));

            adapter = new ShowImageAdapter(this, imageList);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
            viewLoadTrace.stop();
        });
    }
}