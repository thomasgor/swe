package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.JsonHelper;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Sitzung")
@IdName("benutzer")
public class Sitzung extends Datenbank {

    public static int getRaumteilnehmer_anz(String raumID){
        System.out.print("getRaumteilnehmer_anz: " + raumID + " ");
        connect();
        Long teilnehmer_anz = Sitzung.count("raum=?", raumID);
        int result = Integer.parseInt(teilnehmer_anz.toString());
        disconnect();
        System.out.print(result);
        return result;
    }

    private static String includeBenutzer(String json) {
        System.out.println("Include Benutzer");
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

    //TODO: Nachteil: Wenn man eine Response returned dann kann man die methode von nirgendwo anders benutzen
    //TODO: Ich finde es besser, wenn man hier einen String zurück gibt oder so, lass einfach mla drüber reden!!
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
        antwort = includeRaumInSitzung(antwort);
        return Response.ok(antwort, MediaType.APPLICATION_JSON).build();
    }
    public static String includeRaumInSitzung(String json){
        Map input = JsonHelper.toMap(json);
        String raumID = input.get("raum").toString();
        String raum = Raum.getRaumByID(raumID);
        input.put("raum", JsonHelper.toMap(raum));

        return JsonHelper.getJsonStringFromMap(input);
    }

    public static Response postSitzung(String json, String benutzerID) {
        connect();
        String antwort = null;
        Map input = JsonHelper.toMap(json);
        String raum = input.get("raum").toString();
        long endzeit = ((Long) System.currentTimeMillis() / 1000L) + (45 * 60);

        try {
            Sitzung sitz = Sitzung.findById(benutzerID);
            if (sitz != null) {
                System.out.println("Benutzer hat bereits eine aktive Sitzung:" + benutzerID);
                deleteSitzung(benutzerID);
            }

            connect();
            Sitzung s  = new Sitzung();
            s.set("benutzer", benutzerID);
            s.set("endzeit", endzeit);
            s.set("raum", raum);
            s.set("hasTag", 0);
            boolean erfolg = s.insert();

            disconnect();
            System.out.println("Neue sitzung wurde erstellt");
            antwort = s.toJson(true);
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }

        disconnect();
        antwort = includeRaumInSitzung(antwort);
        return Response.ok(antwort, MediaType.APPLICATION_JSON).build();
    }

    public static String getNoActiceSession() {
        try{
        Map homescreen = JsonHelper.toMap("{\"räume\": null}");
        String raumliste = Raum.getRaum();
        System.out.println(raumliste);
        homescreen.put("räume", JsonHelper.toMaps(raumliste));
        homescreen.put("karte", "SIMON MUSS DAS REGELN!");
        return JsonHelper.getJsonStringFromMap(homescreen);

        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static Response putSitzung(String benutzer, String json) {
        connect();
        String antwort = null;
        try {
            Sitzung sitz = Sitzung.findFirst("benutzer = ?", benutzer);
            if (sitz == null) {
                return Antwort.NO_ACTIVE_SESSION;
            }

            long endzeit = ((Long) System.currentTimeMillis() / 1000L) + (45 * 60);
            int hasTag = Integer.parseInt(JsonHelper.getAttribute(json, "hasTag"));

            sitz.set("endzeit", endzeit).set("hasTag", hasTag).saveIt();
            antwort = sitz.toJson(true);
            antwort = includeRaumInSitzung(antwort);
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        return Response.ok(antwort,MediaType.APPLICATION_JSON).build();
    }

    public static Response deleteSitzung(String benutzer) {
        connect();
        String antwort = null;
        try {
            Sitzung sitz = Sitzung.findFirst("benutzer = ?", benutzer);
            if (sitz == null) {
                return Antwort.NO_ACTIVE_SESSION;
            }
            sitz.delete();
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        return Response.ok().build();
    }

    public static boolean istTagBesitzer(String userid,String raumid){
        connect();
        Long count = Sitzung.count("benutzer = ? and raum = ? and hasTag == 1", userid,raumid);

        disconnect();

        return (count != null && count >0);
    }

    public static boolean raumHasTag(String raumID){
        connect();
        long anz = Sitzung.count("raum = ? and hasTag = 1", raumID);
        disconnect();
        if(anz >= 1)
            return true;
        return false;
    }

    public static boolean setHasTag(String benutzerID){
        connect();
        try {
            Sitzung sitz = Sitzung.findFirst("benutzer = ?", benutzerID);
            sitz.set("hasTag",1);
            sitz.saveIt();
        }catch(Exception e){
            disconnect();
            return false;
        }

        disconnect();
        return true;
    }

    public static boolean hasSessionInRoom(String raumID, String benutzerID) {
        connect();
        try {
            Sitzung sitz = findFirst("benutzer = ? and raum = ?", benutzerID, raumID);
            if(sitz != null) {
                disconnect();
                return true;
            }
        } catch(Exception e) {
            disconnect();
            return false;
        }
        disconnect();
        return false;
    }

}
