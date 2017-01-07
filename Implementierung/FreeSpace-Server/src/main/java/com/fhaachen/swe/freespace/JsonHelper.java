package com.fhaachen.swe.freespace;

import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

/**
 * Die Klasse JsonHelper ist eine Hilfsklasse die es ermöglicht Strings im Json-Format in Java-Maps umzuwandeln und
 * umgekehrt
 *
 * @author Partrick Wueller
 * @version 1.0
 */

public class JsonHelper {

    /**
     * Wandelt einen String im Json-Format, der ein einzelnes Json-Objekt enthaelt, in eine Map um.
     *
     * @param json Umzuwandelndes Json-Objekt als String
     * @return Map mit Keys und Values aus dem Json-Objekt entnommen
     */

    public static Map toMap(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, Map.class);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    /**
     * Wandelt einen String im Json-Format, der eine Liste aus Json-Objekten enthaelt, in ein Map-Array um.
     *
     * @param json Umzuwandelnde Json-Objekt-Liste als String
     * @return Map-Array mit einer Map fuer jedes Listenelement, mit dessen jeweiligen Keys und Values aus
     * dem Json-Objekt entnommen
     */

    public static Map[] toMaps(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, Map[].class);
        } catch (IOException e) { throw new RuntimeException(e); } }


    public static String getAttribute(String json, String attibut){
        Map tmp = toMap(json);
        return String.valueOf(tmp.get(attibut));
    }

    public static Object getAttribute(String json, String attibut1, String attibut2){
        Map tmp = JsonHelper.toMap(json);

        String tag = String.valueOf(tmp.get(attibut1));

        Map m = (Map)tmp.get(attibut1);
        String name = String.valueOf(m.get(attibut2));
        return name;
    }

    /**
     * Wandelt eine Map in einen String im Json-Format um. Keys werden Atrribute und deren
     * Values werden die Attributwerte.
     * Strings in der Map werden im Json-String in Hochkomata gefasst, Zahlen jedoch nicht.
     *
     * @param m Umzuwandelnde Map
     * @return String im Json-Format mit Attributen und Attributwerten aus den Keys und Values der Map m entnommen
     */

    public static String getJsonStringFromMap(Map m){
        try {
            ObjectMapper mapper = new ObjectMapper();
            //   string a = mapper.writeValue();
            return mapper.writeValueAsString(m);

        }catch(Exception e){
            return null;
        }
        // return "";
    }

    /**
     * Wandelt eine LinkedList l in einen String im Json-Format um, dabei wird ein Json-Liste erstellt, bei der jedes Element
     * das Attribute und Attributwerte des Listenelements erhaelt.
     * Strings in der Map werden im Json-String in Hochkomata gefasst, Zahlen jedoch nicht.
     *
     * @param l Umzuwandelnde LinkedList
     * @return String im Json-Format mit Liste aus Elementen, mit Attributen und Attributwerten aus den Attributen der
     * Listenelemente aus l entnommen
     */

    public static String getJsonStringFromMap(LinkedList l){
        try {
            ObjectMapper mapper = new ObjectMapper();
            //   string a = mapper.writeValue();
            return mapper.writeValueAsString(l);

        }catch(Exception e){
            return null;
        }
    }

    /**
     * Wandelt ein Map-Array m in einen String im Json-Format um, dabei wird ein Json-Liste erstellt, bei der jedes Element
     * das Attribute und Attributwerte einer Map des Arrays erhaelt.
     * Strings in der Map werden im Json-String in Hochkomata gefasst, Zahlen jedoch nicht.
     *
     * @param m Umzuwandelndes Map-Array
     * @return String im Json-Format mit Liste aus Elementen, mit Attributen und Attributwerten aus den Attributen der
     * Arrayelemente aus m entnommen
     */

    public static String getJsonStringFromMap(Map[] m){
        try {
            ObjectMapper mapper = new ObjectMapper();
            //   string a = mapper.writeValue();
            return mapper.writeValueAsString(m);

        }catch(Exception e){
            return null;
        }
    }
}