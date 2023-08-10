package com.utar.individual;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "history.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_HISTORY = "history";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_RESULT = "result";

    public HistoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_HISTORY +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RESULT + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    public long insertHistory(String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESULT, result);
        long newRowId = db.insert(TABLE_HISTORY, null, values);
        db.close();
        return newRowId;
    }

    public Cursor getAllHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_HISTORY, null, null, null, null, null, null);
    }

    public String getRecord() {
        SQLiteDatabase db = this.getReadableDatabase();
        String getRecord = ""; // Default value if no record is found

        Cursor cursor = db.query(
                TABLE_HISTORY,
                new String[]{COLUMN_RESULT},
                null,
                null,
                null,
                null,
                COLUMN_ID + " DESC", // Sort by ID in ascending order
                "1" // Limit to the first record
        );

        if (cursor.moveToFirst()) {
            getRecord = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESULT));
        }

        cursor.close();
        db.close();

        return getRecord;
    }
}