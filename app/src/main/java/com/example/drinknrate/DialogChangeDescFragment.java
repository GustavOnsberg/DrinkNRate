package com.example.drinknrate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogChangeDescFragment extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText descInput = new EditText(getActivity());
        TextView desc = (TextView) getActivity().findViewById(R.id.drinkDesc);
        descInput.setText(desc.getText().toString());
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        descInput.setLayoutParams(layout);
        builder.setView(descInput);
        builder.setTitle("Change Description")
                .setMessage("Change the description for the drink")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView descText = (TextView) getActivity().findViewById(R.id.drinkDesc);
                        descText.setText(descInput.getText().toString());
                        getActivity().findViewById(R.id.addDescButton).setVisibility(View.GONE);
                        getActivity().findViewById(R.id.changeDesc).setVisibility(View.VISIBLE);
                        Toast finalMsg = Toast.makeText(getContext(),"Description is changed", Toast.LENGTH_SHORT);
                        finalMsg.show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
