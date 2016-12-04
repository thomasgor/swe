package com.swe.gruppe4.mockup2.Objektklassen;

import java.io.Serializable;

/**
 * Created by Merlin on 22.11.2016.
 */

public class Homescreen implements Serializable {
    private Raum[] räume;
    private Karte karte;

    public Homescreen(Raum[] räume, Karte karte) {
        this.räume = räume;
        this.karte = karte;
    }

    public Raum[] getRäume() {
        return räume;
    }

    public void setRäume(Raum[] räume) {
        this.räume = räume;
    }

    public Karte getKarte() {
        return karte;
    }

    public void setKarte(Karte karte) {
        this.karte = karte;
    }
}
