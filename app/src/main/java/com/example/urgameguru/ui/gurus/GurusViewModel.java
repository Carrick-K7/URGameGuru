package com.example.urgameguru.ui.gurus;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GurusViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GurusViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gurus fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}