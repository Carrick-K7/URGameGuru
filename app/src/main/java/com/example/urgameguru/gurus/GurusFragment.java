package com.example.urgameguru.gurus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urgameguru.R;
import com.example.urgameguru.game_detail.GameDetailActivity;
import com.example.urgameguru.my_expo.AddGameActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GurusFragment extends Fragment implements MostPopularAdapter.ItemClickListener, MostRecentAdapter.ItemClickListener {

    MostPopularAdapter most_popular_adapter;
    MostRecentAdapter most_recent_adapter;

    private static final String TAG = "MyExpoActivity";

    private DatabaseReference databaseReference;
    private Query query_most_popular_games;
    private Query query_most_recent_games;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final GurusViewModel gurusViewModel =
                new ViewModelProvider(this).get(GurusViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gurus, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        most_popular_adapter = new MostPopularAdapter(view.getContext());
        most_popular_adapter.setClickListener(GurusFragment.this);

        most_recent_adapter = new MostRecentAdapter(view.getContext());
        most_recent_adapter.setClickListener(GurusFragment.this);

        //query user game data
        databaseReference = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        query_most_popular_games = databaseReference.child("games").orderByChild("num_users").limitToLast(10);

        ValueEventListener eventListener1 = new ValueEventListener() {
            List<String> games = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String game_name = postSnapshot.getKey();
                    if(!games.contains(game_name)) games.add(game_name);
                }

                // set up the RecyclerView
                RecyclerView recyclerView = view.findViewById(R.id.rv_most_popular_games);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                List ranked_games = Lists.reverse(games);
                most_popular_adapter.game_rank = new String[]{"1","2","3","4","5","6","7","8","9","10"};
                most_popular_adapter.mData = ranked_games;
                recyclerView.setAdapter(most_popular_adapter);

            }

            @Override

            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        };
        query_most_popular_games.addValueEventListener(eventListener1);

        query_most_recent_games = databaseReference.child("games").orderByChild("release").limitToLast(10);
        ValueEventListener eventListener2 = new ValueEventListener() {
            List<String> games = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String game_name = postSnapshot.getKey();
                    if(!games.contains(game_name)) games.add(game_name);
                }

                // set up the RecyclerView
                RecyclerView recyclerView = view.findViewById(R.id.rv_most_recent_games);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                List ranked_games = Lists.reverse(games);
                most_recent_adapter.game_rank = new String[]{"1","2","3","4","5","6","7","8","9","10"};
                most_recent_adapter.mData = ranked_games;
                recyclerView.setAdapter(most_recent_adapter);

            }

            @Override

            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        };
        query_most_recent_games.addValueEventListener(eventListener2);
    }

    @Override
    public void onItemClick1(View view, int position) {
        Log.i("TAG", "You clicked number " + most_popular_adapter.getItem(position) + ", which is at cell position " + position);
        Intent intent = new Intent(view.getContext(), GameDetailActivity.class);
        intent.putExtra("name", most_popular_adapter.getItem(position));
        startActivity(intent);
    }

    @Override
    public void onItemClick2(View view, int position) {
        Log.i("TAG", "You clicked number " + most_recent_adapter.getItem(position) + ", which is at cell position " + position);
        Intent intent = new Intent(view.getContext(), GameDetailActivity.class);
        intent.putExtra("name", most_recent_adapter.getItem(position));
        startActivity(intent);
    }
}