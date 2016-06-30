package com.village.wannajoin.model;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by richa on 6/28/16.
 */
public class Member {
    private String name;
    private String userId;
    private Uri photoUrl;
    private HashMap<String, Object> timestampJoined;


    /**
     * Required public constructor
     */
    public Member() {
    }

    public Member(String name, String userId, Uri photoUrl, HashMap<String, Object> timestampJoined) {
        this.name = name;
        this.userId = userId;
        this.photoUrl = photoUrl;
        this.timestampJoined = timestampJoined;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("userId", userId);
        result.put("photoUrl", photoUrl);
        result.put("timestampJoined", timestampJoined);
        return result;
    }
}
