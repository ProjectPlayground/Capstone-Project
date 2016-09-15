package com.village.wannajoin.messaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.village.wannajoin.R;
import com.village.wannajoin.ui.WelcomeActivity;

/**
 * Created by richa on 9/11/16.
 */
public class WannaJoinFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";
    private final static String GROUP_NOTIFICATION = "wannajoin_notifications";

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
            String sender = remoteMessage.getData().get("owner");
            String title = remoteMessage.getData().get("eventTitle");
            String type = remoteMessage.getData().get("type");
            sendNotification(sender, type, title);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    private void sendNotification(String sender, String type, String title) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean notificationPref = sharedPref.getBoolean("notifications_new_message",true);
        if (notificationPref) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            String messageBody = null;
            if (type.equals("New")) {
                messageBody = getString(R.string.notification_new_event, title);
            } else if (type.equals("Update")) {
                messageBody = getString(R.string.notification_update_event, title);
            } else if (type.equals("Delete")) {
                messageBody = getString(R.string.notification_delete_event, title);
            }
            Boolean notificationVibratePref = sharedPref.getBoolean("notifications_new_message_vibrate",true);
            Uri soundUri = Uri.parse(sharedPref.getString("notifications_new_message_ringtone","content://settings/system/notification_sound"));


           // Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Notification notification1;
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.wannajoinlogo);

            if (notificationVibratePref) {
                notification1 = new NotificationCompat.Builder(this)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setSmallIcon(R.mipmap.wannajoinlogo)
                        .setLargeIcon(largeIcon)
                        .setContentTitle(getString(R.string.notification_title_for_msg, sender))
                        .setContentText(messageBody)
                        .setGroup(GROUP_NOTIFICATION)
                        .setAutoCancel(true)
                        .setSound(soundUri)
                        .setContentIntent(pendingIntent).build();
            }else{
                notification1 = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.wannajoinlogo)
                        .setLargeIcon(largeIcon)
                        .setContentTitle(getString(R.string.notification_title_for_msg, sender))
                        .setContentText(messageBody)
                        .setGroup(GROUP_NOTIFICATION)
                        .setAutoCancel(true)
                        .setSound(soundUri)
                        .setVibrate(new long[]{0l})
                        .setContentIntent(pendingIntent).build();
            }


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int notificationId = (int) (System.currentTimeMillis()%10000);

            notificationManager.notify(notificationId, notification1);

        }
    }
}
