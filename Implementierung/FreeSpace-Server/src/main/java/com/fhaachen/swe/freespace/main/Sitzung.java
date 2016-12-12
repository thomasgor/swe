package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.JsonHelper;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Sitzung")
public class Sitzung extends Datenbank {

    private static String includeBenutzer(String json) {
        connect();
        Map map = JsonHelper.toMap(json);
        try {
            Benutzer ben = Benutzer.findById(map.get("benutzer"));
            if (ben != null) {
                String jsonBenutzer = ben.toJson(true);
                Map benutzer = JsonHelper.toMap(jsonBenutzer);
                map.put("benutzer", benutzer);
            }
            System.out.println(map.toString());
        } catch(Exception e) {
            e.printStackTrace();
            return json;
        }
        if (map != null) {
            disconnect();
            return JsonHelper.getJsonStringFromMap(map);
        }
        disconnect();
        return json;
    }

    public static Response getSitzungById(String id){
        connect();
        String antwort = "{}";
        try {
            Sitzung sitz = Sitzung.findFirst("benutzer = ?", Integer.parseInt(id));
            if (sitz == null) {
                return Antwort.NO_ACTIVE_SESSION;
            }
            antwort = sitz.toJson(true);
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        antwort = includeBenutzer(antwort);
        return Response.ok(antwort, MediaType.APPLICATION_JSON).build();
    }

    public static Response postSitzung(String json) {
        connect();
        String antwort = null;
        int benutzer = Integer.parseInt(JsonHelper.getAttribute(json,"benutzer"));
        int raum = Integer.parseInt(JsonHelper.getAttribute(json, "raum"));
        int endzeit = Integer.parseInt(JsonHelper.getAttribute(json, "endzeit"));
        int hasTag = Integer.parseInt(JsonHelper.getAttribute(json, "hasTag"));
        try {
            Sitzung sitz = Sitzung.findFirst("benutzer = ?", benutzer);
            if (sitz == null) {
                return Antwort.NO_ACTIVE_SESSION;
            }
            antwort = Sitzung.createIt("benutzer", benutzer, "raum", raum, "endzeit", endzeit, "hasTag", hasTag).toJson(true);
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        antwort = includeBenutzer(antwort);
        return Response.ok(antwort, MediaType.APPLICATION_JSON).build();
    }

    public static Response putSitzung(String benutzer, String json) {
        connect();
        String antwort = null;
        try {
            Sitzung sitz = Sitzung.findFirst("benutzer = ?", benutzer);
            if (sitz == null) {
                return Antwort.NO_ACTIVE_SESSION;
            }
            int raum = Integer.parseInt(JsonHelper.getAttribute(json, "raum"));
            int endzeit = Integer.parseInt(JsonHelper.getAttribute(json, "endzeit"));
            int hasTag = Integer.parseInt(JsonHelper.getAttribute(json, "hasTag"));
            sitz.set("raum", raum).set("endzeit", endzeit).set("hasTag", hasTag).saveIt();
            antwort = sitz.toJson(true);
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        return Response.ok(antwort,MediaType.APPLICATION_JSON).build();
    }

    //TODO: Warum wird ein inhalt zurück gegeben Ich denke heir brauchen wir keine Antwort nur einen status!
    public static Response deleteSitzung(String benutzer) {
        connect();
        String antwort = null;
        try {
            Sitzung sitz = Sitzung.findFirst("benutzer = ?", benutzer);
            if (sitz == null) {
                return Antwort.NOT_FOUND;
            }

            sitz.delete();
            antwort = sitz.toJson(true);
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        return Response.ok(antwort,MediaType.APPLICATION_JSON).build(); // ??????
    }

    public static boolean istTagBesitzer(String userid,String raumid){
        connect();
        Long count = Sitzung.count("benutzer = ? and raum = ? and hasTag == 1", userid,raumid);

        disconnect();

        return (count != null && count >0);
    }
}
