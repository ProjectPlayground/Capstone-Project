package com.village.joinalong.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.ServerValue;
import com.village.joinalong.util.Constants;

import java.util.HashMap;

/**
 * Created by richa on 3/29/16.
 */
public class Event implements Parcelable {
    private String eventId;
    private String title;
    private String location;
    private double locationLat;
    private double locationLng;
    private String locationId; //place id from google place api
    private String ownerId;
    private String ownerName;
    private String ownerPhotoUrl;
    private long fromTime;
    private long toTime;
    private String notes;
    private int type; // 1 is event, -1 for empty view
    private HashMap<String, Object> timestampLastChanged;
    private HashMap<String, Object> timestampCreated;
    private HashMap<String,String> eventMembers;

    public Event() {
    }

    public Event(String eventId, String title, String ownerId, String ownerName, String ownerPhotoUrl, long fromTime, long toTime, HashMap<String, Object> timestampCreated, HashMap<String,String> eventMembers) {
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

    protected Event(Parcel in) {
        eventId = in.readString();
        title = in.readString();
        location = in.readString();
        locationLat = in.readDouble();
        locationLng = in.readDouble();
        locationId = in.readString();
        ownerId = in.readString();
        ownerName = in.readString();
        ownerPhotoUrl = in.readString();
        fromTime = in.readLong();
        toTime = in.readLong();
        notes = in.readString();
        timestampLastChanged = in.readHashMap(ServerValue.class.getClassLoader());
        timestampCreated = in.readHashMap(ServerValue.class.getClassLoader());
        eventMembers = in.readHashMap(String.class.getClassLoader());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

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

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
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

    public String getOwnerPhotoUrl() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventId);
        dest.writeString(title);
        dest.writeString(location);
        dest.writeDouble(locationLat);
        dest.writeDouble(locationLng);
        dest.writeString(locationId);
        dest.writeString(ownerId);
        dest.writeString(ownerName);
        dest.writeString(ownerPhotoUrl);
        dest.writeLong(fromTime);
        dest.writeLong(toTime);
        dest.writeString(notes);
        dest.writeMap(timestampLastChanged);
        dest.writeMap(timestampCreated);
        dest.writeMap(eventMembers);
    }
}
