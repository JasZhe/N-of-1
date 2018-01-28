package com.example.user.n_of_1;

import android.content.Intent;
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

public class TrialInputsActivity extends AppCompatActivity {
    private TextView title;
    private TextView comparisons;
    private TextView length;
    private TextView notification;

    private ListView listView;
    private ArrayList<String> listItems= new ArrayList<String>();
    private int clickCounter=0;
    private ArrayAdapter<String> adapter;

    private Button addComparisons;
    private Button submitAll;

    private EditText inputComparison;
    private EditText inputTitle;
    private EditText inputNotification;
    private EditText inputTrialLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_inputs);
        setupTextViews();
        setupButtons();
        setupEditTexts();
        setupListViews();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
    }

    private void setupListViews() {
        listView = findViewById(R.id.listViewComparisons);
    }

    private void setupTextViews() {
        title = findViewById(R.id.textViewTitle);
        title.setText("Title: ");

        comparisons = findViewById(R.id.textViewComparisons);
        comparisons.setText("Comparisons: ");

        length = findViewById(R.id.textViewTrialLength);
        length.setText("Length of Trial: ");

        notification = findViewById(R.id.textViewNotification);
        notification.setText("Notification Time: ");
    }

    private void setupButtons() {
        addComparisons = findViewById(R.id.buttonSubmitComparison);
        submitAll = findViewById(R.id.buttonSubmitAll);

        addComparisons.setText("Add Comparison");
        addComparisons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItems();
                inputComparison.setText("");
            }
        });

        submitAll.setText("Submit All");
        submitAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrialInputsActivity.this, TrialOutputsActivity.class);
                intent.putExtra("comparisons", listItems);
                intent.putExtra("num_comparisons", clickCounter);
                intent.putExtra("trial_title", inputTitle.getText().toString());
                intent.putExtra("trial_length", inputTrialLength.getText().toString());
                intent.putExtra("trial_notification_time", inputNotification.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void setupEditTexts() {
        inputComparison = findViewById(R.id.editTextComparison);
        inputTrialLength = findViewById(R.id.editTextTrialLength);
        inputNotification = findViewById(R.id.editTextNotifications);
        inputTitle = findViewById(R.id.editTextTitle);

        inputComparison.setText("");
        inputTrialLength.setText("");
        inputNotification.setText("HH/MM/SS");
        inputTitle.setText("");
        inputTitle.requestFocus();

        inputNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputNotification.getText().toString().equalsIgnoreCase("HH/MM/SS")) {
                    inputNotification.setText("");
                }
            }
        });
    }

    public void addItems() {
        listItems.add(inputComparison.getText().toString());
        clickCounter++;
        adapter.notifyDataSetChanged();
    }
}
