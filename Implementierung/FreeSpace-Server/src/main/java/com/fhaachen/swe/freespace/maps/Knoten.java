package com.fhaachen.swe.freespace.maps;

/**
 *  Klasse Knoten stellt eine Hilfsklasse fuer die Realissierung der Karten-Logik dar. Ein Knoten steht entweder fuer einen
 *  bestimmten Raum auf einer der Karten, oder einen bestimmten Punkt auf dem Weg zwischen den Knoten, an dem eine Abzweigung
 *  des Weges besteht
 *  {@link Karte}
 *
 *  @author Simon Catley
 *  @version 1.0
 */

public class Knoten {

    //Eindeutige Identifizierer im Format: "G111", wobei "G" fuer die Gebaeudekennung, die erste Zahl für die Etage, beginnend bei 1
    // fuer Erdgeschoss und die letzten beiden fuer die Raumnummer stehen
    private final String id;

    /**
     * Konstruktor der Klasse Knoten mit Übergabeparameter id als String. Bei der ID wird sicher gegangen, dass Buckstaben
     * gross geschrieben sind.
     *
     * @param id ID des neuen Knoten
     */

    public Knoten(String id) {
        id.toUpperCase();
        this.id = id;
    }

    /**
     * Getter des Attributes id
     *
     * @return Attributwert von id
     */

    public String getId() {
        return id;
    }

    /**
     * Überlagerte equals-Methode
     *
     * @param o Zu pruefendes Objekt
     * @return true, falls Objekte identisch, sonst false
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Knoten)) return false;

        Knoten knoten = (Knoten) o;

        return getId().equals(knoten.getId());
    }

    /**
     * Überlagerte hashCode-Methode. Erzeugt eindeutigen Hashwert aus allen Attributen des Objektes.
     *
     * @return Eindeutiger Haswert des Objektes
     */

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Überlagerte toString-Methode. Format: [ ID ]
     *
     * @return String der die Daten des Objektes beschreibt
     */

    @Override
    public String toString() {
        return "[ " + this.getId() + " ]";
    }

    /**
     * Überlagerte clone-Methode.
     *
     * @return Kopie des Objektes
     */

    public Knoten clone(){
        return new Knoten(this.getId());
    }
}
