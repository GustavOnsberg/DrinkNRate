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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogFragment extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText descInput = new EditText(getActivity());
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        descInput.setLayoutParams(layout);
        builder.setView(descInput);
        builder.setTitle("Description")
                .setMessage("TestyTest")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView descText = (TextView) getActivity().findViewById(R.id.drinkDesc);
                        descText.setText(descInput.getText().toString());
                        getActivity().findViewById(R.id.addDescButton).setVisibility(View.GONE);
                        getActivity().findViewById(R.id.changeDesc).setVisibility(View.VISIBLE);
                    }
                });
        return builder.create();
    }
}
