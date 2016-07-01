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
    private int type;  //0 for label, 1 for group and 2 for contact

    public ContactAndGroup(String name, String id, Uri photoUrl, boolean isGroup, int type) {
        this.name = name;
        this.id = id;
        this.photoUrl = photoUrl;
        this.isGroup = isGroup;
        this.isSelected = false;
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
