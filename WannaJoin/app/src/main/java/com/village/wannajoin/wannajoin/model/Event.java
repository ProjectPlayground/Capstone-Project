package com.village.wannajoin.wannajoin.model;

import com.firebase.client.ServerValue;
import com.village.wannajoin.wannajoin.util.Constants;

import java.util.HashMap;

/**
 * Created by richa on 3/29/16.
 */
public class Event {
    private String title;
    private String location;
    private String owner;
    private long fromTime;
    private long toTime;
    private String notes;
    private HashMap<String, Object> timestampLastChanged;
    private HashMap<String, Object> timestampCreated;

    public Event() {
    }

    public Event(String title, String owner,  long fromTime, long toTime, HashMap<String, Object> timestampCreated) {
        this.title = title;
        this.owner = owner;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public HashMap<String, Object> getTimestampCreated() {
        return timestampCreated;
    }
}
