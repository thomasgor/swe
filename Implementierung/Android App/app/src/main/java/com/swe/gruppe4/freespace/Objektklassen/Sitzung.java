package com.swe.gruppe4.freespace.Objektklassen;

import java.io.Serializable;

/**
 * Created by Merlin on 22.11.2016.
 */

public class Sitzung implements Serializable{
    private int id;
    private Raum raum;
    private boolean myTag;
    private long endzeit;

    public Sitzung(int id, Raum raum, boolean myTag, long endzeit) {
        this.id = id;
        this.raum = raum;
        this.myTag = myTag;
        this.endzeit = endzeit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Raum getRaum() {
        return raum;
    }

    public void setRaum(Raum raum) {
        this.raum = raum;
    }

    public boolean isMyTag() {
        return myTag;
    }

    public void setMyTag(boolean myTag) {
        this.myTag = myTag;
    }

    public long getEndzeit() {
        return endzeit;
    }

    public void setEndzeit(long endzeit) {
        this.endzeit = endzeit;
    }
}
