package com.example.urgameguru.gurus;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.urgameguru.R;
import com.example.urgameguru.game_detail.GameDetailActivity;

public class GurusFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final GurusViewModel gurusViewModel =
                new ViewModelProvider(this).get(GurusViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gurus, container, false);

        CardView cvMostPopular1 = root.findViewById(R.id.cv_most_popular_1);
        cvMostPopular1.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), GameDetailActivity.class);
            startActivity(intent);
        });

        return root;
    }
}