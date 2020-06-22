package com.example.drinknrate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateDrinkActivity extends AppCompatActivity {
    String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        TextView barcodeDisplay = (TextView) findViewById(R.id.barcode);
        Bundle extras = getIntent().getExtras();
        barcode = extras.getString("barNumber");
        barcodeDisplay.setText("The Barcode: " + barcode);
        RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
        //sets a listener on ratingbar
        //makes it impossible to give a rating of 0.5, as it makes it pop up on 1
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (ratingBar.getRating() == 0.5) {
                    ratingBar.setRating(1);
                }
            }
        });
    }

    //onClick method to submit the drink
    public void submitDrink(View v) {
        Intent data = new Intent();
        EditText description = (EditText) findViewById(R.id.editDesc);
        EditText title = (EditText) findViewById(R.id.editTitle);
        RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
        if (title.getText().toString().length() > 0) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            //title
            DatabaseReference refTitle = database.getReference(barcode.toString()+"/title");
            refTitle.setValue(title.getText().toString()+"");
            //description
            if (description.getText().toString() != "") {
                DatabaseReference refDescription = database.getReference(barcode.toString()+"/description");
                refDescription.setValue(description.getText().toString()+"");
            }else{
                DatabaseReference refDescription = database.getReference(barcode.toString()+"/description");
                refDescription.setValue("");
            }
            //rating
            DatabaseReference refRating = database.getReference(barcode.toString()+"/rating");
            refRating.setValue(rating.getRating()+"");//saved as float
            //totalRatings
            if (rating.getRating() != 0) {
                DatabaseReference refTotalRatings = database.getReference(barcode.toString()+"/totalRatings");
                refTotalRatings.setValue(1);//saved as interger
            }else{
                DatabaseReference refTotalRatings = database.getReference(barcode.toString()+"/totalRatings");
                refTotalRatings.setValue(0);//saved as interger
            }
            setResult(Activity.RESULT_OK,data);
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Missing title").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}