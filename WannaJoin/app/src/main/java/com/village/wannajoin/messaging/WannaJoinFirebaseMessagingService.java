package com.village.wannajoin.messaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.village.wannajoin.FCMNotificationData.NotificationContract;
import com.village.wannajoin.R;
import com.village.wannajoin.ui.WelcomeActivity;
import com.village.wannajoin.util.Util;

/**
 * Created by richa on 9/11/16.
 */
public class WannaJoinFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";
    private final static String GROUP_NOTIFICATION = "wannajoin_notifications";
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

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
       // Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String notificationId = remoteMessage.getData().get("notificationId");
            String eventId = remoteMessage.getData().get("eventId");
            String sender = remoteMessage.getData().get("owner");
            String title = remoteMessage.getData().get("eventTitle");
            String type = remoteMessage.getData().get("type");
            sendNotification(notificationId,eventId,sender, type, title);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    private void sendNotification(String notificationId, String eventId, String sender, String type, String title) {
        //update widget
        Util.updateWidgets(this);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean notificationPref = sharedPref.getBoolean("notifications_new_message", true);
        if (notificationPref) {

            Boolean notificationVibratePref = sharedPref.getBoolean("notifications_new_message_vibrate", true);
            Uri soundUri = Uri.parse(sharedPref.getString("notifications_new_message_ringtone", "content://settings/system/notification_sound"));
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.wannajoinlogo);

            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            //add notification in local notification db
            Uri uri = addNotificationToDataBase(notificationId, eventId, sender, type, title);

            //Query notifications from local db
            Cursor cursor = readNotificationFromDataBase(PROJECTION, null, null, null);
            NotificationCompat.InboxStyle notificationStyle = new NotificationCompat.InboxStyle();

            if (cursor != null) {
                while (cursor.moveToNext()) {


                   // String messageBody = null;
                    if (cursor.getString(COLUMN_TYPE).equals("New")) {
                       // messageBody = getString(R.string.notification_new_event, cursor.getString(COLUMN_EVENT_TITLE));
                        notificationStyle.addLine(getString(R.string.summary_notification_new_event,cursor.getString(COLUMN_SENDER),cursor.getString(COLUMN_EVENT_TITLE)));
                    } else if (cursor.getString(COLUMN_TYPE).equals("Update")) {
                       // messageBody = getString(R.string.notification_update_event, cursor.getString(COLUMN_EVENT_TITLE));
                        notificationStyle.addLine(getString(R.string.summary_notification_update_event,cursor.getString(COLUMN_SENDER),cursor.getString(COLUMN_EVENT_TITLE)));
                    } else if (cursor.getString(COLUMN_TYPE).equals("Delete")) {
                       // messageBody = getString(R.string.notification_delete_event, cursor.getString(COLUMN_EVENT_TITLE));
                        notificationStyle.addLine(getString(R.string.summary_notification_delete_event,cursor.getString(COLUMN_SENDER),cursor.getString(COLUMN_EVENT_TITLE)));
                    }


                    // Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                   /* Notification notification1;

                    if (notificationVibratePref) {
                        notification1 = new NotificationCompat.Builder(this)
                                .setDefaults(Notification.DEFAULT_VIBRATE)
                                .setSmallIcon(R.mipmap.wannajoinlogo)
                                .setContentTitle(getString(R.string.notification_title_for_msg, cursor.getString(COLUMN_SENDER)))
                                .setContentText(messageBody)
                                .setGroup(GROUP_NOTIFICATION)
                                .setAutoCancel(true)
                                .setSound(soundUri)
                                .setContentIntent(pendingIntent).build();
                    } else {
                        notification1 = new NotificationCompat.Builder(this)
                                .setSmallIcon(R.mipmap.wannajoinlogo)
                                .setContentTitle(getString(R.string.notification_title_for_msg, cursor.getString(COLUMN_SENDER)))
                                .setContentText(messageBody)
                                .setGroup(GROUP_NOTIFICATION)
                                .setAutoCancel(true)
                                .setSound(soundUri)
                                .setVibrate(new long[]{0l})
                                .setContentIntent(pendingIntent).build();
                    }

                    notificationManager.notify(cursor.getInt(_ID), notification1);*/
                }
                //String summaryMessagyBody = sb.toString();

                String summaryNotificationTitle=null;
                if (cursor.getCount()==1)
                    summaryNotificationTitle = getString(R.string.summary_notification_title,1);
                else
                    summaryNotificationTitle = getString(R.string.summary_notification_title_mm,cursor.getCount());
                if (notificationVibratePref) {
                    // Create an InboxStyle summary notification
                    Notification summaryNotification = new NotificationCompat.Builder(this)
                            .setContentTitle(summaryNotificationTitle)
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setSmallIcon(R.mipmap.wannajoinlogo)
                            .setLargeIcon(largeIcon)
                            .setAutoCancel(true)
                            .setStyle(notificationStyle)
                            .setGroup(GROUP_NOTIFICATION)
                            .setSound(soundUri)
                            .setContentIntent(pendingIntent)
                            .build();
                    notificationManager.notify(0, summaryNotification);
                }else {
                    // Create an InboxStyle summary notification

                    notificationStyle.setBigContentTitle(summaryNotificationTitle);
                    Notification summaryNotification = new NotificationCompat.Builder(this)
                            .setContentTitle(summaryNotificationTitle)
                            .setSmallIcon(R.mipmap.wannajoinlogo)
                            .setLargeIcon(largeIcon)
                            .setSound(soundUri)
                            .setAutoCancel(true)
                            .setVibrate(new long[]{0l})
                            .setStyle(notificationStyle)
                            .setGroup(GROUP_NOTIFICATION)
                            .setContentIntent(pendingIntent)
                            .build();
                    notificationManager.notify(0, summaryNotification);
                }
            }
            cursor.close();
        }
    }


    private Uri addNotificationToDataBase(String notificationId, String eventId, String sender, String type, String title){
        ContentResolver contentResolver = getContentResolver();
       // NotificationQueryHandler notificationHandler = new NotificationQueryHandler(contentResolver, this);
        ContentValues values = new ContentValues();
        values.put(NotificationContract.NotificationEntry.COLUMN_NOTIFICATION_ID,notificationId);
        values.put(NotificationContract.NotificationEntry.COLUMN_EVENT_ID,eventId);
        values.put(NotificationContract.NotificationEntry.COLUMN_SENDER,sender);
        values.put(NotificationContract.NotificationEntry.COLUMN_TYPE,type);
        values.put(NotificationContract.NotificationEntry.COLUMN_EVENT_TITLE,title);

        Uri uri = contentResolver.insert(NotificationContract.NotificationEntry.CONTENT_URI, values);
        return uri;
    }

    private Cursor readNotificationFromDataBase(String[] projection, String selection, String[] selectionArgs, String sortOrder){
        Cursor cursor = getContentResolver().query(
                NotificationContract.NotificationEntry.CONTENT_URI,  // The content URI of the words table
                projection,                       // The columns to return for each row
                selection,                  // Either null, or the word the user entered
                selectionArgs,                    // Either empty, or the string the user entered
                sortOrder);
        return cursor;
    }
}
