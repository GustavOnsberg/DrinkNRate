package com.example.drinknrate.ui.drink;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.drinknrate.MainActivity;
import com.example.drinknrate.R;
import com.google.firebase.database.FirebaseDatabase;

public class DrinkFragment extends Fragment {

    private DrinkViewModel drinkViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drink, container, false);
        Button setDesc = root.findViewById(R.id.addDescButton);
        Button changeDesc = root.findViewById(R.id.changeDesc);
        Button setImage = root.findViewById(R.id.addImageBtn);
        RatingBar meanRating = root.findViewById(R.id.meanRating);
        TextView drinkDesc = root.findViewById(R.id.drinkDesc);
        TextView drinkName = root.findViewById(R.id.drinkName);
        ImageView image = root.findViewById(R.id.imageView);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (((MainActivity)getActivity()).getDrinkSelected() == -1) {
            setDesc.setVisibility(View.GONE);
            changeDesc.setVisibility(View.GONE);
            setImage.setVisibility(View.GONE);
            meanRating.setVisibility(View.GONE);
            drinkDesc.setVisibility(View.GONE);
            drinkName.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
        } else {
            drinkName.setText(((MainActivity)getActivity()).getBarcodeNumber());
        }
        drinkViewModel =
                ViewModelProviders.of(this).get(DrinkViewModel.class);

        return root;
    }
}