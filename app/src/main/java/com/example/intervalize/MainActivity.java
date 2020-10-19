package com.example.intervalize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String  TAG = this.toString();
    PlansData plansData;
    SQLiteDatabase database;
    Map<String,String> planNameNTime  = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setContentView(R.layout.activity_main);
        setActionBar(toolbar);

    }

    @Override
    protected void onStart() {
        super.onStart();
        readAllPlans();
    }

    void readAllPlans()
    {
        plansData =  new PlansData(this);

        database =  plansData.getReadableDatabase();

        Cursor cursor = database.query(PlansData.MAINTABLENAME,new String[]{PlansData.PLANNAME,PlansData.PLANTIME} ,null,null,null,null,null);
        cursor.moveToFirst();

        while (cursor.moveToNext())
        {
            planNameNTime.put(cursor.getString(0),cursor.getString(1));
            Log.i(TAG, "readAllPlans: table name is " +  cursor.getString(0)  + "--" + cursor.getString(1) );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
        plansData.close();
    }

    public void addNewRoutine(View view)
    {
        Intent addroutine  = new Intent(this,AddRoutine.class);
        startActivity(addroutine);
    }
}