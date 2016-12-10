package com.fhaachen.swe.freespace.maps;

import java.util.*;

/**
 * Created by Simon on 08.12.2016.
 */
public class Dijkstra {

    private final List<Knoten> knoten;
    private final List<Kante> kanten;
    private Set<Knoten> besuchteKnoten;
    private Set<Knoten> unbesuchteKnoten;
    private Map<Knoten, Knoten> vorgänger;
    private Map<Knoten, Integer> entfernung;

        public Dijkstra(ArrayList<Knoten> knoten, ArrayList<Kante> kanten) {
            this.knoten = knoten;
            this.kanten = kanten;
        }

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

        private int getEntfernung(Knoten knoten, Knoten ziel) {
            for (Kante kante : kanten) {
                if ((kante.getAnfang().equals(knoten) && kante.getEnde().equals(ziel)) || (kante.getEnde().equals(knoten) && kante.getAnfang().equals(ziel))) {
                    return kante.getGewicht();
                }
            }
            throw new RuntimeException("Knoten keine Nachbarn. Sollte nicht passieren!!!");
        }

        private ArrayList<Knoten> getNachbarn(Knoten knoten) {
            ArrayList<Knoten> nachbarn = new ArrayList<Knoten>();
            for (Kante kante : kanten) {
                if (kante.getAnfang().equals(knoten) && !isBesucht(kante.getEnde())) {
                    nachbarn.add(kante.getEnde());
                } else if(kante.getEnde().equals(knoten) && !isBesucht(kante.getAnfang())) {
                    nachbarn.add(kante.getAnfang());
                }
            }
            return nachbarn;
        }

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

        private boolean isBesucht(Knoten knoten) {
            return besuchteKnoten.contains(knoten);
        }

        private int getKürzesteEntfernung(Knoten ziel) {
            Integer gewicht = entfernung.get(ziel);
            if (gewicht == null) {
                return Integer.MAX_VALUE;
            } else {
                return gewicht;
            }
        }

        /*
         * NULL falls kein Pfad existiert. Sollte nicht passieren!!!
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