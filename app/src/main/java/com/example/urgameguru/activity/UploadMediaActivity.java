package com.example.urgameguru.activity;

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
import com.example.urgameguru.bean.Media;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
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
    String gameName, mediaName;
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

        gameName = getIntent().getStringExtra("name");

        user = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        ContentResolver cr = this.getContentResolver();

        etMediaName = findViewById(R.id.et_media_name);
        pbUpload = findViewById(R.id.pb_upload);
        pvPreview = findViewById(R.id.pv_upload_preview);
        ivPreview = findViewById(R.id.iv_upload_preview);

        // Upload image or video to database async
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                assert data != null;
                uploadUri = data.getData();

                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                mimetype = cr.getType(uploadUri);
                typeStr =  mimetype.substring(0, mimetype.indexOf("/"));
                fileExt = mimeTypeMap.getExtensionFromMimeType(mimetype);
                Log.d(TAG, mimetype);

                ViewGroup.LayoutParams pvParams = pvPreview.getLayoutParams();
                ViewGroup.LayoutParams ivParams = ivPreview.getLayoutParams();

                etMediaName.setText("");

                // Clear the existing media file if user reselect the resource
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

        // Invoke Android media selection activity
        findViewById(R.id.bt_select).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            activityResultLauncher.launch(intent);
        });

        findViewById(R.id.bt_upload).setOnClickListener(v -> {

            Trace myTrace = FirebasePerformance.getInstance().newTrace("UploadMedia_trace");
            myTrace.start();
            pbUpload.setVisibility(View.VISIBLE);

            uuid = UUID.randomUUID().toString();
            String storageUploadPath = typeStr + "/" + gameName + "/" + user.getUid() + "/" + uuid + "-" + mediaName + "." + fileExt;
            Log.d(TAG, storageUploadPath);

            StorageReference uploadRef = storageRef.child(storageUploadPath);

            UploadTask uploadTask = uploadRef.putFile(uploadUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                uploadRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    mediaName = etMediaName.getText().toString().trim();

                    Media media = new Media(mediaName, downloadUri.toString());
                    String dbUploadPath = typeStr + "/" + gameName + "/" + user.getUid() + "/" + uuid;
                    databaseRef.child(dbUploadPath).setValue(media);

                    pbUpload.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "Upload " + typeStr + " success!", Toast.LENGTH_SHORT).show();

                    myTrace.stop();
                    finish();
                });
            }).addOnFailureListener( e -> {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            });
        });

    }
}