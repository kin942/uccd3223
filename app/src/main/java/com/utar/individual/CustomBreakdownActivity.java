package com.utar.individual;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;


public class CustomBreakdownActivity extends AppCompatActivity {

    //variable declarations
    private EditText editTextBillAmount;
    private EditText editTextNumberOfPeople;
    private Button buttonGenerateFields;
    private Button buttonCalculate;
    private LinearLayout customFieldsLayout;
    private EditText editTextDescription;
    private TextView textViewResult;
    private int status = 0;

    private HistoryDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_breakdown);

        editTextBillAmount = findViewById(R.id.editTextBillAmount);
        editTextNumberOfPeople = findViewById(R.id.editTextNumberOfPeople);
        buttonGenerateFields = findViewById(R.id.buttonGenerateFields);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        customFieldsLayout = findViewById(R.id.customFieldsLayout);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewResult = findViewById(R.id.textViewResult);

        dbHelper = new HistoryDatabaseHelper(this);


        //Click listener for generate custom fields button
        buttonGenerateFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateCustomFields();
            }
        });


        //Click listener for calculate custom breakdown button
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 1) {
                    calculateCustomBreakdown();
                    storeCustomBreakdownResult();
                }
                else{
                    textViewResult.setText("Click the Generate Custom Fields first.");
                }
            }
        });
    }

    //Generate Custom fields function
    private void generateCustomFields() {
        String numberOfPeopleStr = editTextNumberOfPeople.getText().toString();

        //some error handling and generate the number of people involved
        if(!numberOfPeopleStr.isEmpty()) {
            int numberOfPeople = Integer.parseInt(numberOfPeopleStr);

            if (numberOfPeople > 0) {
                for (int i = 1; i <= numberOfPeople; i++) {
                    EditText editText = new EditText(this);
                    editText.setId(View.generateViewId());
                    editText.setHint("Person " + i + " Percentage");
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    customFieldsLayout.addView(editText);
                }
            }
            else {
                textViewResult.setText("Invalid number of people.");
            }
        }
        else{
            textViewResult.setText("Please enter the number of people.");
        }

        //change the status of generate custom field button to 1
        status = 1;
    }


    //Calculate custom breakdown function
    private void calculateCustomBreakdown() {
        String numberOfPeopleStr = editTextNumberOfPeople.getText().toString();
        String billAmountStr = editTextBillAmount.getText().toString();
        String description = editTextDescription.getText().toString();

        if (numberOfPeopleStr.isEmpty()) {
            textViewResult.setText("Please enter the number of participants.");
            return;
        }

        int numberOfPeople = Integer.parseInt(numberOfPeopleStr);
        if (numberOfPeople <= 0) {
            textViewResult.setText("Invalid number of participants.");
            return;
        }

        double totalPercentage = 0.0;
        double[] customPercentages = new double[numberOfPeople];

        for (int i = 0; i < numberOfPeople; i++) {
            EditText editText = customFieldsLayout.findViewById(customFieldsLayout.getChildAt(i).getId());
            String personPercentageStr = editText.getText().toString();
            if (personPercentageStr.isEmpty()) {
                textViewResult.setText("Please enter percentages for all participants.");
                return;
            }

            double personPercentage = Double.parseDouble(personPercentageStr);

            customPercentages[i] = personPercentage;
            totalPercentage += personPercentage;
        }

        if (totalPercentage != 100.0) {
            textViewResult.setText("Total percentage must be 100%");
            return;
        }

        if (billAmountStr.isEmpty()) {
            textViewResult.setText("Please enter bill amount.");
            return;
        }


        // Calculate the breakdown based on custom percentages
        double[] calculatedAmounts = new double[numberOfPeople];
        double billAmount = Double.parseDouble(billAmountStr);

        for (int i = 0; i < numberOfPeople; i++) {
            calculatedAmounts[i] = (customPercentages[i] / 100) * billAmount;
        }

        // Display the calculated amounts in the result text view
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        StringBuilder resultText = new StringBuilder("Result:\n");

        for (int i = 0; i < numberOfPeople; i++) {
            String customAmount = decimalFormat.format(calculatedAmounts[i]);
            resultText.append("Person ").append(i + 1).append(": $").append(customAmount).append("\n");
        }

        if (!description.isEmpty()) {
            resultText.append("Description: ").append(description).append("\n");
        }

        textViewResult.setText(resultText.toString());

    }

    private void storeCustomBreakdownResult() {
        String customBreakdownText = textViewResult.getText().toString();
        //Insert the result into the local database
        dbHelper.insertHistory(customBreakdownText);

    }
}