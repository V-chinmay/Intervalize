package com.example.intervalize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setContentView(R.layout.activity_main);
        setActionBar(toolbar);
    }



    public void addNewRoutine(View view)
    {
        Intent addroutine  = new Intent(this,AddRoutine.class);
        startActivity(addroutine);
    }
}