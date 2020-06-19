package com.example.drinknrate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class CreateDrinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        TextView barcodeDisplay = (TextView) findViewById(R.id.barcode);
        Bundle extras = getIntent().getExtras();
        String barcode = extras.getString("barNumber");
        barcodeDisplay.setText("The Barcode: " + barcode);
    }

    public void submitDrink(View v) {//onclick
        Intent data = new Intent();
        EditText description = (EditText) findViewById(R.id.editDesc);
        EditText title = (EditText) findViewById(R.id.editTitle);
        RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
        if (title.getText().toString().length() > 0) {
            data.putExtra("description", description.getText().toString());
            data.putExtra("title",title.getText().toString());
            data.putExtra("rating",rating.getRating());
            System.out.println(rating.getRating());
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