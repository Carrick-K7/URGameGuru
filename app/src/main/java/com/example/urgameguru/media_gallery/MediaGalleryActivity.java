package com.example.urgameguru.media_gallery;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.urgameguru.R;

import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.builder.GallerySettings;

import static com.veinhorn.scrollgalleryview.loader.picasso.dsl.DSL.*; // simplifies adding media

public class MediaGalleryActivity extends FragmentActivity {
    private ScrollGalleryView galleryView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_gallery);

        galleryView = ScrollGalleryView
                .from(findViewById(R.id.scroll_gallery_view))
                .settings(
                        GallerySettings
                                .from(getSupportFragmentManager())
                                .thumbnailSize(100)
                                .enableZoom(true)
                                .build()
                )
                .onImageClickListener(position -> Toast.makeText(MediaGalleryActivity.this, "image position = " + position, Toast.LENGTH_SHORT).show())
                .onImageLongClickListener(position -> Toast.makeText(MediaGalleryActivity.this, "image position = " + position, Toast.LENGTH_SHORT).show())
                .add(image("https://www.riotgames.com/darkroom/1440/08bcc251757a1f64e30e0d7e8c513d35:be16374e056f8268996ef96555c7a113/wr-cb1-announcementarticle-banner-1920x1080.png"))
                .add(image("https://thumbor.forbes.com/thumbor/960x0/https%3A%2F%2Fspecials-images.forbesimg.com%2Fimageserve%2F5fb527d63d8a39e3d4a0a258%2FThe-League-of-Legends--Wild-Rift-key-art-%2F960x0.jpg%3Ffit%3Dscale"))
                .add(video("https://cdn.videvo.net/videvo_files/video/free/2019-02/large_watermarked/181004_10_LABORATORIES-SCIENCE_12_preview.mp4", R.mipmap.default_video))
                .build();
    }
}
