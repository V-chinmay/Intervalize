package com.example.intervalize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StartPlanActivity extends AppCompatActivity {

    public String selectedPlan ;

    private String TAG =this.toString();
    public static Map<String,int[]> slotslabelntime = new LinkedHashMap<String,int[]>();

    public static  String[] slotsLabels;
    private  RecyclerView recyclerView;



    public  static String EXTRA_PLAN_NAME = "planname";
    private  PlansData plansData;
    private SQLiteDatabase database;
    private Cursor cursor;

    private  RecyclerAdap recyclerAdap = new RecyclerAdap(this,R.layout.recycler_layout_start_plan);

    public void readAllSlots(String planName)
    {
        slotslabelntime = new LinkedHashMap<String, int[]>();
        plansData = new PlansData(this);
        database =  plansData.getReadableDatabase();

        cursor =  database.query(selectedPlan,new String[]{AddRoutine.DB_COLUMN_0,AddRoutine.DB_COLUMN_1,AddRoutine.DB_COLUMN_2,AddRoutine.DB_COLUMN_3},null,null,null,null,"_id ASC");

        if(cursor.moveToFirst())
        {
            slotslabelntime.put(cursor.getString(0),new int[]{cursor.getInt(1),cursor.getInt(2),cursor.getInt(3)});
            Log.i(TAG, "readAllSlots: read slot label is "+cursor.getString(0) + " and time is " + Arrays.toString(slotslabelntime.get(cursor.getString(0))));
        }

        while(cursor.moveToNext())
        {
            slotslabelntime.put(cursor.getString(0),new int[]{cursor.getInt(1),cursor.getInt(2),cursor.getInt(3)});
            Log.i(TAG, "readAllSlots: read slot label is "+cursor.getString(0) + " and time is " + Arrays.toString(slotslabelntime.get(cursor.getString(0))));
        }


        slotsLabels = new String[slotslabelntime.size()];
        slotslabelntime.keySet().toArray(slotsLabels);
        recyclerAdap.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_plan);




        selectedPlan = getIntent().getStringExtra(EXTRA_PLAN_NAME);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarstartplan);
        toolbar.setTitle(selectedPlan);
        toolbar.inflateMenu(R.menu.menu_start_plan);
        DividerItemDecoration dividerItemDecoration= new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);

//        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_background));

       recyclerView = (RecyclerView) findViewById(R.id.planprogress);

//        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdap);

        setSupportActionBar(toolbar);



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_start_plan,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.pause_butt)
        {
            PausePromptFragment pausePromptFragment = new PausePromptFragment();
            pausePromptFragment.show(getSupportFragmentManager(),"resumeprompt");
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        readAllSlots(selectedPlan);
        Handler handler = new Handler();


    }

    @Override
    protected void onStop() {
        if(database!=null )
        {
            database.close();
            cursor.close();
            plansData.close();
            Log.i(TAG, "onStop: closed dbs");

        }

        super.onStop();
    }
}