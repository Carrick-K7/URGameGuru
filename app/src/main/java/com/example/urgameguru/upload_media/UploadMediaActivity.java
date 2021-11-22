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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.urgameguru.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class UploadMediaActivity extends AppCompatActivity {

    private static final String TAG = "uploadMedia";

    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri uploadUri;
    String mimetype;
    String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_media);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        ContentResolver cr = this.getContentResolver();

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                assert data != null;
                uploadUri = data.getData();

                ImageView imageView = findViewById(R.id.iv_upload_preview);
                imageView.setImageURI(uploadUri);
            }
        }
        );


        Button btSelect = findViewById(R.id.bt_select);
        btSelect.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);
        });

        Button btUpload = findViewById(R.id.bt_upload);
        btUpload.setOnClickListener(v -> {


            mimetype = cr.getType(uploadUri);
            Log.d(TAG, mimetype);

            uuid = UUID.randomUUID().toString();
            String typeStr =  mimetype.substring(0, mimetype.indexOf("/")) + "/";
            String uploadPath = typeStr + user.getUid() + "/" + uuid;
            Log.d(TAG, uploadPath);

            StorageReference uploadRef = storageRef.child(uploadPath);

            UploadTask uploadTask = uploadRef.putFile(uploadUri);
            uploadTask.addOnFailureListener( e -> {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(this, "Upload success!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });

    }
}