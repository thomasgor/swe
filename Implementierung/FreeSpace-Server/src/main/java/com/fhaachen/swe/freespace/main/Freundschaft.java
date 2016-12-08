package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.JsonHelper;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.annotations.Table;

import java.util.Map;

/**
 * Created by thomas on 27.11.2016.
 */

@Table("Freundschaft")

public class Freundschaft extends Datenbank{

    private static String includeBenutzer(String json) {
        Map[] map = JsonHelper.toMaps(json);
        try {
            for (Map element : map) {
                System.out.println(element.toString());
                Benutzer ben = Benutzer.findById(element.get("benutzer"));
                Benutzer fre = Benutzer.findById(element.get("freund"));

                if (ben != null) {
                    String jsonBenutzer = ben.toJson(true);
                    Map benutzer = JsonHelper.toMap(jsonBenutzer);
                    element.put("benutzer", benutzer);
                }
                if (fre != null) {
                    String jsonFreund = fre.toJson(true);
                    Map freund = JsonHelper.toMap(jsonFreund);
                    element.put("freund", freund);
                }
                System.out.println(element.toString());

            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        String antwort = "[]";
        if (map != null) {
            antwort = JsonHelper.getJsonStringFromMap(map);
        }
        return antwort;
    }

    public static String getFreundschaften(String benutzerID) {
        connect();
        String antwort = "[]";
        try {
            LazyList freunde = Freundschaft.find("benutzer = ? OR freund = ?", benutzerID, benutzerID);
            if (freunde != null) {
                antwort = freunde.toJson(true);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        disconnect();
        return includeBenutzer(antwort);
    }

    public static String postFreundschaft(String benutzerID, String json) {
        connect();
        String antwort = null;
        String freund = JsonHelper.getAttribute(json, "freund");
        int status = Integer.valueOf(JsonHelper.getAttribute(json, "status"));
        //prüfen ob Freundschaft bereits besteht
        try {
            if (Freundschaft.findByCompositeKeys(benutzerID, freund) != null) {
                return null;
            }
            antwort = Freundschaft.createIt("Benutzer", benutzerID, "Freund", freund, "Status", status).toJson(true);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        disconnect();
        return antwort;
    }

    public static String putFreundschaft(String benutzerID, String freund, String json) {
        connect();
        String antwort = null;
        int status = Integer.valueOf(JsonHelper.getAttribute(json, "status"));
        //prüfen ob Freundschaft bereits besteht
        try {
            Freundschaft freu = Freundschaft.findByCompositeKeys(benutzerID, freund);
            if (freu == null) {
                return null;
            }
            freu.set("Status", status).saveIt();
            antwort = freu.toJson(true);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        disconnect();
        return antwort;
    }

    public static String deleteFreundschaft(String benutzerID, String freund) {
        connect();
        String antwort = null;
        //prüfen ob Freundschaft besteht
        try {
            Freundschaft freu = Freundschaft.findByCompositeKeys(benutzerID, freund);
            if (freu == null) {
                return null;
            }
            freu.deleteCascade();
            antwort = freu.toJson(true);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        disconnect();
        return antwort;
    }
}
