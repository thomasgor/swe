package com.fhaachen.swe.freespace.maps.Campus;

import com.fhaachen.swe.freespace.maps.Karte;
import com.fhaachen.swe.freespace.maps.Knoten;

/**
 * NOT IMPLEMENTED
 * Klasse Campus ist eine von Karte{@link Karte} abgeleitete Karte, die das Aussengelaende der FH Aachen
 * in der Eupenerstrasse 70 modelliert.
 *
 * @author Simon Catley
 * @version 1.0
 */

public class Campus extends Karte {
    // Paltzhalter fuer die ID der Campus-Karte
    public static final char ID = '!';
    // Statischer konstanter Knoten der fuer den Eingang/Ausgang des Gebaeudes C steht
    public static final Knoten GEBAEUDE_C = new Knoten("C100");
    // Statischer konstanter Knoten der fuer den Eingang/Ausgang des Gebaeudes D steht
    public static final Knoten GEBAEUDE_D = new Knoten("D100");
    // Statischer konstanter Knoten der fuer den Eingang/Ausgang des Gebaeudes E steht
    public static final Knoten GEBAEUDE_E = new Knoten("E100");
    // Statischer konstanter Knoten der fuer den Eingang/Ausgang des Gebaeudes F steht
    public static final Knoten GEBAEUDE_F = new Knoten("F100");
    // Statischer konstanter Knoten der fuer den Eingang/Ausgang des Gebaeudes G steht
    public static final Knoten GEBAEUDE_G = new Knoten("G100");
    // Statischer konstanter Knoten der fuer den Eingang/Ausgang des Gebaeudes H steht
    public static final Knoten GEBAEUDE_H = new Knoten("H100");
    // Statischer konstanter Knoten der fuer den Eingang/Ausgang des Gebaeudes W steht
    public static final Knoten GEBAEUDE_W = new Knoten("W100");

    public Campus() {
        this.setUp();
    }

   /**
    * NOT IMPLEMENTED
    * Diese Methode erzeugt alle Knoten, Kanten und deren Verbindungen, die noetig sind um die zu modellierende Karte darzustellen und
    * mit Dijkstra{@link com.fhaachen.swe.freespace.maps.Dijkstra} bearbeiten zu koennen und fuegt diese in die
    * geerbten Attribute knoten bzw. kanten{@link Karte} ein.
    */
    public void setUp() {
        // hier Karte definieren
    }
}
