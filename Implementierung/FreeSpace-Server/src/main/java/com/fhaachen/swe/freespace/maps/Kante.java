package com.fhaachen.swe.freespace.maps;

/**
 *  Klasse Kante stellt eine Hilfsklasse fuer die Realissierung der Karten-Logik dar. Eine Kante steht fuer die Strecke
 *  zwischen zwei Knoten{@link Knoten}.
 *  {@link Karte}
 *
 *  @author Simon Catley
 *  @version 1.0
 */

public class Kante {

    // Eindeutiger Identifizierer im Format: "G111", wobei "G" fuer die Gebaeudekennung, die erste Zahl für die Etage, beginnend bei 1
    // fuer Erdgeschoss und die letzten beiden zur identifizierung stehen
    private final String id;
    // Erster Knoten der Kante, obwohl anfang genannt, ist die Kante ungerichtet
    private final Knoten anfang;
    // Zweiter Knoten der Kante, obwohl ende genannt, ist die Kante ungerichtet
    private final Knoten ende;
    // Gewicht der Kante welches zur Entfernungsbestimmung genuzt werden kann(NOT IMPLEMENTED)
    private final int gewicht;

    /**
     * Konstruktor mit allen Attributwerten in Übergabeparametern
     *
     * @param id ID der neuen Kante
     * @param anfang Erster Knoten
     * @param ende Zweiter Knoten
     * @param gewicht Gewicht der Kante
     */

    public Kante(String id, Knoten anfang, Knoten ende, int gewicht) {
        id.toUpperCase();
        this.id = id;
        this.anfang = anfang;
        this.ende = ende;
        this.gewicht = gewicht;
    }

    /**
     * Getter des ersten Knoten. Trotz des Namen der Methode, ist die Kante ungerichtet
     *
     * @return Erster Knoten
     */

    public Knoten getAnfang() {
        return anfang;
    }

    /**
     * Getter des zweiten Knoten. Trotz des Namen der Methode, ist die Kante ungerichtet
     *
     * @return Zweiter Knoten
     */

    public Knoten getEnde() {
        return ende;
    }

    /**
     * Getter der ID der Kante
     *
     * @return ID-Attribut
     */

    public String getId() {
        return id;
    }

    /**
     * Getter des Gewichtes
     *
     * @return Wert des Gewicht-Attributes
     */

    public int getGewicht() {
        return gewicht;
    }

    /**
     * Überlagerung der equals-Methode
     *
     * @param o Zu pruefendes Objekt
     * @return true, falls beide Objekte identisch sind, oder gleichen Inhalt haben. Ansonsten false
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Kante)) return false;

        Kante kante = (Kante) o;

        if (getGewicht() != kante.getGewicht()) return false;
        if (!getId().equals(kante.getId())) return false;
        if (!getAnfang().equals(kante.getAnfang())) return false;
        return getEnde().equals(kante.getEnde());
    }

    /**
     * Überlagerung der hashCode-Methode. Erzeugt eindeutigen Hashwert aus allen Attributen des Objektes.
     *
     * @return Eindeutiger Haswert des Objektes
     */

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getAnfang().hashCode();
        result = 31 * result + getEnde().hashCode();
        result = 31 * result + getGewicht();
        return result;
    }

    /**
     * Überlagerte toString-Methode. Format: [ "ID des ersten Knoten" -> "ID des zweiten Knoten" ]
     *
     * @return String der die Daten des Objektes beschreibt
     */

    @Override
    public String toString() {
        return "[ " + getAnfang().getId() + " -> " + getEnde().getId() + " ]";
    }

    /**
     * Überlagerte clone-Methode.
     *
     * @return Kopie des Objektes
     */

    public Kante clone() {
        return new Kante(this.getId(), this.getAnfang().clone(), this.getEnde().clone(), this.getGewicht());
    }
}
