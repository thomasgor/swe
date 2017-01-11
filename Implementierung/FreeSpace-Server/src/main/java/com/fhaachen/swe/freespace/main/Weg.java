package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.JsonHelper;
import com.fhaachen.swe.freespace.maps.*;
import com.fhaachen.swe.freespace.maps.Campus.Campus;
import com.fhaachen.swe.freespace.maps.GebaeudeG.G1;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;


/**
 * Klasse Weg beinhaltet die Logik um den kürzesten zwischen 2 Räumen der Karten-Datenstruktur bestimmen zu können.
 *
 * {@link Karte}
 * @author Simon Catley
 * @version 1.3
 */

public class Weg {

    /**
     * Erstellt ein Response-Objekt mit HTTP-Statuscode OK und einer Liste der Knoten, in der Reihenfolge in der sie
     * abgegangen werden müssen um vom Raum mit der ID startID zum Raum mit der ID zielID auf dem kürzesten Weg zu
     * gelangen, als String im Json-Format. Falls kein Weg gefunden wurde, wird als Statuscode Bad Request geliefert.
     *
     * @param startID ID des Raumes, an dem gestartet wird
     * @param zielID  ID des Raumes, welchen man erreichen möchte
     * @return Response-Objekt mit Statuscode und bei Erfolg, den Weg als geordnete Liste als String im Json-Format
     */

    public static Response getWeg(String startID, String zielID) {
        String antwort = "{}";
        Knoten start = new Knoten(startID);
        Knoten ziel = new Knoten(zielID);
        LinkedList<Knoten> weg = getWeg(start, ziel);
        if (weg != null) {
            antwort = JsonHelper.getJsonStringFromMap(weg);
            return Response.ok(antwort, MediaType.APPLICATION_JSON).build();
        }
        return Antwort.BAD_REQUEST;
    }

    /**
     * Erstellt eine geordnete Liste aus Knoten, die besucht werden müssen um auf dem kürzesten Weg vom Knoten
     * start zum Knotnen ziel zu gelangen. Falls kein Weg existiert wird null geliefert.
     *
     * @param start Startknoten
     * @param ziel  Zielknoten
     * @return Geordnete Liste aus Knoten, die den kürzesten Weg beschreibt, oder null, falls kein Weg existiert
     */

    private static LinkedList<Knoten> getWeg(Knoten start, Knoten ziel) {
        if (start != null && ziel != null) {
            if (checkFormat(start) && checkFormat(ziel)) {
                if (start.getId().charAt(0) == ziel.getId().charAt(0)) {
                    return intern(start, ziel);
                } else {
                    return extern(start, ziel);
                }
            }
        }
        //Bad Request
        return null;
    }

    /**
     * Wird mit zwei Knoten aufgerufen, die sich im gleichen Gebaeude befinden. Ermittelt den kürzesten Weg zwischen
     * den Knoten start und ziel.
     *
     * @param start Startknoten
     * @param ziel  Zielknoten
     * @return Geordnete Liste der Knoten, die den kürzesten Weg zwischen start und ziel beschreiben, oder null, falls kein
     * Weg existiert
     */

    private static LinkedList<Knoten> intern(Knoten start, Knoten ziel) {
        if (start.getId().charAt(1) == ziel.getId().charAt(1)) {
            return gleicheEtage(start, ziel);
        } else {
            return andereEtage(start, ziel);
        }
    }

    /**
     * Wird mit zwei Knoten aufgerufen, die sich nicht im gleichen Gebaeude befinden. Ermittelt den kuerzesten Weg zwischen
     * dem Knoten start und dem Ausgang des Gebaeudes von start, zwischen den Gebauden von start und ziel, zwischen
     * Gebaeudeeingang des Gebaeudes von ziel und ziel und gibt dies als geordnete Liste aus Knoten zurück.
     *
     * @param start Startknoten
     * @param ziel  Zielknoten
     * @return Geordnete Liste der Knoten, die den kürzesten Weg zwischen start und ziel beschreiben, oder null, falls kein
     * Weg existiert
     */

    private static LinkedList<Knoten> extern(Knoten start, Knoten ziel) {
        if (start.getId().charAt(1) == '1' && start.getId().charAt(2) == '0' && start.getId().charAt(3) == '0' &&
                start.getId().charAt(1) == '1' && start.getId().charAt(2) == '0' && start.getId().charAt(3) == '0') {
            Karte karte = new Campus();
            Dijkstra dijkstra = new Dijkstra(karte.getKnoten(), karte.getKanten());
            dijkstra.start(start);
            return dijkstra.getWeg(ziel);
        }
        Karte gebäude1 = getKarte(start);
        Karte gebäude2 = getKarte(ziel);
        LinkedList<Knoten> antwort = null;
        if (gebäude1 != null && gebäude2 != null) {
            LinkedList<Knoten> innen1 = getWeg(start, gebäude1.AUSGANG);
            LinkedList<Knoten> aussen = getWeg(gebäude1.AUSGANG, gebäude2.AUSGANG);
            LinkedList<Knoten> innen2 = getWeg(gebäude2.AUSGANG, ziel);
            if (innen1 != null && aussen != null && innen2 != null) {
                antwort = new LinkedList<Knoten>();
                antwort.addAll(innen1);
                antwort.addAll(aussen);
                antwort.addAll(innen2);
            }
        }
        return antwort;
    }

    /**
     * Wird mit zwei Knoten aufgerufen, die sich auf der gleichen Etage und im gleichen Gebaeude  befinden.
     * Gibt eine geordnete Liste der Knoten zurück, die besucht werden müssen, um auf dem kürzesten weg von start
     * nach ziel zu gelangen, oder null, falls dieser nicht existiert
     *
     * @param start Startknoten
     * @param ziel Zielknoten
     * @return Geordnete Liste aller Knoten, die den kürzesten Weg zwischen start und ziel beschreibt, oder null, falls
     * dieser nicht existiert
     */

    private static LinkedList<Knoten> gleicheEtage(Knoten start, Knoten ziel) {
        Karte karte = getKarte(start);
        if(karte != null) {
            Dijkstra dijkstra = new Dijkstra(karte.getKnoten(), karte.getKanten());
            dijkstra.start(start);
            return dijkstra.getWeg(ziel);
        }
        return null;
    }

    /**
     * Wird mit zwei Knoten aufgerufen, die sich nicht auf der gleichen Etage, aber im gleichen Gebaeude befinden.
     * Gibt eine geordnete Liste der Knoten zurück, die besucht werden müssen, um auf dem kürzesten weg von start
     * nach ziel zu gelangen, oder null, falls dieser nicht existiert
     *
     * @param start Startknoten
     * @param ziel Zielknoten
     * @return Geordnete Liste aller Knoten, die den kürzesten Weg zwischen start und ziel beschreibt, oder null, falls
     * dieser nicht existiert
     */

    private static LinkedList<Knoten> andereEtage(Knoten start, Knoten ziel) {
        if(start.getId().charAt(1) > ziel.getId().charAt(1)) {
                LinkedList<Knoten> antwort = new LinkedList<Knoten>();
                char[] startId = start.getId().toCharArray();
                char[] zielId = ziel.getId().toCharArray();
                while(startId[1] > zielId[1]) {
                    antwort.addAll(gleicheEtage(new Knoten(startId.toString()), new Knoten(Karte.ETAGE_RUNTER)));
                    startId[1] -= 1;
                }
                antwort.addAll(gleicheEtage(new Knoten(Karte.ETAGE_HOCH), new Knoten(zielId.toString())));
                return antwort;
        }
        else {
                LinkedList<Knoten> antwort = new LinkedList<Knoten>();
                char[] startId = start.getId().toCharArray();
                char[] zielId = ziel.getId().toCharArray();
                while(startId[1] < zielId[1]) {
                    antwort.addAll(gleicheEtage(new Knoten(startId.toString()), new Knoten(Karte.ETAGE_HOCH)));
                    startId[1] += 1;
                }
                antwort.addAll(gleicheEtage(new Knoten(Karte.ETAGE_RUNTER), new Knoten(zielId.toString())));
                return antwort;
            }
    }

    /**
     * Ermittelt und liefert die zum Knoten knoten zugehörige Karte zurück, oder null, falls diese nicht existiert, bzw.
     * der Knoten fehlerhaft ist
     *
     * @param knoten der Knoten, für den die Karte geladen werden soll
     * @return zum Knoten zugehörige Karte
     */


    private static Karte getKarte(Knoten knoten) {
        // neue Gebäude müssen mit Kürzel hier hinzugefügt werden
        switch(knoten.getId().charAt(0)) {
            case 'C':   //NOT IMPLEMENTED
                return null;
            case 'D':   //NOT IMPLEMENTED
                return null;
            case 'E':   //NOT IMPLEMENTED
                return null;
            case 'F':   //NOT IMPLEMENTED
                return null;
            case 'G':
                switch(knoten.getId().charAt(1)) {
                    case '1':
                        return new G1();
                    case '2':   //NOT IMPLEMENTED
                        return null;
                    case '3':   //NOT IMPLEMENTED
                        return null;
                    default:
                        return null;
                }
            case 'H':   //NOT IMPLEMENTED
                return null;
            case 'W':   //NOT IMPLEMENTED
                return null;
            // Platzhalter für Campus-Karte
            case Campus.ID:      //NOT IMPLEMENTED
                return null;
            default:
                return null;
        }
    }

    /**
     * Überprüft die Foramtierung der ID des übergebenen Knotens auf Korrektheit.
     * Def.:
     *      Die ID ist immer 4 chars lang.
     *      An 1. Stelle steht die Gebaeudekennung als Grossbuchstabe
     *      An 2. Stelle steht die Etagenkennung. Die Zählung beginnt im Erdgeschoss bei 1.
     *      An 3. und 4. Stelle steht die Raumnummer
     *
     * @param knoten Knoten, der überprüft werden soll
     * @return true, falls die KnotenID dem Format entspricht, anderenfalls false
     */

    private static boolean checkFormat(Knoten knoten) {
        String id = knoten.getId();
        if(id.length() >= 4) {
            if (       id.charAt(0) >= 'A' && id.charAt(0) <= 'Z'
                    && id.charAt(1) >= '0' && id.charAt(1) <= '9'
                    && id.charAt(2) >= '0' && id.charAt(2) <= '9'
                    && id.charAt(3) >= '0' && id.charAt(3) <= '9') {
                return true;
            }
        }
        return false;
    }
}
