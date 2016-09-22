package com.village.joinalong.util;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by richa on 9/14/16.
 */
public class NotificationUtil {
    public static void sendRegistrationToken(String token){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null) {
            String currentUserId = firebaseUser.getUid();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_REGISTRATIONS).child(currentUserId).child(token);
            dbRef.setValue(true);
        }
    }

    public static void removeRegistrationToken(String token){
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_REGISTRATIONS).child(currentUserId).child(token);
        dbRef.setValue(false);
    }

    public static void sendEventNotification(String eventId,String senderName, String type, String eventTitle, ArrayList<String> users){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_NOTIFICATIONS);
        DatabaseReference notificationRef = dbRef.push();
        String notificationKey = notificationRef.getKey();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + notificationKey+"/"+ Constants.FIREBASE_LOCATION_EVENTID,eventId);
        childUpdates.put("/" + notificationKey+"/"+ Constants.FIREBASE_LOCATION_OWNER,senderName);
        childUpdates.put("/" + notificationKey+"/"+ Constants.FIREBASE_LOCATION_TYPE,type);
        childUpdates.put("/" + notificationKey+"/"+ Constants.FIREBASE_LOCATION_TITLE,eventTitle);
        for (String userId:users) {
            childUpdates.put("/" + notificationKey + "/" + Constants.FIREBASE_LOCATION_SENDTO+"/"+userId,true);
        }
        dbRef.updateChildren(childUpdates);
    }
}
