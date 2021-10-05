package com.example.urgameguru.ui.my_expo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyExpoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyExpoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my_expo fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}