package com.example.intervalize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void addNewRoutine(View view)
    {
        Intent addroutine  = new Intent(this,AddRoutine.class);
        startActivity(addroutine);
    }
}