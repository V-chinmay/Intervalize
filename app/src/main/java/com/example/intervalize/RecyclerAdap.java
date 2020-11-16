package com.example.intervalize;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class RecyclerAdap extends RecyclerView.Adapter<RecyclerAdap.viewholder> {

    public String TAG = this.toString();
    public static String SLOTPROGRESS = "slotprogress";
    public static  int presentPosition = 0 ;
    public int presentSettime = 0;
    public ArrayList<String[]> slots = new ArrayList<String[]>();
    public static   int slotProgress[];
    Activity parentActivity;
    Listeners listeners;
    ListenersMain listenersMain;
    AfterTimeOut afterTimeOut;
    int recyclerLayoutid;
    public boolean paused = false;
    public static boolean restarted = false;




    interface Listeners
    {
        void deleteOnClick(int position);
        void editOnClick(int position);
    }

    interface ListenersMain
    {
        void onSelectedPlan( String selectedPlan);
    }

    interface AfterTimeOut
    {
        void afterTimeOut();
    }

    public RecyclerAdap(Activity parent, int layoutid)
    {

        parentActivity= parent;
        recyclerLayoutid = layoutid;

        switch (recyclerLayoutid)
        {
            case R.layout.recycler_lay:
                listeners = (Listeners) parent;
                break;
            case R.layout.recycler_layout_main:
                listenersMain = (ListenersMain) parent;
                Log.i(TAG, "RecyclerAdap: using main layout" );
                break;
            case R.layout.recycler_layout_start_plan:
                afterTimeOut = (AfterTimeOut) parent;
                Log.i(TAG, "RecyclerAdap: using start_plan layout" );
                break;
        }
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(recyclerLayoutid,parent,false);
        viewholder vh  = new viewholder(view);
        if(vh==null)
        {
            Log.i(TAG, "onCreateViewHolder: vh is null ");
        }
        else 
        {
            Log.i(TAG, "onCreateViewHolder: vh is not null");
        }
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        switch(recyclerLayoutid)
        {
            case R.layout.recycler_lay:
                holder.labeldisp.setText(slots.get(position)[3]);
                holder.hh_disp.setText(slots.get(position)[0]);
                holder.mm_disp.setText(slots.get(position)[1]);
                holder.ss_disp.setText(slots.get(position)[2]);
                holder.delete_butt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listeners.deleteOnClick(position);
                    }
                });
                holder.edit_butt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listeners.editOnClick(position);
                    }
                });
                break;

            case R.layout.recycler_layout_main:
                Log.i(TAG, "onBindViewHolder: plan name " + MainActivity.planNames[position] + " : " + MainActivity.planNameNTime.get(MainActivity.planNames[position]));
                holder.mainView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listenersMain.onSelectedPlan(MainActivity.planNames[position]);
                    }
                });
                holder.labeldisp.setText(MainActivity.planNames[position]);
                holder.hh_disp.setText(MainActivity.planNameNTime.get(MainActivity.planNames[position]));
                break;

            case R.layout.recycler_layout_start_plan:
                int[] hhmmss = StartPlanActivity.slotslabelntime.get(StartPlanActivity.slotsLabels[position]);

                Log.i(TAG, "onBindViewHolder: slot name " + StartPlanActivity.slotsLabels[position] + " : " + Arrays.toString(hhmmss));
                holder.labeldisp.setText(StartPlanActivity.slotsLabels[position]);
                holder.hh_disp.setText(String.format("%02d:%02d:%02d",hhmmss[0],hhmmss[1],hhmmss[2]));

                holder.mainView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!restarted)
                        {
                            slotProgress[position] = (hhmmss[0] * 3600) + (hhmmss[1] * 60) + (hhmmss[2]);
                            presentSettime = slotProgress[position];
                            holder.progressBar.setMax(slotProgress[position]);
                        }
                        else
                        {
                            restarted=false;
                        }

                        Handler handler = new Handler();

                        holder.mainView.setClickable(false);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "onBindViewHolder: after runnable set time for " + StartPlanActivity.slotsLabels[position] +slotProgress[position]);

                                if(slotProgress[position] >0)
                                {
                                    handler.postDelayed(this,1000);

                                    if(!paused)
                                    {
                                        slotProgress[position]--;
                                        int hours =(slotProgress[position]/3600);
                                        int minutes=((slotProgress[position]%3600)/60);
                                        int seconds=((slotProgress[position]%3600)%60);

                                        holder.hh_disp.setText(String.format("%02d:%02d:%02d",hours,minutes,seconds));
                                        holder.progressBar.setProgress(presentSettime - slotProgress[position]);
                                    }

                                }
                                else
                                    {
                                        Log.i(TAG, "run: Timout occured for " + StartPlanActivity.slotsLabels[position]);
                                        presentPosition++;
                                        holder.progressBar.setProgress(holder.progressBar.getMax());
                                        afterTimeOut.afterTimeOut();

                                    }

                            }
                        });
                    }
                });

                break;
        }

    }




    @Override
    public int getItemCount() {

        switch (recyclerLayoutid)
        {
            case R.layout.recycler_layout_main:
                Log.i(TAG, "getItemCount: size is " + MainActivity.planNameNTime.size());
                return MainActivity.planNameNTime.size();

            case R.layout.recycler_lay:
                return slots.size();

            case R.layout.recycler_layout_start_plan:
                Log.i(TAG, "getItemCount: slots size is " + StartPlanActivity.slotslabelntime.size());
                slotProgress = new int[StartPlanActivity.slotslabelntime.size()];
                return StartPlanActivity.slotslabelntime.size();

        }

        return 0;


    }




    public class viewholder extends RecyclerView.ViewHolder
    {
        public TextView labeldisp;
        public TextView hh_disp;
        public TextView mm_disp;
        public TextView ss_disp;
        public ImageButton edit_butt;
        public ImageButton delete_butt;
        public View mainView;
        public  ProgressBar progressBar;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            mainView= itemView;
            switch (recyclerLayoutid)
            {

                case R.layout.recycler_lay:
                    labeldisp = (TextView) itemView.findViewById(R.id.labeldisp);
                    hh_disp = (TextView) itemView.findViewById(R.id.hh_disp);
                    mm_disp = (TextView) itemView.findViewById(R.id.mm_disp);
                    ss_disp = (TextView) itemView.findViewById(R.id.ss_disp);
                    edit_butt = (ImageButton) itemView.findViewById(R.id.editbutt);
                    delete_butt = (ImageButton) itemView.findViewById(R.id.deletebutt);
                    break;

                case R.layout.recycler_layout_main:
                    labeldisp = (TextView) itemView.findViewById(R.id.totaltime);
                    hh_disp = (TextView) itemView.findViewById(R.id.plan_name_disp);
                    break;

                case R.layout.recycler_layout_start_plan:
                    labeldisp = (TextView) itemView.findViewById(R.id.label_disp_start_plan_recycler);
                    hh_disp = (TextView) itemView.findViewById(R.id.time_left_disp_start_plan_recycler);
                    progressBar = (ProgressBar) itemView.findViewById(R.id.slot_progress);
                    break;
            }

        }
    }
}

