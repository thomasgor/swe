package com.fhaachen.swe.freespace.maps;

/**
 * Created by Simon on 08.12.2016.
 */
public class Knoten {

    private final String id;

    public Knoten(String id) {
        id.toUpperCase();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Knoten)) return false;

        Knoten knoten = (Knoten) o;

        return getId().equals(knoten.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return "[ " + this.getId() + " ]";
    }

    public Knoten clone(){
        return new Knoten(this.getId());
    }
}
