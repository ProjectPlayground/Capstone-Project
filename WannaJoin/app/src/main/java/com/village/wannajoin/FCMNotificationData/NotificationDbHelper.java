package com.village.wannajoin.FCMNotificationData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by richa on 9/17/16.
 */
public class NotificationDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "notifications.db";

    public NotificationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + NotificationContract.NotificationEntry.TABLE_NAME + " (" +
                NotificationContract.NotificationEntry._ID + " INTEGER PRIMARY KEY," +
                NotificationContract.NotificationEntry.COLUMN_NOTIFICATION_ID + " TEXT NOT NULL, " +
                NotificationContract.NotificationEntry.COLUMN_EVENT_ID + " TEXT NOT NULL, " +
                NotificationContract.NotificationEntry.COLUMN_SENDER + " TEXT NOT NULL, " +
                NotificationContract.NotificationEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                NotificationContract.NotificationEntry.COLUMN_EVENT_TITLE + " TEXT NOT NULL "+
                " );";

        db.execSQL(SQL_CREATE_NOTIFICATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +NotificationContract.NotificationEntry.TABLE_NAME);
        onCreate(db);
    }
}