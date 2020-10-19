package com.example.intervalize;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class GetNamePromptFragment extends DialogFragment {

    public EditText nameField;
    public Button postiveButton;
    public Button negativeButton;
    private Listeners listeners;

    interface Listeners
    {
        void onPositiveDone(String planName);
        void onNegativeDone();
    }

    public GetNamePromptFragment()
    {

    }

    public GetNamePromptFragment(Context context)
    {
        listeners = (Listeners) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View dialog_view = layoutInflater.inflate(R.layout.done_get_name,null);

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        nameField = (EditText) dialog_view.findViewById(R.id.plan_name);

        postiveButton = (Button) dialog_view.findViewById(R.id.positive_button_done);

        negativeButton = (Button) dialog_view.findViewById(R.id.negative_button_done);

        postiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listeners.onPositiveDone(nameField.getText().toString());
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listeners.onNegativeDone();
            }
        });

        dialog.setView(dialog_view);

        return dialog.create();
    }
}