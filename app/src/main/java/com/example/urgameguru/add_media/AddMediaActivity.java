package com.example.urgameguru.add_media;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.urgameguru.R;

import java.util.ArrayList;

public class AddMediaActivity extends AppCompatActivity implements AddMediaAdapter.ItemClickListener {

    AddMediaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_media);

        ArrayList<String> testData = new ArrayList<>();
        testData.add("https://images.contentstack.io/v3/assets/blt370612131b6e0756/blt949920a2daca917e/5fad835646f622769b5edc16/LoL_WR_KV_Wallpaper_1920x1080.jpg");
        testData.add("https://thumbor.forbes.com/thumbor/960x0/https%3A%2F%2Fspecials-images.forbesimg.com%2Fimageserve%2F5fb527d63d8a39e3d4a0a258%2FThe-League-of-Legends--Wild-Rift-key-art-%2F960x0.jpg%3Ffit%3Dscale");
        testData.add("https://cdn.vox-cdn.com/thumbor/IbcWaah9UlIiIZ5mKOugJAtGtsA=/0x0:8000x5312/1200x800/filters:focal(3360x2016:4640x3296)/cdn.vox-cdn.com/uploads/chorus_image/image/69048793/Key_Visual.0.png");

        RecyclerView recyclerView = findViewById(R.id.rv_test);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddMediaAdapter(this, testData);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View v, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}