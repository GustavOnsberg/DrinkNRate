package com.example.drinknrate;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private static final int GALLERY_CODE = 10;
    private static final int REQUEST_CODE_CREATEDRINK = 69;
    public int drinkSelected = -1;
    public String barcodeNumber;
    private String description;
    private String title = "";
    private float rating;
    private int totalRatings;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_input, R.id.navigation_scan, R.id.navigation_drink).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        //database test


    }

    public void databaseUpdate(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    rating = Float.parseFloat(dataSnapshot.child("rating").getValue().toString());
                    totalRatings = Integer.parseInt(dataSnapshot.child("totalRatings").getValue().toString());
                    title = dataSnapshot.child("title").getValue().toString();
                    description = dataSnapshot.child("description").getValue().toString();
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //onClick methods

    public void sendNumber(View v){
        final EditText editText = (EditText) findViewById(R.id.inputNumber);
        barcodeNumber = editText.getText().toString();

        findDrink(v);
    }

    public void findDrink(View v){//onclick
        Log.i("intput", "onClick: button was clicked");
        if (14 > barcodeNumber.length() && barcodeNumber.length() > 7) {
            ref = database.getReference(barcodeNumber.toString());
            databaseUpdate();
            if(title != ""){
                setDrinkFragment();
            }else{
                createNewDrinkDialog();
            }

        } else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Wrong input length")
                    .setMessage("Barcode numbers are between 8 and 13 characters.")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void createNewDrinkDialog() {
        final EditText editText = (EditText) findViewById(R.id.inputNumber);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create new drink?")
                .setMessage("Please make sure the numbers are correct")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent createDrink = new Intent(getBaseContext(), CreateDrinkActivity.class);
                        createDrink.putExtra("barNumber",barcodeNumber);
                        startActivityForResult(createDrink,REQUEST_CODE_CREATEDRINK);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setDrinkFragment() {
        drinkSelected = 1;
        Log.i("onDataChange", "onDataChange: switch window");
        BottomNavigationView bottomNav = (BottomNavigationView)findViewById(R.id.nav_view);
        Log.i("onDataChange", "onDataChange: switch button");
        bottomNav.setSelectedItemId(R.id.navigation_drink);
    }


    public void setDesc(View v){
        Log.i("drink", "onClick: desc");
        DialogSetDescFragment dialogSetDescFragment = new DialogSetDescFragment();
        dialogSetDescFragment.show(getSupportFragmentManager(),"dialogSetDesc");
    }

    public void changeDesc(View v) {
        DialogChangeDescFragment dialogChangeDescFragment = new DialogChangeDescFragment();
        dialogChangeDescFragment.show(getSupportFragmentManager(),"changeTheDescription");
    }

    //add image to imageview
    public void addImage(View v) {
        Intent imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(imageIntent,"Select Image"), GALLERY_CODE);
    }

    // HJÆLP FRA VIKTOR MED DETTE
    //
    //
    //
    //
    //
    public void submitRating(View v) {
        RatingBar ratingBar = findViewById(R.id.meanRating);
        DatabaseReference ref = database.getReference(barcodeNumber.toString());
        DatabaseReference rating = database.getReference(barcodeNumber.toString()+"/rating");
        DatabaseReference totalRatings = database.getReference(barcodeNumber.toString()+"/totalRatings");
        float newRating = ratingBar.getRating() + this.rating;
        int newTotalRatings = this.totalRatings++;
        rating.setValue(newRating);
        totalRatings.setValue(newTotalRatings);



        Log.i("submit rating", "submitRating: rating is submitted");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rating has been submitted").setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            Uri image = data.getData();
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageURI(image);
            Button imageSelect = (Button) findViewById(R.id.addImageBtn);
            imageSelect.setVisibility(View.GONE);
        } else if (requestCode == REQUEST_CODE_CREATEDRINK && resultCode == RESULT_OK && data != null) {
            Toast okResult = Toast.makeText(this,"Drink is submitted", Toast.LENGTH_SHORT);
            okResult.show();
            setDrinkFragment();
        }
    }

    public int getDrinkSelected() {
        return drinkSelected;
    }

    public String getBarcodeNumber() {
        return barcodeNumber;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}