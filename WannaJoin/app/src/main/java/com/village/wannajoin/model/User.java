package com.village.wannajoin.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by richa on 5/31/16.
 */
public class User implements Parcelable{
    private String name;
    private String email;
    private String userId;
    private Uri photoUrl;
    private HashMap<String, Object> timestampJoined;
    private HashMap<String,Boolean> groups;
    private HashMap<String,Boolean> contactOf;


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
    public User(String name, String uid, String email, Uri photoUrl, HashMap<String, Object> timestampJoined) {
        this.name = name;
        this.userId = uid;
        this.email = email;
        this.photoUrl = photoUrl;
        this.timestampJoined = timestampJoined;
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        userId = in.readString();
        timestampJoined = in.readHashMap(ServerValue.class.getClassLoader());
        groups = in.readHashMap(Boolean.class.getClassLoader());
        contactOf = in.readHashMap(Boolean.class.getClassLoader());
        photoUrl = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public String getUserId() {
        return userId;
    }

    public HashMap<String, Boolean> getGroups() {
        return groups;
    }

    public HashMap<String, Boolean> getContactOf() {
        return contactOf;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(userId);
        dest.writeMap(timestampJoined);
        dest.writeMap(groups);
        dest.writeMap(contactOf);
        dest.writeParcelable(photoUrl,0);

    }

}
