package com.example.urgameguru.my_expo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.urgameguru.R;
import com.example.urgameguru.my_game_detail.MyGameDetailActivity;

public class MyExpoFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final MyExpoViewModel myExpoViewModel =
                new ViewModelProvider(this).get(MyExpoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_expo, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final LinearLayout ll_wild_rift = view.findViewById(R.id.ll_wild_rift);
        ll_wild_rift.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MyGameDetailActivity.class);
            startActivity(intent);
        });
    }

}