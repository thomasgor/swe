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

    public static long getRaumteilnehmer_anz(String raumID){
        connect();
        Long teilnehmer_anz = Sitzung.count("raum = ?", raumID);
        disconnect();
        return teilnehmer_anz;
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

    //TODO: in der Sitzung sollte eigentlich kein benutzer objekt stehen, der benutzer kennt sich
    //TODO: der raum sollte nachgeladen werden
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
        antwort = includeBenutzer(antwort);
        return Response.ok(antwort, MediaType.APPLICATION_JSON).build();
    }

    public static Response postSitzung(String json, String benutzerID) {
        connect();
        String antwort = null;
        Map input = JsonHelper.toMap(json);
        String raum = input.get("raum").toString();
        long endzeit = ((Long) System.currentTimeMillis() / 1000L) + (45 * 60);

        try {
            Sitzung sitz = Sitzung.findFirst("benutzer = ?", benutzerID);
            //Nuetzer hat bereits eine akive Sitzung, was machen wir in einem Solchen Fall???
            if (sitz != null) {
                return Antwort.NOT_IMPLEMENTED;
            }

            Sitzung s  = new Sitzung();
            s.set("benutzer", benutzerID);
            s.set("endzeit", endzeit);
            s.set("raum", raum);
            s.set("hasTag", 0);
            s.saveIt();
            antwort = s.toJson(true);
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
            //TODO: Sollen wir wirklich erlauben, das der Raum geändert werden kann????
            //TODO: Eigentlich sollte in diesem fall eine neue Sitzung angelegt werden
            //TODO: Thomas sagt, hier soll nur hastag und endzeit gesetzt werden
            int raum = Integer.parseInt(JsonHelper.getAttribute(json, "raum"));
            long endzeit = ((Long) System.currentTimeMillis() / 1000L) + (45 * 60);
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

    //TODO: SIMON, das klappt jetzt !
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
}
