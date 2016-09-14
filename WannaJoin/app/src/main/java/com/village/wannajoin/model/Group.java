package com.village.wannajoin.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by richa on 6/15/16.
 */
public class Group implements Parcelable {
    private String name;
    private String groupId;
    private String groupOwner;
    private Uri groupPhotoUrl;
    private HashMap<String, Object> timestampCreated;
    private HashMap<String,Boolean> groupMembers;

    /**
     * Required public constructor
     */
    public Group() {
    }


    public Group(String name, String groupId,String groupOwner,  Uri groupPhotoUrl, HashMap<String, Object> timestampCreated, HashMap<String, Boolean> groupMembers) {
        this.name = name;
        this.groupOwner = groupOwner;
        this.groupId = groupId;
        this.groupPhotoUrl = groupPhotoUrl;
        this.timestampCreated = timestampCreated;
        this.groupMembers = groupMembers;
    }

    protected Group(Parcel in) {
        name = in.readString();
        groupId = in.readString();
        groupOwner = in.readString();
        timestampCreated = in.readHashMap(ServerValue.class.getClassLoader());
        groupMembers = in.readHashMap(Boolean.class.getClassLoader());
        groupPhotoUrl = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupOwner() {
        return groupOwner;
    }

    public String getGroupId() {
        return groupId;
    }

    public Uri getGroupPhotoUrl() {
        return groupPhotoUrl;
    }

    public HashMap<String, Object> getTimestampCreated() {
        return timestampCreated;
    }

    public HashMap<String, Boolean> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(HashMap<String, Boolean> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("groupId", groupId);
        result.put("groupOwner", groupOwner);
        result.put("groupPhotoUrl", groupPhotoUrl);
        result.put("timestampCreated", timestampCreated);
        result.put("groupMembers", groupMembers);


        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(groupId);
        dest.writeString(groupOwner);
        dest.writeMap(timestampCreated);
        dest.writeMap(groupMembers);
        dest.writeParcelable(groupPhotoUrl, 0);
    }
}
