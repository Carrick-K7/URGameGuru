package com.example.urgameguru.my_expo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.urgameguru.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import java.util.UUID;

public class AddGameDetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    public AddGameDetailActivity() {
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        // [END initialize_database_ref]
    }

    private static final String TAG = "addGame";
    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri uploadUri;
    String mimetype;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_detail);

        String name = getIntent().getStringExtra("name");
        MaterialButton btAddIcon = findViewById(R.id.bt_add_game_icon);
        btAddIcon.setText("Select an icon for " + name);

        //dealing with select icon image
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


        //dealing with adding information and upload icon
        MaterialButton btAdd = findViewById(R.id.bt_add_game);

        btAdd.setOnClickListener(v -> {

            //store game information
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

            //upload icon to "icon/name"
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
                finish();
            });

        });
    }
}
