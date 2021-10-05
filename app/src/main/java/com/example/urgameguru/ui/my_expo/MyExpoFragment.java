package com.example.urgameguru.ui.my_expo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.urgameguru.R;

public class MyExpoFragment extends Fragment {

    private MyExpoViewModel myExpoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myExpoViewModel =
                new ViewModelProvider(this).get(MyExpoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_expo, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        myExpoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}