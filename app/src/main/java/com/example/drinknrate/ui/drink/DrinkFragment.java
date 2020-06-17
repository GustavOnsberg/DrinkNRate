package com.example.drinknrate.ui.drink;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.drinknrate.R;

public class DrinkFragment extends Fragment {

    private DrinkViewModel drinkViewModel;
    public TextView descText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        drinkViewModel =
                ViewModelProviders.of(this).get(DrinkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_drink, container, false);
        return root;
    }
}