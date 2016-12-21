package com.fhaachen.swe.freespace.maps;

import java.util.ArrayList;

/**
 *  Klasse Karte stellt eine abstrakte Oberklasse fuer alle speziefischen Karten dar
 *
 *  @author Simon Catley
 *  @version 1.1
 */

public class Karte {

    // Liste aller Knoten einer Karte
    private static ArrayList<Knoten> knoten = new ArrayList<Knoten>();
    // Liste aller Kanten einer Karte
    private static ArrayList<Kante> kanten = new ArrayList<Kante>();
    // Statische Konstante um Treppenaufgaenge zu markieren
    public static final String ETAGE_HOCH = "hoch";
    // Statische Konstante um Treppenabgaenge zu markieren
    public static final String ETAGE_RUNTER = "runter";
    // Statischer Variable um Gebaeudeausgaenge zu markieren. Format: G100 der Buchstabe steht fuer die Gebaeudekennung
    // und variiert je nach Gebauede, die Zahlenkombination 100 steht auf jeder Karte fuer Gebaeudeeingang/-ausgang
    public static Knoten AUSGANG = null;

    /**
     * Standard-Konstruktor. Nicht genutzt, ausser im Hintergrund.
     */

    public Karte() {
    }

    /**
     * Ueberladener Konstruktor, der als Uebergabeparameter eine Knoten- und Kantenliste bekommt und diese als
     * seine Attribute setzt
     *
     * @param knoten Neue Knotenliste
     * @param kanten Neue Kantenliste
     */

    public Karte(ArrayList<Knoten> knoten, ArrayList<Kante> kanten) {
        this.knoten = knoten;
        this.kanten = kanten;
    }

    /**
     * Getter-Methode fuer die Knotenliste.
     *
     * @return Das Attribut knoten
     */

    public ArrayList<Knoten> getKnoten() {
        return knoten;
    }

    /**
     * Setter-Methode fuer die Knotenliste
     *
     * @param knotenListe Neue Knotenliste
     */

    protected void setKnoten(ArrayList<Knoten> knotenListe) {
        knoten = knotenListe;
    }

    /**
     * Getter-Methode fuer die Kantenliste
     *
     * @return Das Attibut kanten
     */

    public ArrayList<Kante> getKanten() {
        return kanten;
    }

    /**
     * Setter-Methode fuer die Kantenliste
     *
     * @param kantenListe Neue Kantenliste
     */

    protected void setKanten(ArrayList<Kante> kantenListe) {
        kanten = kantenListe;
    }
}
