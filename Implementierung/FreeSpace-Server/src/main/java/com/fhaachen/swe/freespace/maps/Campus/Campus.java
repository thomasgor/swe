package com.fhaachen.swe.freespace.maps.Campus;

import com.fhaachen.swe.freespace.maps.Karte;
import com.fhaachen.swe.freespace.maps.Knoten;

/**
 * Created by Simon on 09.12.2016.
 */
public class Campus extends Karte {
    // Paltzhalter
    public static final char ID = '!';
    public static final Knoten GEBAEUDE_C = new Knoten("C100");
    public static final Knoten GEBAEUDE_D = new Knoten("D100");
    public static final Knoten GEBAEUDE_E = new Knoten("E100");
    public static final Knoten GEBAEUDE_F = new Knoten("F100");
    public static final Knoten GEBAEUDE_G = new Knoten("G100");
    public static final Knoten GEBAEUDE_H = new Knoten("H100");
    public static final Knoten GEBAEUDE_W = new Knoten("W100");

    public Campus() {
        this.setUp();
    }

    public void setUp() {
        // hier Karte definieren
    }
}
