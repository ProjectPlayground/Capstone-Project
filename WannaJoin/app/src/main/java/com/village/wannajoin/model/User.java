package com.village.wannajoin.model;

import android.net.Uri;

import java.net.URI;
import java.util.HashMap;

/**
 * Created by richa on 5/31/16.
 */
public class User {
    private String name;
    private String email;
    private Uri photoUrl;
    private HashMap<String, Object> timestampJoined;


    /**
     * Required public constructor
     */
    public User() {
    }

    /**
     * Use this constructor to create new User.
     * Takes user name, email and timestampJoined as params
     *
     * @param name
     * @param email
     * @param timestampJoined
     */
    public User(String name, String email, Uri photoUrl, HashMap<String, Object> timestampJoined) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.timestampJoined = timestampJoined;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }
}
