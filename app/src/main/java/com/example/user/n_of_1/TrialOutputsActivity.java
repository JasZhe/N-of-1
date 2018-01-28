package com.example.user.n_of_1;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class TrialOutputsActivity extends AppCompatActivity {
    private PendingIntent pendingIntent;

    private Button submitAll;
    private Button submitOutcome;

    private TextView textOutcomes;

    private EditText editOutcomes;

    private ListView listOutcomes;
    private ArrayList<String> listItems= new ArrayList<String>();
    private int clickCounter=0;
    private ArrayAdapter<String> adapter;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private String trial_title;
    private String trial_length;
    private String trial_notification_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_outputs);
        setupButtons();
        setupEditTexts();
        setupListViews();
        setupTextViews();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listOutcomes.setAdapter(adapter);

        //Intent alarmIntent = new Intent(TrialOutputsActivity.this, AlarmReceiver.class);
        //pendingIntent = PendingIntent.getBroadcast(TrialOutputsActivity.this, 0, alarmIntent, 0);

        int comparisonCount = (int) getIntent().getExtras().getInt("num_comparisons");
        trial_title = getIntent().getExtras().getString("trial_title");
        trial_length = getIntent().getExtras().getString("trial_length");
        trial_notification_time = getIntent().getExtras().getString("trial_notification_time");
        Log.i("TITLE", trial_title + trial_length + trial_notification_time);
        ArrayList<String> comparisons = getIntent().getExtras().getStringArrayList("comparisons");
        Log.i("ARRAYLIST", comparisons.get(0).toString());

        int hour = Integer.parseInt(trial_notification_time.substring(0, 2));
        Log.i("HOUR", "" + hour);
        int minute = Integer.parseInt(trial_notification_time.substring(3, 5));
        Log.i("MINUTE", "" + minute);
        int second = Integer.parseInt(trial_notification_time.substring(6, 8));
        Log.i("SECOND", "" + second);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.AM_PM,  Calendar.AM);

        Intent myIntent = new Intent(TrialOutputsActivity.this, MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(TrialOutputsActivity.this, 0, myIntent,0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

        sharedPref = this.getSharedPreferences(trial_title + trial_length + trial_notification_time.replace("/", ""), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        // Store everything from TrialInputsActivity (parent activity)
        editor.putInt("num_comparisons", comparisonCount);
        editor.putString("trial_title", trial_title);
        editor.putString("trial_length", trial_length);
        editor.putString("trial_notification_time", trial_notification_time);

        // Store each comparison as ("comparison" + index, comparison)
        for(int i = 0; i < comparisonCount; i++) {
            editor.putString("comparison" + i, comparisons.get(i));
        }
        editor.commit();
    }

    private void setupListViews() {
        listOutcomes = findViewById(R.id.listView);
    }

    private void setupTextViews() {
        textOutcomes = findViewById(R.id.textViewOutputs);
        textOutcomes.setText("Outcomes: ");
    }

    private void setupEditTexts() {
        editOutcomes = findViewById(R.id.editTextOutputs);
        editOutcomes.setText("");
    }

    private void setupButtons() {
        submitAll = findViewById(R.id.buttonSubmitAll);
        submitOutcome = findViewById(R.id.buttonOutputs);

        submitAll.setText("Finish");
        submitOutcome.setText("Submit");

        submitOutcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItems();
                editOutcomes.setText("");
            }
        });

        submitAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                //startAt(6, 35);
                //start();
            }
        });
    }

    public void addItems() {
        listItems.add(editOutcomes.getText().toString());
        clickCounter++;
        adapter.notifyDataSetChanged();
    }

    private void saveData() {
        // Store each outcome as ("outcome" + index, outcome)
        for(int i = 0; i < clickCounter; i++) {
            editor.putString("outcome" + i, listItems.get(i));
        }
        editor.putInt("num_outcomes", clickCounter);
        editor.commit();

        sharedPref = this.getSharedPreferences(this.getPackageName() + "all_trials", Context.MODE_PRIVATE);
        int num_trials = sharedPref.getAll().size();
        num_trials++;
        Log.i("Info", "" + num_trials);
        editor = sharedPref.edit();
        editor.putString("" + num_trials, trial_title + trial_length + trial_notification_time);
        editor.commit();

        sharedPref = this.getSharedPreferences(this.getPackageName() + "all_trials", Context.MODE_PRIVATE);
        String test_trial_name = sharedPref.getString("" + num_trials, "");
        sharedPref = this.getSharedPreferences(trial_title + trial_length + trial_notification_time.replace("/", ""), Context.MODE_PRIVATE);
        String test_trial_title = sharedPref.getString("trial_title", "");
        Log.i("GRABBING PREFERENCES", test_trial_name + ", TITLE: " + test_trial_title);
    }

    public void start() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 8000;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    public void startAt(int hour, int min) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 * 20;

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);

        /* Repeating on every 20 minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }
}
