package com.example.drinknrate.ui.drink;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DrinkFragment extends Fragment {

    private DrinkViewModel drinkViewModel;
    private String title;
    private String description;
    private float rating;
    private int totalRatings;
    private Context mContext;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drink, container, false);

        final Button setDesc = root.findViewById(R.id.addDescButton);
        final Button changeDesc = root.findViewById(R.id.changeDesc);
        Button setImage = root.findViewById(R.id.addImageBtn);
        final RatingBar meanRating = root.findViewById(R.id.meanRating);
        final TextView drinkDesc = root.findViewById(R.id.drinkDesc);
        final TextView drinkName = root.findViewById(R.id.drinkName);
        Button submitRatingBtn = root.findViewById(R.id.submitNewRatingBtn);
        ImageView image = root.findViewById(R.id.imageView);
        if (((MainActivity)mContext).getDrinkSelected() == -1) {
            setDesc.setVisibility(View.GONE);
            changeDesc.setVisibility(View.GONE);
            setImage.setVisibility(View.GONE);
            meanRating.setVisibility(View.GONE);
            drinkDesc.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            submitRatingBtn.setVisibility(View.GONE);
            drinkName.setText("No drink selected");
        } else {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            String barcodeNumber = ((MainActivity)getActivity()).getBarcodeNumber();
            final DatabaseReference ref = database.getReference(barcodeNumber);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    title = dataSnapshot.child("title").getValue().toString();
                    description = dataSnapshot.child("description").getValue().toString();
                    rating = Float.parseFloat(dataSnapshot.child("rating").getValue().toString());
                    totalRatings = Integer.parseInt(dataSnapshot.child("totalRatings").getValue().toString());


                    if (((MainActivity)mContext).getDrinkSelected() == 1) {
                        drinkName.setText(title);
                        if (description.length() != 0) {
                            drinkDesc.setText(description);
                            Log.i("description", "onDataChange: description is set");
                            changeDesc.setVisibility(View.VISIBLE);
                            setDesc.setVisibility(View.GONE);
                        }
                        meanRating.setRating(rating/totalRatings);
                        meanRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            @Override
                            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                if (meanRating.getRating() < 1) {
                                    meanRating.setRating(1);
                                }
                            }
                        });

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        drinkViewModel =
                ViewModelProviders.of(this).get(DrinkViewModel.class);

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;

    }
}