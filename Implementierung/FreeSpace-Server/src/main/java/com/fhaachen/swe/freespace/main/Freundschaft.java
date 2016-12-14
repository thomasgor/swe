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

    private static String includeBenutzerMaps(String json) {
        Map[] map = JsonHelper.toMaps(json);
        String antwort = "[]";
        if(map != null) {
                for (Map element : map) {
                    connect();
                    try {
                    System.out.println(element.toString());
                    Benutzer ben = Benutzer.findById(element.get("benutzer"));
                    Benutzer fre = Benutzer.findById(element.get("freund"));

                    element.put("raum", null);

                    if (ben != null) {
                        ben.set("istAnonym", null);
                        ben.set("token", null);
                        String jsonBenutzer = ben.toJson(true);
                        Map benutzer = JsonHelper.toMap(jsonBenutzer);
                        element.put("benutzer", benutzer);
                    }
                    if (fre != null) {

                        Sitzung sitz = Sitzung.findFirst("benutzer = ?", fre.getId());
                        if(sitz != null) {
                            String raum = Raum.getRaumByID(sitz.get("raum").toString());
                            Map raumMap = JsonHelper.toMap(raum);
                            element.put("raum", raumMap);
                        }
                        fre.set("istAnonym", null);
                        fre.set("token", null);
                        String jsonFreund = fre.toJson(true);
                        Map freund = JsonHelper.toMap(jsonFreund);
                        element.put("freund", freund);
                    }
                    System.out.println(element.toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                        return json;
                }
                disconnect();
            }
            antwort = JsonHelper.getJsonStringFromMap(map);
        }
        return antwort;
    }

    private static String includeBenutzerMap(String json) {
        connect();
        Map map = JsonHelper.toMap(json);
        String antwort = null;
        if (map != null) {
            try {
                System.out.println(map.toString());
                Benutzer ben = Benutzer.findById(map.get("benutzer"));
                Benutzer fre = Benutzer.findById(map.get("freund"));
                if (ben != null) {
                    ben.set("istAnonym", null);
                    ben.set("token", null);
                    String jsonBenutzer = ben.toJson(true);
                    Map benutzer = JsonHelper.toMap(jsonBenutzer);
                    map.put("benutzer", benutzer);
                }
                if (fre != null) {
                    fre.set("istAnonym", null);
                    fre.set("token", null);
                    String jsonFreund = fre.toJson(true);
                    Map freund = JsonHelper.toMap(jsonFreund);
                    map.put("freund", freund);
                }
                System.out.println(map.toString());


            } catch (Exception e) {
                e.printStackTrace();
                return json;
            }
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
        antwort = includeBenutzerMaps(antwort);
        return Response.ok(antwort, MediaType.APPLICATION_JSON).build();
    }

    //erstellt neue Freundschaft mit Status 0(Anfrage), muss somit noch durch put bejaht, oder durch delete verneint werden
    public static Response postFreundschaft(String benutzerID, String json) {
        connect();
        String antwort = null;
        Benutzer freund = null;
        try {
            String email = JsonHelper.getAttribute(json, "email");
            freund = Benutzer.findFirst("email = ?", email);
        } catch (Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        if(freund == null) {
            return Antwort.BAD_REQUEST;
        }
        String freundID = freund.getId().toString();
        //prüfen ob Freundschaft bereits besteht
        if(benutzerID.equals(freundID)) {
            return Antwort.BAD_REQUEST;
        }
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
        antwort = includeBenutzerMap(antwort);
        return Response.ok(antwort,MediaType.APPLICATION_JSON).build();
    }


    //ändert Status der freundschaft von 0 (Anfrage) auf 1 (Freundschaft zugestimmt)
    public static Response putFreundschaft(String benutzerID, String freund) {
        connect();
        String antwort = null;
        //prüfen ob Freundschaft bereits besteht
        try {
            Freundschaft freu = Freundschaft.findByCompositeKeys(freund, benutzerID);
            if(freu == null) {
                return Antwort.BAD_REQUEST;
            }

            freu.set("Status", 1).saveIt();
            antwort = freu.toJson(true);
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        antwort = includeBenutzerMap(antwort);
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
        return Response.ok().build();
    }
}
