package com.example.drinknrate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DialogSetDescFragment extends AppCompatDialogFragment {
    @SuppressLint("ResourceType")
    @NonNull
    @Override
    //creates a dialog from a dialogfragment
    //set and change description fragment is two different fragments
    //due to the fact, that change desc fragment gets the description to fill it in the editview
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText descInput = new EditText(getActivity());
        //sets up a linear layout with layout params that matches the parent
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String barcodeNumber = ((MainActivity)getActivity()).getBarcodeNumber();
        final DatabaseReference refDesc = database.getReference(barcodeNumber.toString()+"/description");
        descInput.setLayoutParams(layout);
        builder.setView(descInput);
        builder.setTitle("Add Description")
                .setMessage("Add a description to the drink (less than 100 characters)")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView descText = (TextView) getActivity().findViewById(R.id.drinkDesc);
                //checks if the description is less than 100 characters
                //this choice is made, as a max of 100 looks the best on the fragment_drink description
                if (descInput.length() <= 100) {
                    descText.setText(descInput.getText().toString());
                    getActivity().findViewById(R.id.addDescButton).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.changeDesc).setVisibility(View.VISIBLE);
                    refDesc.setValue(descInput.getText().toString()+"");
                    Toast finalMsg = Toast.makeText(getContext(),"Description is set", Toast.LENGTH_SHORT);
                    finalMsg.show();
                    dialog.dismiss();
                } else {
                    Toast errorMsg = Toast.makeText(getContext(), "Description needs to be less than 100 characters", Toast.LENGTH_LONG);
                    errorMsg.show();
                }
            }
        });
        return dialog;
    }
}
