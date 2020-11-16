package com.example.intervalize;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


public class PausePromptFragment extends DialogFragment {


    Listeners listeners;
    String TAG = this.toString();

    interface  Listeners
    {
        void onResumePressed();
        void onEndPressed();
    }

    public PausePromptFragment()
    {


    }


    public PausePromptFragment(Activity activity)
    {
        listeners = (Listeners) activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.paused_dialog,null);

        Log.i(TAG, "onCreateDialog: pause prompt being shown");

        ImageButton resumeButton  = (ImageButton) view.findViewById(R.id.resume_plan);
        Button endButton = (Button) view.findViewById(R.id.end_plan);
        
        if(resumeButton==null | endButton==null | view==null )
        {
            Log.i(TAG, "onCreateDialog: views were null");
        }

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick:  resuming the plan");
                listeners.onResumePressed();
                dismiss();
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick:  ending the plan");
                listeners.onEndPressed();
                dismiss();
            }
        });



        alert.setView(view);


        return alert.create();
    }
}