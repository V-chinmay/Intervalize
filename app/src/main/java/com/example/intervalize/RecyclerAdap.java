package com.example.intervalize;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdap extends RecyclerView.Adapter<RecyclerAdap.viewholder> {

    public ArrayList<String[]> slots = new ArrayList<String[]>();
    Activity parentActivity;
    Listeners listeners;


    interface Listeners
    {
        void deleteOnClick(int position);
        void editOnClick(int position);
    }

    public RecyclerAdap(Activity parent)
    {
        parentActivity= parent;
        listeners = (Listeners) parent;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_lay,parent,false);
        viewholder vh = new viewholder(view);
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    public class viewholder extends RecyclerView.ViewHolder
    {
        public TextView labeldisp;
        public TextView hh_disp;
        public TextView mm_disp;
        public TextView ss_disp;
        public ImageButton edit_butt;
        public ImageButton delete_butt;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            labeldisp = (TextView) itemView.findViewById(R.id.labeldisp);
            hh_disp = (TextView) itemView.findViewById(R.id.hh_disp);
            mm_disp = (TextView) itemView.findViewById(R.id.mm_disp);
            ss_disp = (TextView) itemView.findViewById(R.id.ss_disp);
            edit_butt = (ImageButton) itemView.findViewById(R.id.editbutt);
            delete_butt = (ImageButton) itemView.findViewById(R.id.deletebutt);

        }
    }
}

