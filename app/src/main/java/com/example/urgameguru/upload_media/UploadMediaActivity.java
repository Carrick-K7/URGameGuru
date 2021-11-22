package com.example.urgameguru.upload_media;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.urgameguru.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.BaseMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class UploadMediaActivity extends AppCompatActivity {

    private static final String TAG = "uploadMedia";

    FirebaseUser user;
    StorageReference storageRef;
    DatabaseReference databaseRef;

    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri uploadUri;
    String mediaName;
    String mimetype, typeStr, fileExt;
    String uuid;

    Button btUpload;
    ImageView ivPreview;
    EditText etMediaName;
    PlayerView pvPreview;

    ExoPlayer player;

    ProgressBar pbUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_media);

        user = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        ContentResolver cr = this.getContentResolver();

        etMediaName = findViewById(R.id.et_media_name);
        pbUpload = findViewById(R.id.pb_upload);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                assert data != null;
                uploadUri = data.getData();

                mediaName = etMediaName.getText().toString().trim();
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                mimetype = cr.getType(uploadUri);
                typeStr =  mimetype.substring(0, mimetype.indexOf("/"));
                fileExt = mimeTypeMap.getExtensionFromMimeType(mimetype);
                Log.d(TAG, mimetype);

                pvPreview = findViewById(R.id.pv_upload_preview);
                ViewGroup.LayoutParams pvParams = pvPreview.getLayoutParams();
                ivPreview = findViewById(R.id.iv_upload_preview);
                ViewGroup.LayoutParams ivParams = ivPreview.getLayoutParams();

                if(typeStr.equals("image")) {
                    pvPreview.setVisibility(View.INVISIBLE);
                    pvParams.height = 0;
                    pvPreview.setLayoutParams(pvParams);

                    ivPreview.setImageURI(uploadUri);
                    ivParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    ivPreview.setLayoutParams(ivParams);
                    ivPreview.setVisibility(View.VISIBLE);

                } else if (typeStr.equals("video")) {
                    ivPreview.setVisibility(View.INVISIBLE);
                    ivParams.height = 0;
                    ivPreview.setLayoutParams(ivParams);

                    player = new ExoPlayer.Builder(this).build();
                    pvPreview.setPlayer(player);
                    MediaItem mediaItem = MediaItem.fromUri(uploadUri);
                    player.setMediaItem(mediaItem);
                    player.prepare();
                    player.setPlayWhenReady(false);

                    pvParams.height = 1000;
                    pvPreview.setLayoutParams(pvParams);
                    pvPreview.setVisibility(View.VISIBLE);
                }

                btUpload = findViewById(R.id.bt_upload);
                btUpload.setVisibility(View.VISIBLE);

                etMediaName.setVisibility(View.VISIBLE);
            }
        }
        );

        findViewById(R.id.bt_select).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            activityResultLauncher.launch(intent);
        });

        findViewById(R.id.bt_upload).setOnClickListener(v -> {

            pbUpload.setVisibility(View.VISIBLE);

            uuid = UUID.randomUUID().toString();
            String storageUploadPath = typeStr + "/" + user.getUid() + "/" + uuid + "-" + mediaName + "." + fileExt;
            Log.d(TAG, storageUploadPath);

            StorageReference uploadRef = storageRef.child(storageUploadPath);

            UploadTask uploadTask = uploadRef.putFile(uploadUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {

                Media media = new Media(mediaName, taskSnapshot.getUploadSessionUri().toString());
                String dbUploadPath = typeStr + "/" + user.getUid() + "/" + uuid;
                databaseRef.child(dbUploadPath).setValue(media);

                pbUpload.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Upload " + typeStr + " success!", Toast.LENGTH_SHORT).show();

                finish();
            }).addOnFailureListener( e -> {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            });
        });

    }
}