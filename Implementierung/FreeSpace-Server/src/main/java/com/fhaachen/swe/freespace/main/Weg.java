package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.JsonHelper;
import com.fhaachen.swe.freespace.maps.*;
import com.fhaachen.swe.freespace.maps.Campus.Campus;
import com.fhaachen.swe.freespace.maps.GebäudeG.G1;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Simon on 08.12.2016.
 */
public class Weg {

    public static String getWeg(String startID, String zielID) {
        String antwort = null;
        Knoten start = new Knoten(startID);
        Knoten ziel = new Knoten(zielID);
        LinkedList<Knoten> weg = getWeg(start, ziel);
        if(weg != null) {
            antwort = JsonHelper.getJsonStringFromMap(weg);
        }
        return antwort;
    }

    private static LinkedList<Knoten> getWeg(Knoten start, Knoten ziel) {
        if(start != null && ziel != null) {
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

    private static LinkedList<Knoten> intern(Knoten start, Knoten ziel) {
        if(start.getId().charAt(1) == ziel.getId().charAt(1)) {
            return gleicheEtage(start, ziel);
        } else {
            return andereEtage(start, ziel);
        }
    }

    private static LinkedList<Knoten> extern(Knoten start, Knoten ziel) {
        if(     start.getId().charAt(1) == '1' && start.getId().charAt(2) == '0' && start.getId().charAt(3) == '0' &&
                start.getId().charAt(1) == '1' && start.getId().charAt(2) == '0' && start.getId().charAt(3) == '0')
        {
            Karte karte = new Campus();
            Dijkstra dijkstra = new Dijkstra(karte.getKnoten(), karte.getKanten());
            dijkstra.start(start);
            return dijkstra.getWeg(ziel);
        }
        Karte gebäude1 = getKarte(start);
        Karte gebäude2 = getKarte(ziel);
        LinkedList<Knoten> antwort = null;
        if(gebäude1 != null && gebäude2 != null) {
            antwort = getWeg(start, gebäude1.AUSGANG);
            antwort.addAll(getWeg(gebäude1.AUSGANG, gebäude2.AUSGANG));
            antwort.addAll(getWeg(gebäude2.AUSGANG, ziel));
        }
        return antwort;
    }

    private static LinkedList<Knoten> gleicheEtage(Knoten start, Knoten ziel) {
        Karte karte = getKarte(start);
        if(karte != null) {
            Dijkstra dijkstra = new Dijkstra(karte.getKnoten(), karte.getKanten());
            dijkstra.start(start);
            return dijkstra.getWeg(ziel);
        }
        return null;
    }

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

    private static boolean checkFormat(Knoten knoten) {
        String id = knoten.getId();
        if(id.length() == 4) {
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
