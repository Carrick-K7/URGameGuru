package com.example.urgameguru.ui.my_expo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;

import com.example.urgameguru.utils.FirstDrawListener;
import com.example.urgameguru.activity.AddGameActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.example.urgameguru.R;
import com.example.urgameguru.activity.MyGameDetailActivity;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import java.util.ArrayList;
import java.util.List;

public class MyExpoFragment extends Fragment implements MyExpoRecyclerViewAdapter.ItemClickListener{

    MyExpoRecyclerViewAdapter adapter;
    private Trace fragmentLoadTrace;

    private static final String TAG = "MyExpoActivity";

    private DatabaseReference databaseReference;
    private Query query_user_games;

    private ValueEventListener mMessagesListener;

    private TextView tvUserName;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fragmentLoadTrace = FirebasePerformance.startTrace("MyExpoFragment-LoadTime");
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_expo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUserName = view.findViewById(R.id.tv_my_expo_user_name);
        tvUserName.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        FloatingActionButton btAdd = view.findViewById(R.id.bt_add);
        btAdd.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddGameActivity.class);
            startActivity(intent);
        });

        adapter = new MyExpoRecyclerViewAdapter(view.getContext());
        adapter.setClickListener(MyExpoFragment.this);

        // query user game data
        databaseReference = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        query_user_games = databaseReference.child("user_games").child(user.getUid());

        List<String> user_games = new ArrayList<>();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String game_name = postSnapshot.getKey();
                    if(!user_games.contains(game_name)) user_games.add(game_name);
                }

                TextView num_games = view.findViewById(R.id.tv_num_games);
                int n_games = user_games.size();
                String N_GAMES = String.format("%d", n_games);
                num_games.setText(N_GAMES);

                // set up the RecyclerView
                RecyclerView recyclerView = view.findViewById(R.id.rv_my_games);
                int numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), numberOfColumns));

                adapter.mData = user_games;
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        };
        query_user_games.addValueEventListener(eventListener);

        FirstDrawListener.registerFirstDrawListener(view, new FirstDrawListener.OnFirstDrawCallback() {

            @Override
            public void onDrawingStart() {}

            @Override
            public void onDrawingFinish() {
                fragmentLoadTrace.stop();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
        Intent intent = new Intent(view.getContext(), MyGameDetailActivity.class);
        intent.putExtra("name", adapter.getItem(position));
        startActivity(intent);
    }
}