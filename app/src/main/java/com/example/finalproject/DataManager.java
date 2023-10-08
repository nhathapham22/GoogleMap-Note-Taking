package com.example.finalproject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DataManager {
    //declaration
    private SQLiteDatabase db;

    // use constants to define the database schema

    /*
    Next we have a public static final string for
    each row that we need to refer to both
    inside and outside this class
    */
    public static final String TABLE_ROW_ID = "_id";
    public static final String TABLE_ROW_NAME = "name";
    public static final String TABLE_ROW_ADDRESS = "address";
    public static final String TABLE_ROW_LATITUDE = "latitude";
    public static final String TABLE_ROW_LONGITUDE = "longitude";
    public static final String TABLE_ROW_NOTES = "notes";

    /*
        Next we have a private static final strings for
        each row that we need to refer to just
        inside this class
    */

    private static final String DB_NAME = "journal_record_db";
    private static final int DB_VERSION = 2;
    private static final String TABLE_NAME = "journal_record";

    // define the DataManager constructor
    public DataManager(Context context) {

        // Create an instance of our internal CustomSQLiteOpenHelper

        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);

        // Get a writable database and store it in the global variable
        db = helper.getWritableDatabase();
    }

    // Insert a record
    public void insert(JournalRecord journalRecord) {
        ContentValues values = new ContentValues();
        values.put(TABLE_ROW_NAME, journalRecord.getLocationName());
        values.put(TABLE_ROW_ADDRESS, journalRecord.getLocationAddress());
        values.put(TABLE_ROW_LATITUDE, journalRecord.getLatitude());
        values.put(TABLE_ROW_LONGITUDE, journalRecord.getLongitude());
        values.put(TABLE_ROW_NOTES, journalRecord.getJournalNotes());
        db.insert(TABLE_NAME, null, values);
    }
    public ArrayList<JournalRecord> listAllRecords() {
        ArrayList<JournalRecord> journalRecordList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY _id DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all records
        if (cursor.moveToFirst()) {
            do {

                // String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String address = cursor.getString(2);
                Double latitude = cursor.getDouble(3);
                Double longitude = cursor.getDouble(4);
                String notes = cursor.getString(5);

                JournalRecord journalRecord = new JournalRecord(id, name, address, latitude, longitude, notes);
                journalRecordList.add(journalRecord);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return journalRecordList;
    }

    //delete function
    public void clearAllRecords() {
        String query = String.format("DELETE FROM %s", TABLE_NAME );
        db.execSQL(query);
    }

    // This SQLiteOpenHelper class is called when our DataManager is initialized
    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {

        // when instantiated (constructor is called)
        // this object will check if the database exists
        // if not, call onCreate() to make a new database file

        // and if its version is older, it call onUpgrade() to convert the old database to the new version
        public CustomSQLiteOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        // This runs the first time the database is created
        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create a table for location id, name, address, lattitude, longitude, notes
            String newTableQueryString = "CREATE TABLE "
                    + TABLE_NAME + " ("
                    + TABLE_ROW_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + TABLE_ROW_NAME
                    + " TEXT NOT NULL,"
                    + TABLE_ROW_ADDRESS
                    + " TEXT NOT NULL,"
                    + TABLE_ROW_LATITUDE
                    + " REAL NOT NULL,"
                    + TABLE_ROW_LONGITUDE
                    + " REAL NOT NULL,"
                    + TABLE_ROW_NOTES
                    + " TEXT NOT NULL)"
                    ;


            db.execSQL(newTableQueryString);

        }

        // This method only runs when we increment DB_VERSION
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            // code here to covert the old db to the new version
            // must override the method, but need not code the function body
        }
    }
}