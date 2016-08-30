package com.village.wannajoin.FavoriteEventLocationData;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by richa on 8/29/16.
 */
public class EventLocationProvider extends ContentProvider {

    private EventLocationDbHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    static final int LOCATION = 100;

    @Override
    public boolean onCreate() {
        mOpenHelper = new EventLocationDbHelper(getContext());
        return true;
    }


    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority =EventLocationContract.CONTENT_AUTHORITY;


        matcher.addURI(authority, EventLocationContract.PATH_LOCATIONS, LOCATION);

        return matcher;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case LOCATION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        EventLocationContract.LocationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case LOCATION:
                return EventLocationContract.LocationEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        if (match ==LOCATION){
            //check if the row exists already
            String selection = EventLocationContract.LocationEntry.COLUMN_LOCATION_ID + "="+ "\""+values.getAsString(EventLocationContract.LocationEntry.COLUMN_LOCATION_ID)+"\"";
            String[] LOCATION_COLUMNS = {
                    EventLocationContract.LocationEntry.TABLE_NAME + "." + EventLocationContract.LocationEntry._ID,

            };
            Cursor cur = db.query(EventLocationContract.LocationEntry.TABLE_NAME,LOCATION_COLUMNS,selection,null,null,null,null);

            if (cur.getCount()>0)
                return null;

            //else continue inserting the row

            long _id = db.insert(EventLocationContract.LocationEntry.TABLE_NAME,null,values);
            if ( _id > 0 )
                returnUri = EventLocationContract.LocationEntry.buildLocationsUri(_id);
            else
                throw new android.database.SQLException("Failed to insert row into " + uri);
        }else
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case LOCATION:
                rowsDeleted = db.delete(
                        EventLocationContract.LocationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case LOCATION:

                rowsUpdated = db.update(EventLocationContract.LocationEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
