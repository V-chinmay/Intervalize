package com.example.intervalize;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.intervalize.AddRoutine.recyclerAdap;
import static  com.example.intervalize.AddRoutine.recyclerView;


public class TimePromptFragment extends DialogFragment {

    Activity parentactivity=null;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  View  view;
    private Button positive_button;
    private Button negative_button;
    private String TAG = this.toString();

    Toast toast;
    public int selected_hours = 0;
    public int  selected_minutes = 0;
    public int selected_seconds = 0;
    public String selected_label = " ";

    String SAVE_HOURS ="hours";
    String SAVE_MINUTES ="minutes";
    String SAVE_SECONDS ="seconds";

    ArrayList<String[]> plans = new ArrayList<String[]>();
    EditText editText;
    Integer selected_id = 0;

    public TimePromptFragment()
    {

    }

    public  TimePromptFragment(Activity parent, Integer slot_id)
    {
        parentactivity = parent;
        selected_id = slot_id;
    }

    public static TimePromptFragment newInstance(String param1, String param2) {
        TimePromptFragment fragment = new TimePromptFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        toast = Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.toast_background);
        TextView toasttext = (TextView) view.findViewById(android.R.id.message);
        toasttext.setTextColor(getResources().getColor(R.color.teal_200));
        toast.setDuration(Toast.LENGTH_SHORT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null)
        {
            selected_hours = savedInstanceState.getInt(SAVE_HOURS);
            selected_minutes = savedInstanceState.getInt(SAVE_MINUTES);
            selected_seconds = savedInstanceState.getInt(SAVE_SECONDS);
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();

        view =  layoutInflater.inflate(R.layout.routine_dialog,null);


        positive_button = (Button) view.findViewById(R.id.positive_button);
        negative_button = (Button) view.findViewById(R.id.negative_button);

        positive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPositiveClick();
            }
        });

        negative_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNegativeClick();
            }
        });

        setupPicker();

        alertDialog.setView(view);

        return alertDialog.create();
    }


    private  void setupPicker()
    {
        NumberPicker numberPickerHour =  view.findViewById(R.id.dialog_hour);
        NumberPicker numberPickerMinute = view.findViewById(R.id.dialog_minute);
        NumberPicker numberPickerSecond  = view.findViewById(R.id.dialog_second);
        editText = view.findViewById(R.id.label);

        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(24);
        numberPickerMinute.setMaxValue(60);
        numberPickerMinute.setMinValue(0);
        numberPickerSecond.setMinValue(0);
        numberPickerSecond.setMaxValue(60);
        numberPickerHour.setValue(selected_hours);
        numberPickerMinute.setValue(selected_minutes);
        numberPickerSecond.setValue(selected_seconds);
        editText.setText(selected_label);


        numberPickerHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selected_hours = newVal;
            }
        });
        numberPickerMinute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selected_minutes = newVal;
            }
        });
        numberPickerSecond.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selected_seconds = newVal;
            }
        });
    }

    private  void onPositiveClick()
    {
        String[] set_element = new String[]{String.valueOf(selected_hours),String.valueOf(selected_minutes),String.valueOf(selected_seconds),editText.getText().toString()};

        if(selected_id!=null)
        {
            recyclerAdap.slots.set(selected_id,set_element);

        }
        else
        {
            recyclerAdap.slots.add(set_element);
        }
        recyclerAdap.notifyDataSetChanged();

        Log.i(TAG, "onPositiveClick: " + Arrays.toString(set_element) + " and size is " + recyclerAdap.slots.size());
        toast.setText("Set Time " + selected_hours + "::" + selected_minutes + "::" + selected_seconds);
        toast.show();
        dismiss();
    }

    private  void onNegativeClick()
    {
        toast.setText("Set Cancelled");
        toast.show();
        dismiss();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_HOURS,selected_hours);
        outState.putInt(SAVE_MINUTES,selected_minutes);
        outState.putInt(SAVE_SECONDS,selected_seconds);
    }


}