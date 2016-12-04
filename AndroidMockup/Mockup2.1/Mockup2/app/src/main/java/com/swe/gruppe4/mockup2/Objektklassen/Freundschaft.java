package com.swe.gruppe4.mockup2.Objektklassen;

import java.io.Serializable;

/**
 * Created by Merlin on 22.11.2016.
 */

public class Freundschaft implements Serializable{
    private Benutzer benutzer;
    private boolean status;

    public Freundschaft(Benutzer benutzer, boolean status, Raum raum) {
        this.benutzer = benutzer;
        this.status = status;
        this.raum = raum;
    }

    public Raum getRaum() {
        return raum;
    }

    public void setRaum(Raum raum) {
        this.raum = raum;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    private Raum raum;
}
