package com.example.intervalize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements RecyclerAdap.ListenersMain{

    String  TAG = this.toString();
    PlansData plansData;
    SQLiteDatabase database;
    public static Map<String,String> planNameNTime  = new LinkedHashMap<String, String>();
    public static String[] planNames;
    RecyclerView routineList;
    RecyclerAdap recyclerAdapMain;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setContentView(R.layout.activity_main);
        setActionBar(toolbar);

        DividerItemDecoration dividerItemDecoration= new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);

//        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_background));

        routineList = (RecyclerView) findViewById(R.id.routinelist);
        recyclerAdapMain = new RecyclerAdap(this,R.layout.recycler_layout_main);

//        routineList.addItemDecoration(dividerItemDecoration);

        routineList.setAdapter(recyclerAdapMain);
        routineList.setLayoutManager(new LinearLayoutManager(this));

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

        cursor= database.query(PlansData.MAINTABLENAME,new String[]{PlansData.PLANNAME,PlansData.PLANTIME} ,null,null,null,null,"_id ASC");

        if(cursor.moveToFirst())
        {
            planNameNTime.put(cursor.getString(0),cursor.getString(1));
            Log.i(TAG, "readAllPlans: table name is " + cursor.getString(0)  + "--" + planNameNTime.get(cursor.getString(0)) );

        }


        while (cursor.moveToNext())
        {
            planNameNTime.put(cursor.getString(0),cursor.getString(1));
            Log.i(TAG, "readAllPlans: table name is " + cursor.getString(0)  + "--" + planNameNTime.get(cursor.getString(0)) );
        }
        if(planNameNTime.size()!=0)
        {
            planNames = new String[planNameNTime.size()];
            planNameNTime.keySet().toArray(planNames);
            Log.i(TAG, "readAllPlans: size is " + planNameNTime.size() + "and the plannames" + Arrays.toString(planNames));
            recyclerAdapMain.notifyDataSetChanged();

        }

    }

    @Override
    protected void onStop() {


        super.onStop();

    }

    @Override
    protected void onDestroy() {
        if(database!=null)
        {
            database.close();
            plansData.close();
            cursor.close();
            Log.i(TAG, "onStop: closed dbs");
        }


        super.onDestroy();

    }

    public void addNewRoutine(View view)
    {
        Intent addroutine  = new Intent(this,AddRoutine.class);
        startActivity(addroutine);
    }

    @Override
    public void onSelectedPlan(String selectedPlan) {
        Intent intent = new Intent(this,StartPlanActivity.class);
        intent.putExtra(StartPlanActivity.EXTRA_PLAN_NAME,selectedPlan);
        startActivity(intent);
    }
}