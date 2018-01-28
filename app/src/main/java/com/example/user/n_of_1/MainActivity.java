package com.example.user.n_of_1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button create_new;
    private Button view_previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupButtons();
    }

    private void setupButtons() {
        create_new = findViewById(R.id.button_create);
        view_previous = findViewById(R.id.button_view_previous);

        create_new.setText("Enter New Trial");
        view_previous.setText("View Previous Trials");

        create_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inputTrial = new Intent(MainActivity.this, TrialInputsActivity.class);
                startActivity(inputTrial);
            }
        });
    }
}
