package com.example.urgameguru.my_expo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

import com.example.urgameguru.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddGameActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    public AddGameActivity() {
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        MaterialButton btAdd = findViewById(R.id.bt_add_game_name);
        btAdd.setOnClickListener(v -> {
            TextInputEditText et_name = findViewById(R.id.et_game_name);
            String name = et_name.getText().toString();

            Intent intent = new Intent(v.getContext(), AddGameDetailActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);

        });
    }
}
