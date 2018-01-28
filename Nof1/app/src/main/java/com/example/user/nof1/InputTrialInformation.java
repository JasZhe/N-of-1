package com.example.user.nof1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class InputTrialInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_trial_information);
        EditText editText = (EditText)findViewById(R.id.trialTitle);
        editText.setText("Trial:", TextView.BufferType.EDITABLE);

        editText = findViewById(R.id.trialComparisons);
        editText.setText("Comparison: ", TextView.BufferType.EDITABLE);

    }
    public void inputTrialOutcomes(View view) {
        Intent intent = new Intent(this, InputTrialOutcomes.class);
        /*
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);*/
        startActivity(intent);
    }
}
