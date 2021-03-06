package com.example.urgameguru.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.urgameguru.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;


public class AddGameActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    String[] existedGameList;
    AutoCompleteTextView avGameName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        avGameName = findViewById(R.id.et_game_name);

        mDatabase = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        // Add game to my repo
        MaterialButton btAdd = findViewById(R.id.bt_add_game_name);
        btAdd.setOnClickListener(v -> {
            String name = avGameName.getText().toString();

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(name.equals("") || name.contains(".") || name.contains("$") || name.contains("#") || name.contains("[") || name.contains("]") || name.contains("/")) {
                        Toast.makeText(v.getContext(), "Invalid input!", Toast.LENGTH_SHORT).show();
                    }
                    else if(dataSnapshot.child(name).exists()) {

                        Trace myTrace = FirebasePerformance.getInstance().newTrace("AddGame_trace");
                        myTrace.start();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        mDatabase.child("user_games").child(user.getUid()).child(name).setValue(true);

                        // Increase the number of users of that game by 1
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("num_users", ServerValue.increment(1));
                        mDatabase.child("games").child(name).updateChildren(updates);

                        Toast.makeText(AddGameActivity.this, "Game template found, " + name + " has been added to your repo!", Toast.LENGTH_SHORT).show();

                        myTrace.stop();
                        finish();
                    }
                    else {
                        Intent intent = new Intent(v.getContext(), AddGameDetailActivity.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            mDatabase.child("games").addListenerForSingleValueEvent(eventListener);
        });

        // Show the suggestions of existed game templates when user add a game
        DatabaseReference gameListRef = mDatabase.child("games");
        gameListRef.get().addOnSuccessListener(dataSnapshot -> {
            List<String> listGameList = new ArrayList<>();
            for (DataSnapshot game: dataSnapshot.getChildren()) {
                listGameList.add(game.getKey());
            }

            existedGameList = listGameList.toArray(new String[0]);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, existedGameList);
            avGameName.setAdapter(adapter);
        });

    }
}
