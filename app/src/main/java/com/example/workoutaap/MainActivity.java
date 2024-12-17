package com.example.workoutaap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private TextView squat1;
    private TextView squat2;
    private TextView squat3;
    private TextView pullup1;
    private TextView pullup2;
    private TextView pullup3;
    private TextView dips1;
    private TextView dips2;
    private TextView dips3;
    private EditText editExSetRep;
    private Button btnSaveWorkout;
    private WorkoutAapOpenHelper openHelper = new WorkoutAapOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // get TextViews
        squat1 = (TextView) findViewById(R.id.squat1);
        squat2 = (TextView) findViewById(R.id.squat2);
        squat3 = (TextView) findViewById(R.id.squat3);
        pullup1 = (TextView) findViewById(R.id.pullup1);
        pullup2 = (TextView) findViewById(R.id.pullup2);
        pullup3 = (TextView) findViewById(R.id.pullup3);
        dips1 = (TextView) findViewById(R.id.dips1);
        dips2 = (TextView) findViewById(R.id.dips2);
        dips3 = (TextView) findViewById(R.id.dips3);
        editExSetRep = (EditText) findViewById(R.id.edit_exsetrep);
        btnSaveWorkout = (Button) findViewById(R.id.btn_save_workout);
        btnSaveWorkout.setOnClickListener(v -> saveWorkout());
        updateLastWorkout();
//        Log.d(TAG, "squat_w[0] = " + squat_w[0] + " dips_r[2] = " + dips_r[2]);
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

        long result = database.insert(WorkoutAapOpenHelper.TABLE_NAME, null, databaseEntry);
        if (result == -1) {
            Log.e("Database", "Error inserting");
        } else {
            Log.d("Database", "Inserted in database a training session" + result);
        }
    }

    public void getLastWorkout() {
        SQLiteDatabase database = openHelper.getReadableDatabase();

        String query = "SELECT * FROM " + WorkoutAapOpenHelper.TABLE_NAME +
                " ORDER BY " + WorkoutAapOpenHelper.KEY_ID + "  DESC LIMIT 1";

        Cursor cursor = database.rawQuery(query, null);
        if (cursor == null) return;
        cursor.moveToFirst();

        String exsetrep = cursor.getString(cursor.getColumnIndexOrThrow(WorkoutAapOpenHelper.KEY_EXSETREP));
        String[] exsetrep_split = exsetrep.trim().split("\\s+");
        for (int i = 0, j = 0; j < 3; i += 2, j++) {
            if (i < exsetrep_split.length)
                squat_w[j] = Integer.parseInt(exsetrep_split[i]);
            else squat_w[j] = 0;
            if (i+1 < exsetrep_split.length)
                squat_r[j] = Integer.parseInt(exsetrep_split[i+1]);
            else squat_r[j] = 0;
            if (i+6 < exsetrep_split.length)
                pullup_w[j] = Integer.parseInt(exsetrep_split[i+6]);
            else pullup_w[j] = 0;
            if (i+7 < exsetrep_split.length)
                pullup_r[j] = Integer.parseInt(exsetrep_split[i+7]);
            else pullup_r[j] = 0;
            if (i+12< exsetrep_split.length)
                dips_w[j] = Integer.parseInt(exsetrep_split[i+12]);
            else dips_w[j] = 0;
            if (i+13 < exsetrep_split.length)
                dips_r[j] = Integer.parseInt(exsetrep_split[i+13]);
            else dips_r[j] = 0;
        }
    }

    private void saveWorkout() {
        String exsetrep = editExSetRep.getText().toString().trim();
        if (!exsetrep.isEmpty()) {
            WorkoutAapOpenHelper dbHelper = new WorkoutAapOpenHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(WorkoutAapOpenHelper.KEY_EXSETREP, exsetrep);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            values.put(WorkoutAapOpenHelper.KEY_TIMESTAMP, now.format(timestampFormatter));

            database.insert(WorkoutAapOpenHelper.TABLE_NAME, null, values);
            database.close();

            Toast.makeText(this, "Training session saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please, fill the field", Toast.LENGTH_SHORT).show();
        }
        updateLastWorkout();
    }

    private void updateLastWorkout() {
        getLastWorkout();
        squat1.setText("1. " + squat_w[0] + " kg " + squat_r[0] + " reps");
        squat2.setText("2. " + squat_w[1] + " kg " + squat_r[1] + " reps");
        squat3.setText("3. " + squat_w[2] + " kg " + squat_r[2] + " reps");
        pullup1.setText("1. " + pullup_w[0] + " kg " + pullup_r[0] + " reps");
        pullup2.setText("2. " + pullup_w[1] + " kg " + pullup_r[1] + " reps");
        pullup3.setText("3. " + pullup_w[2] + " kg " + pullup_r[2] + " reps");
        dips1.setText("1. " + dips_w[0] + " kg " + dips_r[0] + " reps");
        dips2.setText("2. " + dips_w[1] + " kg " + dips_r[1] + " reps");
        dips3.setText("3. " + dips_w[2] + " kg " + dips_r[2] + " reps");
    }
}