package com.village.wannajoin.model;

import android.net.Uri;

/**
 * Created by richa on 6/21/16.
 */
public class ContactAndGroup implements Comparable {
    private String name;
    private String id;
    private Uri photoUrl;
    private boolean isGroup;
    private boolean isSelected;

    public ContactAndGroup(String name, String id, Uri photoUrl, boolean isGroup) {
        this.name = name;
        this.id = id;
        this.photoUrl = photoUrl;
        this.isGroup = isGroup;
        this.isSelected = false;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int compareTo(Object another) {

        if (this.isSelected && !((ContactAndGroup)another).isSelected() )
            return -1;
        else if (!this.isSelected && ((ContactAndGroup)another).isSelected() )
            return 1;
        else if (this.isGroup && !((ContactAndGroup)another).isGroup())
        return -1;
        else if (!this.isGroup && ((ContactAndGroup)another).isGroup() )
            return 1;
        else return this.name.compareTo(((ContactAndGroup)another).getName());

    }
}
