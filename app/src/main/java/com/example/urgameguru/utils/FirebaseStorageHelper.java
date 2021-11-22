package com.example.urgameguru.utils;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FirebaseStorageHelper {

    private static final String TAG = "firebaseStorageHelper";

    public static List<String> listAll() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        List<String> imageList = new ArrayList<>();

        // TODO add video support
        String userImagePath = "image/" + user.getUid();
        StorageReference imageRef = storageRef.child(userImagePath);

        Log.d(TAG, imageRef.toString());

        imageRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item: listResult.getItems()) {
                Log.d(TAG, item.toString());
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageList.add(uri.toString());
                    Log.d(TAG, uri.toString());
                });
            }

        });

        Log.d(TAG, imageList.toString());
        return imageList;
    }
}
