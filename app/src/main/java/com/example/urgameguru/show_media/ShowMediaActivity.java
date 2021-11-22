package com.example.urgameguru.show_media;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.urgameguru.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.urgameguru.utils.FirebaseStorageHelper;

import java.util.ArrayList;
import java.util.List;

public class ShowMediaActivity extends AppCompatActivity implements ShowMediaAdapter.ItemClickListener {

    private static final String TAG = "showMedia";

    List<String> imageList;

    ShowMediaAdapter adapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_media);

        setUpRV();

        imageList = FirebaseStorageHelper.listAll();
        Log.d(TAG, imageList.toString());

        getImageListFromFB();
//        testData.add("https://images.contentstack.io/v3/assets/blt370612131b6e0756/blt949920a2daca917e/5fad835646f622769b5edc16/LoL_WR_KV_Wallpaper_1920x1080.jpg");
//        testData.add("https://thumbor.forbes.com/thumbor/960x0/https%3A%2F%2Fspecials-images.forbesimg.com%2Fimageserve%2F5fb527d63d8a39e3d4a0a258%2FThe-League-of-Legends--Wild-Rift-key-art-%2F960x0.jpg%3Ffit%3Dscale");
//        testData.add("https://cdn.vox-cdn.com/thumbor/IbcWaah9UlIiIZ5mKOugJAtGtsA=/0x0:8000x5312/1200x800/filters:focal(3360x2016:4640x3296)/cdn.vox-cdn.com/uploads/chorus_image/image/69048793/Key_Visual.0.png");


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
        String userImagePath = "image/" + user.getUid();
        StorageReference imageRef = storageRef.child(userImagePath);

        imageRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                Log.d(TAG, item.toString());
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageList.add(uri.toString());
                    Log.d(TAG, uri.toString());
                });
            }

            adapter = new ShowMediaAdapter(this, imageList);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        });
    }
}