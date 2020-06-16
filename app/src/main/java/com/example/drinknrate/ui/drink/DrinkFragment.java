package com.example.drinknrate.ui.drink;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.drinknrate.R;

public class DrinkFragment extends Fragment {

    private DrinkViewModel drinkViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        drinkViewModel =
                ViewModelProviders.of(this).get(DrinkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_drink, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        drinkViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}