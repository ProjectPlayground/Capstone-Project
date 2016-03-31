package com.village.wannajoin.wannajoin.util;

import com.village.wannajoin.wannajoin.BuildConfig;

/**
 * Created by richa on 3/29/16.
 */
public class Constants {
    /**
     * Constants for Firebase
     */

    public static final String FIREBASE_LOCATION_USER_EVENTS = "userEvents";
    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";

    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_URL_USER_EVENTS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USER_EVENTS;
}



