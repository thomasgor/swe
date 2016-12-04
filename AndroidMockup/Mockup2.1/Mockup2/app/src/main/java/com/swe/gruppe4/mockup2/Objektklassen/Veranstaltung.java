package com.swe.gruppe4.mockup2.Objektklassen;

import java.io.Serializable;

/**
 * Created by Merlin on 22.11.2016.
 */

public class Veranstaltung implements Serializable {
    private int id;
    private Benutzer dozent;
    private long von;
    private long bis;
    private Raum raum;

    public Veranstaltung(int id, Benutzer dozent, long von, long bis, Raum raum) {
        this.id = id;
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
