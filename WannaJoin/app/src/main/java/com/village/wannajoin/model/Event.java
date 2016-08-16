package com.village.wannajoin.model;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ServerValue;
import com.village.wannajoin.util.Constants;

import java.util.HashMap;

/**
 * Created by richa on 3/29/16.
 */
public class Event {
    private String eventId;
    private String title;
    private String location;
    private double locationLat;
    private double locationLng;
    private String ownerId;
    private String ownerName;
    private Uri ownerPhotoUrl;
    private long fromTime;
    private long toTime;
    private String notes;
    private int type; // 1 is event, -1 for empty view
    private HashMap<String, Object> timestampLastChanged;
    private HashMap<String, Object> timestampCreated;
    private HashMap<String,String> eventMembers;

    public Event() {
    }

    public Event(String eventId, String title, String ownerId, String ownerName, Uri ownerPhotoUrl, long fromTime, long toTime, HashMap<String, Object> timestampCreated, HashMap<String,String> eventMembers) {
        this.eventId = eventId;
        this.title = title;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.ownerPhotoUrl = ownerPhotoUrl;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.timestampCreated = timestampCreated;
        HashMap<String, Object> timestampNowObject = new HashMap<String, Object>();
        timestampNowObject.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampLastChanged = timestampNowObject;
        this.eventMembers =eventMembers;
        this.type = 1;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(double locationLng) {
        this.locationLng = locationLng;
    }

    public long getFromTime() {
        return fromTime;
    }

    public void setFromTime(long fromTime) {
        this.fromTime = fromTime;
    }

    public long getToTime() {
        return toTime;
    }

    public void setToTime(long toTime) {
        this.toTime = toTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public HashMap<String, Object> getTimestampLastChanged() {
        return timestampLastChanged;
    }

    public void setTimestampLastChanged(HashMap<String, Object> timestampLastChanged) {
        this.timestampLastChanged = timestampLastChanged;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Uri getOwnerPhotoUrl() {
        return ownerPhotoUrl;
    }

    public HashMap<String, Object> getTimestampCreated() {
        return timestampCreated;
    }

    public String getEventId() {
        return eventId;
    }

    public HashMap<String, String> getEventMembers() {
        return eventMembers;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
