package com.fhaachen.swe.freespace.maps;

import java.util.ArrayList;

/**
 * Created by Simon on 08.12.2016.
 */
public class Karte {

    private static ArrayList<Knoten> knoten = new ArrayList<Knoten>();
    private static ArrayList<Kante> kanten = new ArrayList<Kante>();
    public static final String ETAGE_HOCH = "hoch";
    public static final String ETAGE_RUNTER = "runter";
    public static Knoten AUSGANG = null;

    public Karte() {
    }

    public Karte(ArrayList<Knoten> knoten, ArrayList<Kante> kanten) {
        this.knoten = knoten;
        this.kanten = kanten;
    }

    public ArrayList<Knoten> getKnoten() {
        return knoten;
    }

    protected void setKnoten(ArrayList<Knoten> knotenListe) {
        knoten = knotenListe;
    }

    protected void setKanten(ArrayList<Kante> kantenListe) {
        kanten = kantenListe;
    }

    public ArrayList<Kante> getKanten() {
        return kanten;
    }
}
