package com.example.drinknrate.ui.drink;

import android.widget.Button;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DrinkViewModel extends ViewModel {

    private MutableLiveData<String> mText;


    public DrinkViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("No drink selected");
    }

    public LiveData<String> getText() {
        return mText;
    }
}