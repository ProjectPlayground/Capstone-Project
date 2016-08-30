package com.village.wannajoin.FavoriteEventLocationData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by richa on 8/29/16.
 */
public class EventLocationDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "eventlocations.db";

    public EventLocationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + EventLocationContract.LocationEntry.TABLE_NAME + " (" +
                EventLocationContract.LocationEntry._ID + " INTEGER PRIMARY KEY," +
                EventLocationContract.LocationEntry.COLUMN_LOCATION_ID + " TEXT NOT NULL, " +
                EventLocationContract.LocationEntry.COLUMN_LOCATION_ADDRESS + " TEXT NOT NULL, " +
                EventLocationContract.LocationEntry.COLUMN_LOCATION_LAT + " TEXT NOT NULL, " +
                EventLocationContract.LocationEntry.COLUMN_LOCATION_LNG + " TEXT NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EventLocationContract.LocationEntry.TABLE_NAME);
        onCreate(db);
    }
}