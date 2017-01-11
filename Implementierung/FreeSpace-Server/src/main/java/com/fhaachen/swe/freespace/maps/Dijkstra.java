package com.fhaachen.swe.freespace.maps;

import java.util.*;

/**
 *  Klasse Dijkstra stellt die Umsetzung des Dijkstra-Algorythmus zur kuerzesten Wegfindung dar. Wird nur fuer zwei
 *  Knoten auf der selben Karte aufgerufen. Der kuerzeste Weg ist hierbei der, der zwischen zwei Knoten die geringste Summe
 *  aller Kantengewichte aufweist.
 *  {@link Karte}
 *
 *  @author Simon Catley
 *  @version 1.1
 */

public class Dijkstra {

    // Liste aller Knoten auf der Karte
    private final List<Knoten> knoten;
    // Liste aller Kanten auf der Karte
    private final List<Kante> kanten;
    // Ein Set aller besuchten Knoten um Schleifen zu vermeiden
    private Set<Knoten> besuchteKnoten;
    // Ein Set aller unbesuchten Knoten. Solange nicht leer, wird weitergesucht
    private Set<Knoten> unbesuchteKnoten;
    // Eine Map mit den Vorgängern aller Knoten bis hin zum Startknoten
    private Map<Knoten, Knoten> vorgänger;
    // Eine Map die die Summe aller Gewichte auf dem kürzesten Weg vom Startknoten zu allen Knoten speichert
    private Map<Knoten, Integer> entfernung;

    /**
     * Überladener Konstruktor. Setzt Attribute knoten und kanten des Objektes gleich den Uebergabeparametern
     *
     * @param knoten Neue Knotenliste
     * @param kanten Neue Kantenliste
     */

    public Dijkstra(ArrayList<Knoten> knoten, ArrayList<Kante> kanten) {
        this.knoten = knoten;
        this.kanten = kanten;
    }

    /**
     * Beginnt den Algorythmus, initialisiert alle nicht gesetzten Attribute und bestimmt den kuerzesten
     * Weg aller Knoten zum Startknoten
     *
     * @param start
     */

    public void start(Knoten start) {
        besuchteKnoten = new HashSet<Knoten>();
        unbesuchteKnoten = new HashSet<Knoten>();
        entfernung = new HashMap<Knoten, Integer>();
        vorgänger = new HashMap<Knoten, Knoten>();
        entfernung.put(start, 0);
        unbesuchteKnoten.add(start);
        while (unbesuchteKnoten.size() > 0) {
            Knoten knoten = getMinimum(unbesuchteKnoten);
            besuchteKnoten.add(knoten);
            unbesuchteKnoten.remove(knoten);
            findeMinimaleEntfernung(knoten);
        }
    }

    /**
     * Setzt die kuerzeste Entfernung der Nachbarn eines Knotens ueber diesen zum Startknoten auf die Entfernung des
     * aufgerufenen Knotens + das Gewicht der Kante zwischen Knoten und Nachbar, falls diese nicht, oder kleiner als
     * die bereits ermittelte ist. Ausserdem werden diese Knoten in die Liste der nicht besuchten Knoten aufgenommen und
     * der aufgerufene Knoten als Vorgaenger gesetzt.
     *
     * @param knoten Knoten, dessen Nachbarn geprueft werden
     */

    private void findeMinimaleEntfernung(Knoten knoten) {
        ArrayList<Knoten> nachbarKnoten = getNachbarn(knoten);
        for (Knoten nachbar : nachbarKnoten) {
            if (getKürzesteEntfernung(nachbar) > getKürzesteEntfernung(knoten) + getEntfernung(knoten, nachbar)) {
                entfernung.put(nachbar, getKürzesteEntfernung(knoten) + getEntfernung(knoten, nachbar));
                vorgänger.put(nachbar, knoten);
                unbesuchteKnoten.add(nachbar);
            }
        }
    }

    /**
     * Sucht die Kante aus der Liste Kante, die die zwei Knoten knoten und ziel verbindet und liefert dessen Gewicht
     * zurueck
     *
     * @param knoten Erster Knoten, fuer den eine Kante gesucht wird
     * @param ziel Zweiter Knoten, fuer den eine Kanten gesucht wird
     * @return Gewicht der gefundenen Kante
     */

    private int getEntfernung(Knoten knoten, Knoten ziel) {
        for (Kante kante : kanten) {
            if ((kante.getAnfang().equals(knoten) && kante.getEnde().equals(ziel)) || (kante.getEnde().equals(knoten) && kante.getAnfang().equals(ziel))) {
                return kante.getGewicht();
            }
        }
        throw new RuntimeException("Knoten keine Nachbarn. Sollte nicht passieren!!!");
    }

    /**
     * Bestimmt alle Nachbarn eines ubergebenen Knotens knoten und gibt diese als ArrayList zurueck.
     *
     * @param knoten Knoten dessen Nachbarn gesucht werden
     * @return ArrayList von allen Nachbarknoten von knoten
     */

    private ArrayList<Knoten> getNachbarn(Knoten knoten) {
        ArrayList<Knoten> nachbarn = new ArrayList<Knoten>();
        for (Kante kante : kanten) {
            if (kante.getAnfang().equals(knoten) && !isBesucht(kante.getEnde())) {
                nachbarn.add(kante.getEnde());
            } else if (kante.getEnde().equals(knoten) && !isBesucht(kante.getAnfang())) {
                nachbarn.add(kante.getAnfang());
            }
        }
        return nachbarn;
    }

    /**
     * Bestimmt aus einem uebergebenen Set knotenSet vom Typ Knoten, den Knoten mit der kuerzesten Entfernung zum Startknoten
     *
     * @param knotenSet Set aus Knoten fuer das das Minimum gesucht ist
     * @return Den Knoten aus dem Set knotenSet mit der geringsten Entfernung zum Startknoten
     */

    private Knoten getMinimum(Set<Knoten> knotenSet) {
        Knoten minimum = null;
        for (Knoten knoten : knotenSet) {
            if (minimum == null) {
                minimum = knoten;
            } else {
                if (getKürzesteEntfernung(knoten) < getKürzesteEntfernung(minimum)) {
                    minimum = knoten;
                }
            }
        }
        return minimum;
    }

    /**
     * Bestimmt ob ein Knoten knoten bereits besucht wurde
     *
     * @param knoten Knoten der gepueft werden soll
     * @return true, falls der Knoten bereits besucht wurde, ansonsten false
     */

    private boolean isBesucht(Knoten knoten) {
        return besuchteKnoten.contains(knoten);
    }

    /**
     * Falls fuer den ueberebenen Knoten ziel bereits eine Entfernung bestimmt worden ist, wird diese zurueckgegeben, ansonsten
     * der Integermaximalwert.
     *
     * @param ziel Knoten der geprueft werden soll
     * @return Die Entfernung des Knotens zum Startknoten, falls diese bestimmt wurde, ansonsten Integer.MAX_VALUE
     */

    private int getKürzesteEntfernung(Knoten ziel) {
        Integer gewicht = entfernung.get(ziel);
        if (gewicht == null) {
            return Integer.MAX_VALUE;
        } else {
            return gewicht;
        }
    }

    /**
     * Erstellt eine LinkedList aller Knoten die abgegangen werden müssen um auf dem kuerzesten Weg von
     * dem vorher in der Methode start festgelegten Startknoten nach ziel zu gelangen. Gibt null zurueck falls kein Weg
     * existiert
     *
     * @param ziel Zu erreichender Zielknoten
     * @return Geordnete Liste aller Knoten die den kuerzesten Weg zwischen Start und Ziel beschreiben, oder null, falls
     * keiner existiert
     */

    public LinkedList<Knoten> getWeg(Knoten ziel) {
        LinkedList<Knoten> weg = new LinkedList<Knoten>();
        Knoten schritt = ziel;
        // existiert?
        if (vorgänger.get(schritt) == null) {
            return null;
        }
        weg.add(schritt);
        while (vorgänger.get(schritt) != null) {
            schritt = vorgänger.get(schritt);
            weg.add(schritt);
        }
        // korrekte Reihenfolge
        Collections.reverse(weg);
        return weg;
    }
}
