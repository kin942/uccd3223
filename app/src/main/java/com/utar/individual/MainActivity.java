package com.utar.individual;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button equalBreakdownButton = findViewById(R.id.buttonEqualBreakdown);
        Button customBreakdownButton = findViewById(R.id.buttonCustomBreakdown);
        Button historyButton = findViewById(R.id.buttonHistoryBreakdown);

        equalBreakdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Equal Break-down section
                Intent intent = new Intent(MainActivity.this, EqualBreakdownActivity.class);
                startActivity(intent);
            }
        });

        customBreakdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Custom Break-down section
                Intent intent = new Intent(MainActivity.this, CustomBreakdownActivity.class);
                startActivity(intent);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Display history section
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}