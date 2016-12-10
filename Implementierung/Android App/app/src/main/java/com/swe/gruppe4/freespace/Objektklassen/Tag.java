package com.swe.gruppe4.freespace.Objektklassen;

import java.io.Serializable;

/**
 * Created by Merlin on 22.11.2016.
 */

public class Tag implements Serializable {
    private int id;
    private String name;

    public Tag(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
