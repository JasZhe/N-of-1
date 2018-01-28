package com.example.user.n_of_1;

import android.content.Context;
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

import java.util.ArrayList;

public class TrialOutputsActivity extends AppCompatActivity {
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

        int comparisonCount = (int) getIntent().getExtras().getInt("num_comparisons");
        trial_title = getIntent().getExtras().getString("trial_title");
        trial_length = getIntent().getExtras().getString("trial_length");
        trial_notification_time = getIntent().getExtras().getString("trial_notification_time");
        Log.i("TITLE", trial_title + trial_length + trial_notification_time);
        ArrayList<String> comparisons = getIntent().getExtras().getStringArrayList("comparisons");
        Log.i("ARRAYLIST", comparisons.get(0).toString());

        sharedPref = this.getSharedPreferences(trial_title + trial_length + trial_notification_time, Context.MODE_PRIVATE);
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
            }
        });

        submitAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
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
        sharedPref = this.getSharedPreferences(trial_title + trial_length + trial_notification_time, Context.MODE_PRIVATE);
        String test_trial_title = sharedPref.getString("trial_title", "");
        Log.i("GRABBING PREFERENCES", test_trial_name + ", TITLE: " + test_trial_title);

    }
}
