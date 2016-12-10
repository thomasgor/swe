package com.swe.gruppe4.freespace;

/**
 * Created by Merlin on 29.10.2016.
 */

public class Tag {
    public boolean isChecked;
    public String tagName;

    public Tag(String name, boolean check){
        super();
        this.tagName=name;
        this.isChecked=check;
    }
}
