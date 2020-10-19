package com.example.intervalize;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PlansData extends SQLiteOpenHelper {

    static String  name = "PlansData";
    static int version = 1;
    public static String MAINTABLENAME = "PLANS";
    public static String PLANNAME = "PLANNAME";
    public static String PLANTIME = "PLANTIME";

    public PlansData(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("Create table "+  MAINTABLENAME + "(" + "_id INTEGER Primary key autoincrement" +
                "," + PLANNAME + " Text " +
                "," + PLANTIME + " Text "+
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
