package com.village.wannajoin.model;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by richa on 6/15/16.
 */
public class Group {
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

    public String getName() {
        return name;
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
}
