package com.village.joinalong.FCMNotificationData;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by richa on 9/17/16.
 */
public class NotificationContract {
    public static final String CONTENT_AUTHORITY = "com.village.joinalong.notification";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_NOTIFICATIONS = "notifications";

    public static final class NotificationEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTIFICATIONS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTIFICATIONS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTIFICATIONS;

        public static final String TABLE_NAME = "notifications";

        public static final String COLUMN_NOTIFICATION_ID = "notification_id";
        public static final String COLUMN_EVENT_ID = "events_id";
        public static final String COLUMN_SENDER = "sender";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_EVENT_TITLE= "event_title";


        public static Uri buildNotificationsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
