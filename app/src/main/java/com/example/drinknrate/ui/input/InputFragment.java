package com.example.drinknrate.ui.input;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.drinknrate.R;

public class InputFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //sets the view as the fragment
        View root = inflater.inflate(R.layout.fragment_input, container, false);
        return root;
    }
}