package com.example.drinknrate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_input, R.id.navigation_scan, R.id.navigation_drink)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        //database test
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");
    }

    //onClick methods

    public void sendNumber(View v){
        EditText editText = (EditText) findViewById(R.id.inputNumber);
        String barcodeNumber = editText.getText().toString();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rating = database.getReference(barcodeNumber+"/rating");
        rating.setValue(5+"");
        DatabaseReference description = database.getReference(barcodeNumber+"/description");
        description.setValue("good beer");
        DatabaseReference title = database.getReference(barcodeNumber+"/title");
        title.setValue("beer name");
        Log.i("intput", "onClick: button was clicked");
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

    public void addImage(View v) {

    }


}