package com.example.intervalize;

import android.annotation.SuppressLint;
import android.app.Activity;

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

import java.util.ArrayList;
import java.util.Arrays;

public class RecyclerAdap extends RecyclerView.Adapter<RecyclerAdap.viewholder> {

    public String TAG = this.toString();
    public ArrayList<String[]> slots = new ArrayList<String[]>();
    Activity parentActivity;
    Listeners listeners;
    ListenersMain listenersMain;
    int recyclerLayoutid;



    interface Listeners
    {
        void deleteOnClick(int position);
        void editOnClick(int position);
    }

    interface ListenersMain
    {
        void onSelectedPlan( String selectedPlan);
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
                Log.i(TAG, "RecyclerAdap: using start_plan layout" );
                break;


        }
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(recyclerLayoutid,parent,false);
        return new viewholder(view);
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
                holder.hh_disp.setText(String.format("%d:%d:%d",hhmmss[0],hhmmss[1],hhmmss[2]));
                Handler handler = new Handler();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.progressBar.incrementProgressBy(1);
                        handler.postDelayed(this,1000);

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

