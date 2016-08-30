package com.village.wannajoin.FavoriteEventLocationData;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by richa on 8/29/16.
 */
public class EventLocationContract {
    public static final String CONTENT_AUTHORITY = "com.village.wannajoin";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_LOCATIONS = "locations";

    public static final class LocationEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATIONS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATIONS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATIONS;

        public static final String TABLE_NAME = "locations";

        public static final String COLUMN_LOCATION_ID = "location_id";
        public static final String COLUMN_LOCATION_ADDRESS = "location_address";
        public static final String COLUMN_LOCATION_LAT = "location_lat";
        public static final String COLUMN_LOCATION_LNG = "location_lng";

        public static Uri buildLocationsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
