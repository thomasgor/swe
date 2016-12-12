package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.JsonHelper;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.annotations.CompositePK;
import org.javalite.activejdbc.annotations.Table;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.ResponseCache;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by thomas on 27.11.2016.
 */

@Table("Freundschaft")
@CompositePK({ "Benutzer", "Freund" })

public class Freundschaft extends Datenbank{

    static {
        validatePresenceOf("Benutzer", "Freund").message("ein oder mehrere Primärschlüssel fehlen!!!");
    }

    private static String includeBenutzer(String json) {
        connect();
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
        disconnect();
        return antwort;
    }

    public static Response getFreundschaften(String benutzerID) {
        connect();
        String antwort = "[]";
        try {
            LazyList freunde = Freundschaft.find("benutzer = ? OR freund = ?", benutzerID, benutzerID);
            if (freunde != null) {
                antwort = freunde.toJson(true);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        antwort = includeBenutzer(antwort);
        return Response.ok(antwort, MediaType.APPLICATION_JSON).build();
    }

    //erstellt neue Freundschaft mit Status 0(Anfrage), muss somit noch durch put bejaht, oder durch delete verneint werden
    public static Response postFreundschaft(String benutzerID, String freundID) {
        connect();
        String antwort = null;
        //prüfen ob Freundschaft bereits besteht
        try {
            if (Freundschaft.findByCompositeKeys(benutzerID, freundID) != null || Freundschaft.findByCompositeKeys(freundID, benutzerID) != null) {
                return Antwort.BAD_REQUEST;
            }
            antwort = Freundschaft.createIt("Benutzer", benutzerID, "Freund", freundID, "Status", 0).toJson(true);
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        antwort = includeBenutzer(antwort);
        return Response.ok(antwort,MediaType.APPLICATION_JSON).build();
    }


    //ändert Status der freundschaft von 0 (Anfrage) auf 1 (Freundschaft zugestimmt)
    public static Response putFreundschaft(String benutzerID, String freund) {
        connect();
        String antwort = null;
        //prüfen ob Freundschaft bereits besteht
        try {
            Freundschaft freu = Freundschaft.findByCompositeKeys(benutzerID, freund);
            if (freu == null) {
                freu = Freundschaft.findByCompositeKeys(freund, benutzerID);
                if(freu == null) {
                    return Antwort.BAD_REQUEST;
                }
            }
            freu.set("Status", 1).saveIt();
            antwort = freu.toJson(true);
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        antwort = includeBenutzer(antwort);
        return Response.ok(antwort,MediaType.APPLICATION_JSON).build();
    }

    public static Response deleteFreundschaft(String benutzerID, String freund) {
        connect();
        String antwort = null;
        //prüfen ob Freundschaft besteht
        try {
            Freundschaft freu = Freundschaft.findByCompositeKeys(benutzerID, freund);
            if (freu == null) {
                freu = Freundschaft.findByCompositeKeys(freund, benutzerID);
                if(freu == null) {
                    return Antwort.BAD_REQUEST;
                }
            }
            freu.deleteCascade();
            antwort = freu.toJson(true);
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        return Response.ok(antwort, MediaType.APPLICATION_JSON).build();
    }
}
