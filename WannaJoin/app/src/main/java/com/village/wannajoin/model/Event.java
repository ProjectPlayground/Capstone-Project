package com.village.wannajoin.model;

import android.net.Uri;

import com.google.firebase.database.ServerValue;
import com.village.wannajoin.util.Constants;

import java.util.HashMap;

/**
 * Created by richa on 3/29/16.
 */
public class Event {
    private String title;
    private String location;
    private String ownerId;
    private String ownerName;
    private Uri ownerPhotoUrl;
    private long fromTime;
    private long toTime;
    private String notes;
    private HashMap<String, Object> timestampLastChanged;
    private HashMap<String, Object> timestampCreated;

    public Event() {
    }

    public Event(String title, String ownerId, String ownerName, Uri ownerPhotoUrl, long fromTime, long toTime, HashMap<String, Object> timestampCreated) {
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
}