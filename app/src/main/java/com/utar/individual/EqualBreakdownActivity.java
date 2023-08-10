package com.utar.individual;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;


public class EqualBreakdownActivity extends AppCompatActivity {

    //variable declarations
    private EditText editTextBillAmount;
    private EditText editTextPeople;
    private EditText editTextDescription;
    private Button buttonCalculate;
    private TextView textViewResult;

    private HistoryDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equal_breakdown);

        editTextBillAmount = findViewById(R.id.editTextBillAmount);
        editTextPeople = findViewById(R.id.editTextPeople);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        textViewResult = findViewById(R.id.textViewResult);

        dbHelper = new HistoryDatabaseHelper(this);

        //Click listener for calculate button
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calculateEqualBreakdown();
                storeEqualBreakdownResult();

            }
        });
    }

    //calculate equal breakdown function
    private void calculateEqualBreakdown() {
        String billAmountStr = editTextBillAmount.getText().toString();
        String peopleStr = editTextPeople.getText().toString();
        String description = editTextDescription.getText().toString();

        //error handling
        if (!billAmountStr.isEmpty() && !peopleStr.isEmpty()) {
            double billAmount = Double.parseDouble(billAmountStr);
            int people = Integer.parseInt(peopleStr);

            if (people > 0) {
                //calculation
                double equalShare = billAmount / people;
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                String equalAmount = decimalFormat.format(equalShare);
                String resultText = "Each person pays: $" + equalAmount;

                if (!description.isEmpty()) {
                    resultText += "\nDescription: " + description; // Add the description to the result
                }
                textViewResult.setText(resultText);

            } else {
                textViewResult.setText("Invalid number of people.");
            }
        } else {
            textViewResult.setText("Please enter bill amount and number of people.");
        }
    }

    private void storeEqualBreakdownResult() {
        String equalShareText = textViewResult.getText().toString();
        //Insert the result into local database
        dbHelper.insertHistory(equalShareText);

    }
}