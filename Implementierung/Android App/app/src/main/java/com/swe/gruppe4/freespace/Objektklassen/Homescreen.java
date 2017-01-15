package com.swe.gruppe4.freespace.Objektklassen;

import java.io.Serializable;

/**
 * <p>Überschrift: Struktur von Homescreen</p>
 * <p>Beschreibung: Diese Klasse dient dazu, für den Homescreen relevante Informationen anzuzeigen
 * </p>
 * <p>Organisation: FH Aachen, FB05, SWE Gruppe 4 </p>
 *
 * @author Merlin
 * @version 1.0
 *
 * @deprecated
 */

@Deprecated
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
