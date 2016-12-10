package com.fhaachen.swe.freespace.maps;

/**
 * Created by Simon on 08.12.2016.
 */
public class Kante {

    private final String id;
    private final Knoten anfang;
    private final Knoten ende;
    private final int gewicht;

    public Kante(String id, Knoten anfang, Knoten ende, int gewicht) {
        id.toUpperCase();
        this.id = id;
        this.anfang = anfang;
        this.ende = ende;
        this.gewicht = gewicht;
    }

    public Knoten getAnfang() {
        return anfang;
    }

    public Knoten getEnde() {
        return ende;
    }

    public String getId() {
        return id;
    }

    public int getGewicht() {
        return gewicht;
    }

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

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getAnfang().hashCode();
        result = 31 * result + getEnde().hashCode();
        result = 31 * result + getGewicht();
        return result;
    }

    @Override
    public String toString() {
        return "[ " + getAnfang().getId() + " -> " + getEnde().getId() + " ]";
    }

    public Kante clone() {
        return new Kante(this.getId(), this.getAnfang().clone(), this.getEnde().clone(), this.getGewicht());
    }
}
