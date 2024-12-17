package com.example.workoutaap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkoutAapOpenHelper extends SQLiteOpenHelper {

    public WorkoutAapOpenHelper (Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "workoutaap";
    // id, day, hour, timestamp, exsetrep
    public static final String TABLE_NAME = "workouts";
    public static final String KEY_ID = "id";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_DAY = "day";
    public static final String KEY_HOUR = "hour";
    public static final String KEY_EXSETREP = "exsetrep";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID +
            " INTEGER PRIMARY KEY," + KEY_DAY + " TEXT, " + KEY_DAY + " TEXT, " + KEY_HOUR + " TEXT, " + KEY_TIMESTAMP +
            " TEXT);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + KEY_EXSETREP + " TEXT;");
        }
    }
}
