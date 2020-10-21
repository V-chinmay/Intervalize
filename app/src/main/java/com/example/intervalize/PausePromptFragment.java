package com.example.intervalize;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PausePromptFragment extends DialogFragment {

    public PausePromptFragment()
    {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setView(R.layout.paused_dialog);

        return alert.create();
    }
}