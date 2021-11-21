package com.example.urgameguru.show_media;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.urgameguru.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ShowMediaActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri uploadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_media);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        assert user != null;
        String testPath = user.getUid() + "/test.img";
        StorageReference testRef = storageRef.child(testPath);


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            assert data != null;
                            uploadUri = data.getData();
                            ImageView imageView = findViewById(R.id.iv_upload_preview);
                            imageView.setImageURI(uploadUri);
                        }
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
            UploadTask uploadTask = testRef.putFile(uploadUri);
            uploadTask.addOnFailureListener( e -> {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(this, "Upload success!", Toast.LENGTH_SHORT).show();
            });
        });

    }
}