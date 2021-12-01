package com.example.urgameguru.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.urgameguru.bean.Game;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.urgameguru.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddGameDetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private static final String TAG = "addGame";
    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri uploadUri;
    String mimetype;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_detail);

        mDatabase = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        String name = getIntent().getStringExtra("name");
        MaterialButton btAddIcon = findViewById(R.id.bt_add_game_icon);
        btAddIcon.setText("Select an icon for " + name);

        // Dealing with selecting an image as icon
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        ContentResolver cr = this.getContentResolver();

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        uploadUri = data.getData();

                        ImageView imageView = findViewById(R.id.iv_icon_preview);
                        imageView.setImageURI(uploadUri);
                    }
                }
        );

        btAddIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);
        });


        // Dealing with adding information and uploading icon
        MaterialButton btAdd = findViewById(R.id.bt_add_game);
        btAdd.setOnClickListener(v -> {
            Trace myTrace = FirebasePerformance.getInstance().newTrace("AddGameDetail_trace");
            myTrace.start();

            // Store game information
            TextInputEditText et_developer = findViewById(R.id.et_developer);
            String developer = et_developer.getText().toString();
            TextInputEditText et_publisher = findViewById(R.id.et_publisher);
            String publisher = et_publisher.getText().toString();
            TextInputEditText et_release = findViewById(R.id.et_release);
            String release = et_release.getText().toString();
            TextInputEditText et_mode = findViewById(R.id.et_mode);
            String mode = et_mode.getText().toString();

            Game game = new Game(developer, publisher, release, mode);

            mDatabase.child("games").child(name).setValue(game)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(this, "Upload game info success!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    });

            // Upload icon to "icon/name"
            mimetype = cr.getType(uploadUri);
            Log.d(TAG, mimetype);

            String uploadPath = "icon/" + name;
            Log.d(TAG, uploadPath);

            StorageReference uploadRef = storageRef.child(uploadPath);

            UploadTask uploadTask = uploadRef.putFile(uploadUri);
            uploadTask.addOnFailureListener( e -> {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(this, "Upload success!", Toast.LENGTH_SHORT).show();
                myTrace.stop();
                finish();
            });

        });
    }
}
