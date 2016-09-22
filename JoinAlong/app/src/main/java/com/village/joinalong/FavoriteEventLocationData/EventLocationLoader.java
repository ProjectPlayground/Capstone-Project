package com.village.joinalong.FavoriteEventLocationData;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/**
 * Created by richa on 8/29/16.
 */
public class EventLocationLoader extends CursorLoader {
    public static EventLocationLoader newAllLocationInstance(Context context) {
        return new EventLocationLoader(context, EventLocationContract.LocationEntry.CONTENT_URI);
    }

    public static EventLocationLoader newInstanceForLocationId(Context context, long locationId) {
        return new EventLocationLoader(context, EventLocationContract.LocationEntry.buildLocationsUri(locationId));
    }

    private EventLocationLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, null);
    }

    public interface Query {
        String[] PROJECTION = {
                EventLocationContract.LocationEntry._ID,
                EventLocationContract.LocationEntry.COLUMN_LOCATION_ID,
                EventLocationContract.LocationEntry.COLUMN_LOCATION_ADDRESS,
                EventLocationContract.LocationEntry.COLUMN_LOCATION_LAT,
                EventLocationContract.LocationEntry.COLUMN_LOCATION_LNG
        };

        int _ID = 0;
        int COLUMN_LOCATION_ID = 1;
        int COLUMN_LOCATION_ADDRESS = 2;
        int COLUMN_LOCATION_LAT = 3;
        int COLUMN_LOCATION_LNG= 4;

    }
}