package com.utar.individual;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    //variable declarations
    private ListView historyListView;
    private HistoryDatabaseHelper dbHelper;
    private Button buttonShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyListView = findViewById(R.id.historyListView);
        dbHelper = new HistoryDatabaseHelper(this);
        buttonShare = findViewById(R.id.buttonShare);

        displayHistory();

        //Click listener for share whatsapp button
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWhatsApp();
            }
        });
    }

    //display history function
    private void displayHistory() {
        Cursor historyCursor = dbHelper.getAllHistory();

        ArrayList<String> historyList = new ArrayList<>();
        while (historyCursor.moveToNext()) {
            String result = historyCursor.getString(historyCursor.getColumnIndexOrThrow(HistoryDatabaseHelper.COLUMN_RESULT));
            historyList.add(result);
        }
        historyCursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
        historyListView.setAdapter(adapter);
    }

    //share to whatsapp function
    private void shareWhatsApp() {
        String getRecord = getRecordFromDatabase();

        //check if the record in database is empty or not
        if (!getRecord.isEmpty()) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getRecord);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp"); // Use the WhatsApp package name
            startActivity(sendIntent);
        }
    }

    //get record from local database function
    private String getRecordFromDatabase() {
        return dbHelper.getRecord();
    }
}
