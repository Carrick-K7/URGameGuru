package com.example.urgameguru.ui.gurus;

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

public class GurusFragment extends Fragment {

    private GurusViewModel gurusViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        gurusViewModel =
                new ViewModelProvider(this).get(GurusViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gurus, container, false);
        return root;
    }
}