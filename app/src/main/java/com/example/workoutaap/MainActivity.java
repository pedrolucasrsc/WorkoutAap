package com.example.workoutaap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "WorkoutApp";
    private int[] squat_w = new int[3];
    private int[] squat_r = new int[3];
    private int[] pullup_w = new int[3];
    private int[] pullup_r = new int[3];
    private int[] dips_w = new int[3];
    private int[] dips_r = new int[3];
    private WorkoutAapOpenHelper openHelper = new WorkoutAapOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        registerWorkout();
        getLastWorkout();
        Log.d(TAG, "squat_w[0] = " + squat_w[0] + " dips_r[2] = " + dips_r[2]);
    }

    public void registerWorkout() {
        SQLiteDatabase database = openHelper.getWritableDatabase();

        ContentValues databaseEntry = new ContentValues();

        // timestamp, day, hour
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timestamp = now.format(timestampFormatter);
        String day = now.format(dayFormatter);
        String hour = now.format(hourFormatter);

        databaseEntry.put(WorkoutAapOpenHelper.KEY_TIMESTAMP, timestamp);
        databaseEntry.put(WorkoutAapOpenHelper.KEY_DAY, day);
        databaseEntry.put(WorkoutAapOpenHelper.KEY_HOUR, hour);
        databaseEntry.put(WorkoutAapOpenHelper.KEY_EXSETREP, "60 12 60 8 60 6 63 10 63 8 63 6 63 10 63 9 63 8");

        database.insert(WorkoutAapOpenHelper.TABLE_NAME, null, databaseEntry);
    }

    public void getLastWorkout() {
        SQLiteDatabase database = openHelper.getReadableDatabase();

        String query = "SELECT * FROM " + WorkoutAapOpenHelper.TABLE_NAME +
                " ORDER BY " + WorkoutAapOpenHelper.KEY_ID + "  DESC LIMIT 1";

        Cursor cursor = database.rawQuery(query, null);
        if (cursor == null) return;

        String exsetrep = cursor.getString(cursor.getColumnIndexOrThrow(WorkoutAapOpenHelper.KEY_EXSETREP));
        String[] exsetrep_split = exsetrep.trim().split("\\s+");
        for (int i = 0, j = 0; j < 3; i += 2, j++) {
            squat_w[j] = Integer.parseInt(exsetrep_split[i]);
            squat_r[j] = Integer.parseInt(exsetrep_split[i+1]);
            pullup_w[j] = Integer.parseInt(exsetrep_split[i+6]);
            pullup_r[j] = Integer.parseInt(exsetrep_split[i+7]);
            dips_w[j] = Integer.parseInt(exsetrep_split[i+12]);
            dips_r[j] = Integer.parseInt(exsetrep_split[i+13]);
        }
    }
}