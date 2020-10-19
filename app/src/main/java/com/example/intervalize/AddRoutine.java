package com.example.intervalize;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.LocusId;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import java.sql.SQLData;

public class AddRoutine extends AppCompatActivity implements RecyclerAdap.Listeners,GetNamePromptFragment.Listeners {

    public  static  RecyclerAdap recyclerAdap = null;
    public  static  RecyclerView recyclerView = null;
    private GetNamePromptFragment getNamePromptFragment;
    private  String TAG = this.toString();
    PlansData plansData;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_routine);


        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_add_routine);


        recyclerAdap = new RecyclerAdap(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_background));

        recyclerView = (RecyclerView) findViewById(R.id.slots_recycler);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(recyclerAdap);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add_routine,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.done_toolbar)
        {
            onDoneAddingSlots();
        }
        return true;
    }

    public void onAddSlot(View view)
    {
        TimePromptFragment timePromptFragment = new TimePromptFragment(this,null);

        timePromptFragment.show(getSupportFragmentManager(),"gettime");
    }


    public void  onDoneAddingSlots()
    {
        getNamePromptFragment = new GetNamePromptFragment(this);
        getNamePromptFragment.show(getSupportFragmentManager(),"getname");
    }

    public  void  addPlanToDB(String planName)
    {
        if(recyclerAdap.slots.size()>0)
        {
            Integer totalPlanTime = 0;
            plansData = new PlansData(this);
            db = plansData.getWritableDatabase();

            db.execSQL("CREATE TABLE " + planName + "("
                            +"_id INTEGER PRIMARY KEY AUTOINCREMENT" +
                            ",LABEL TEXT" +
                            ",HH INT" +
                            ",MM INT" +
                            ",SS INT" +
                            ");"
                    );

            for (String[] slot:recyclerAdap.slots)
            {
                    ContentValues contentValues =  new ContentValues();
                    contentValues.put("HH",Integer.valueOf(slot[0]));
                    contentValues.put("MM",Integer.valueOf(slot[1]));
                    contentValues.put("SS",Integer.valueOf(slot[2]));
                    contentValues.put("LABEL",slot[3]);
                    totalPlanTime+=(Integer.valueOf(slot[0])*3600 + Integer.valueOf(slot[1])*60 + Integer.valueOf(slot[2]));

                    Log.i(TAG, "addPlanToDB: adding " + slot[0] +  ":" +  slot[1] + ":" +slot[2] + ":" + slot[3] );
                    db.insert(planName, null ,contentValues);
            }

            String finalTotalTime = String.format("%d:%d:%d",( (totalPlanTime)/3600),((((totalPlanTime)%3600))/60),(((((totalPlanTime)%3600))%60)));

            ContentValues maintableentry =  new ContentValues();
            maintableentry.put(PlansData.PLANNAME,planName);
            maintableentry.put(PlansData.PLANTIME,finalTotalTime);


            db.insert(PlansData.MAINTABLENAME,null,maintableentry);
            Log.i(TAG, "addPlanToDB: added plan to db- table name is  "+ planName + "Total time is " + finalTotalTime);

        }
        else
        {
            Toast.makeText(this,"Plans cant be empty",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void deleteOnClick(int position) {
        recyclerAdap.slots.remove(position);
        recyclerAdap.notifyDataSetChanged();
    }

    @Override
    public void editOnClick(int position) {
        TimePromptFragment timePromptFragment = new TimePromptFragment(this,position);
        timePromptFragment.selected_hours = Integer.valueOf(recyclerAdap.slots.get(position)[0]);
        timePromptFragment.selected_minutes = Integer.valueOf(recyclerAdap.slots.get(position)[1]);
        timePromptFragment.selected_seconds = Integer.valueOf(recyclerAdap.slots.get(position)[2]);
        timePromptFragment.selected_label = recyclerAdap.slots.get(position)[3];
        timePromptFragment.show(getSupportFragmentManager(),"editslot");
    }


    @Override
    public void onPositiveDone(String planName) {
        if(planName.isEmpty())
        {
            Toast.makeText(this,"Plan name can't be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            addPlanToDB(planName);
            getNamePromptFragment.dismiss();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        plansData.close();
    }

    @Override
    public void onNegativeDone() {
        getNamePromptFragment.dismiss();
    }
}