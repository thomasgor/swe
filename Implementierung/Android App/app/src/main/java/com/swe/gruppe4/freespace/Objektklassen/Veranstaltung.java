package com.swe.gruppe4.freespace.Objektklassen;

import java.io.Serializable;

/**
 *  * Created by Merlin on 22.11.2016.
 */

public class Veranstaltung implements Serializable {
    private int id;
    private String name;
    private Benutzer dozent;
    private long von;
    private long bis;
    private Raum raum;

    public Veranstaltung(int id, String name, Benutzer dozent, long von, long bis, Raum raum) {
        this.id = id;
        this.name = name;
        this.dozent = dozent;
        this.von = von;
        this.bis = bis;
        this.raum = raum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {this.name = name;}

    public String getName() {return name;}

    public Benutzer getDozent() {
        return dozent;
    }

    public void setDozent(Benutzer dozent) {
        this.dozent = dozent;
    }

    public long getVon() {
        return von;
    }

    public void setVon(long von) {
        this.von = von;
    }

    public long getBis() {
        return bis;
    }

    public void setBis(long bis) {
        this.bis = bis;
    }

    public Raum getRaum() {
        return raum;
    }

    public void setRaum(Raum raum) {
        this.raum = raum;
    }
}
