package com.swe.gruppe4.freespace.Objektklassen;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Kiesa on 05.01.2017
 */

public  class RoomEnterance {
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    private float x;
    private float y;
    RoomEnterance(int id, String name, float x, float y){
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
