package com.village.joinalong.FCMNotificationData;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;


/**
 * Created by richa on 9/18/16.
 */
public class NotificationLoader extends CursorLoader {
    public static NotificationLoader newAllEventInstance(Context context) {
        return new NotificationLoader(context, NotificationContract.NotificationEntry.CONTENT_URI);
    }

    private NotificationLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, null);
    }

    public interface Query {
        String[] PROJECTION = {
                NotificationContract.NotificationEntry._ID,
                NotificationContract.NotificationEntry.COLUMN_NOTIFICATION_ID,
                NotificationContract.NotificationEntry.COLUMN_EVENT_ID,
                NotificationContract.NotificationEntry.COLUMN_SENDER,
                NotificationContract.NotificationEntry.COLUMN_TYPE,
                NotificationContract.NotificationEntry.COLUMN_EVENT_TITLE
        };

        int _ID= 0;
        int COLUMN_NOTIFICATION_ID = 1;
        int COLUMN_EVENT_ID = 2;
        int COLUMN_SENDER = 3;
        int COLUMN_TYPE= 4;
        int COLUMN_EVENT_TITLE= 5;

    }
}