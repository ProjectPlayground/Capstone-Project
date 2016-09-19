package com.village.wannajoin.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by richa on 6/28/16.
 */
public class Member implements Parcelable {
    private String name;
    private String userId;
    private String photoUrl;
    private String status;
    private HashMap<String, Object> timestampJoined;


    /**
     * Required public constructor
     */
    public Member() {
    }

    public Member(String name, String userId, String photoUrl, HashMap<String, Object> timestampJoined) {
        this.name = name;
        this.userId = userId;
        this.photoUrl = photoUrl;
        this.timestampJoined = timestampJoined;
    }

    public Member(String name, String userId, String photoUrl,  String status, HashMap<String, Object> timestampJoined) {
        this.name = name;
        this.userId = userId;
        this.photoUrl = photoUrl;
        this.status = status;
        this.timestampJoined = timestampJoined;
    }

    protected Member(Parcel in) {
        name = in.readString();
        userId = in.readString();
        photoUrl = in.readString();
        status = in.readString();
        timestampJoined = in.readHashMap(ServerValue.class.getClassLoader());
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }

    public void setTimestampJoined(HashMap<String, Object> timestampJoined) {
        this.timestampJoined = timestampJoined;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("userId", userId);
        result.put("photoUrl", photoUrl);
        result.put("status",status);
        result.put("timestampJoined", timestampJoined);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(userId);
        dest.writeString(photoUrl);
        dest.writeString(status);
        //dest.writeString(photoUrl);
        dest.writeMap(timestampJoined);
    }
}
