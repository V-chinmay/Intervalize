package com.example.intervalize;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class AddRoutine extends AppCompatActivity implements RecyclerAdap.Listeners{

    public  static  RecyclerAdap recyclerAdap = null;
    public  static  RecyclerView recyclerView = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine);
        recyclerAdap = new RecyclerAdap(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_background));

        recyclerView = (RecyclerView) findViewById(R.id.slots_recycler);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(recyclerAdap);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onAddSlot(View view)
    {
        TimePromptFragment timePromptFragment = new TimePromptFragment(this,null);

        timePromptFragment.show(getSupportFragmentManager(),"gettime");
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
}