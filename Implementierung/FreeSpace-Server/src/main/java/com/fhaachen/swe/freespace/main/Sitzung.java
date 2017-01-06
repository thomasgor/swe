package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.JsonHelper;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Klasse Sitzung beinhaltet die Logik um die HTTP-Methoden GET, POST, PUT, DELETE des Restservices umzusetzen.
 * Die Logik wurde anhand des Systementwurfs umgesetzt.
 *
 * @author Simon Catley
 * @version 1.5
 */
@Table("Sitzung")
@IdName("benutzer")
public class Sitzung extends Datenbank {

    /**
     * Gibt die Anzahl der in einen Raum eingeloggten Personen des Raumes mit der ID raumID als Integer zurück.
     * Falls nicht auf die Datenbank zugegriffen werden kann, wird der Maximalwert von Integer zurückgegeben.
     *
     * @param raumID Die ID des Raumes, dessen Teilnehmer Anzahl gesucht ist
     * @return Anzahl Teilnehmer des angegebenen Raum
     */

    public static int getRaumteilnehmer_anz(String raumID){
        System.out.print("getRaumteilnehmer_anz: " + raumID + " ");
        connect();
        int result = Integer.MAX_VALUE;
        try {
            Long teilnehmer_anz = Sitzung.count("raum=?", raumID);
            if(teilnehmer_anz != null) {
                result = Integer.parseInt(teilnehmer_anz.toString());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        disconnect();
        System.out.print(result);
        return result;
    }

    /**
     * Der Übergabeparameter json beinhaltet ein einzelnes Sitzungs-Objekt aus der Datenbank im Json-Format. Diese
     * Methode ersetzt die Benutzer-ID durch deren zugehöriges Benutzer-Objekt im Json-Format und maskiert deren
     * Token- und istAnonym-Felder.
     *
     * @param json Freundschaftseintrag aus der Datenbank als String-Objekt im Json-Format
     * @return Freundschaftseintrag aus der Datenbank als String-Objekt im Json-Format mit eingefügten Benutzer-Objekten
     */


    private static String includeBenutzer(String json) {
        System.out.println("Include Benutzer");
        connect();
        Map map = JsonHelper.toMap(json);
        try {
            Benutzer ben = Benutzer.findById(map.get("benutzer"));
            if (ben != null) {
                ben.set("istAnonym", null);
                ben.set("token", null);
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

    /**
     * Die Methode sucht einen Sitzungseintrag in der Datenbank des Benutzers mit der ID, die im Übergabeparameter steht.
     * Es wird ein Response-Objekt mit HTTP-Statuscode zurückgegeben. Falls kein Eintrag mit dieser Benutzer-ID existiert,
     * wird der HTTP-Statuscode 900 zurückgegeben(Eigendefinition als NO ACTIVE SESSION), falls eine Sitzung existiert,
     * diese aber abgelaufen ist, wird auch der Statuscode 900 zurückgegeben und zusätzlich der Eintrag in der Datenbank
     * gelöscht und falls nicht auf die Datenbank zugegriffen werden kann, der HTTP-Statuscode für Internal Server Error.
     * Bei Erfolg wird mit dem Statuscode OK auch die gesuchte Sitzung als String im Json-Format. Im String wird die ID
     * des Benutzers durch das Benutzer-Objekt ersetzt.
     *
     * @param id ID des Benutzers dessen Sitzung gesucht ist
     * @return Sitzung des Benutzers als String im Json-Format
     */

    public static Response getSitzungById(String id){
        connect();
        String antwort = null;
        try {
            Sitzung sitz = Sitzung.findFirst("benutzer = ?", Integer.parseInt(id));
            if (sitz == null) {
                return Antwort.NO_ACTIVE_SESSION;
            }
            boolean outOfTime = Long.parseLong(sitz.get("endzeit").toString()) <= System.currentTimeMillis() / 1000L;
            if(outOfTime) {
                sitz.delete();
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

    /**
     * Im Übergabeparameter json wird eine Sitzung als String im Jsom-Format übergeben. Diese Methode ersetzt
     * die ID des Raumes dieses Strings mit dem Eintrag des zugehörigen Raumes aus der Datenbank.
     * Falls nicht auf die Datenbank zugegriffen werden kann, oder der Raum mit der übergebenen ID nicht existiert,
     * wird der Original-String zurückgegriffen.
     *
     * @param json Sitzung als String im Json-Format
     * @return Eingegangener String mit der Raum-ID ersetzt durch deren zugehöriges Raum-Objekt
     */

    public static String includeRaumInSitzung(String json){
        Map input = JsonHelper.toMap(json);
        String raumID = input.get("raum").toString();
        String raum = null;
        try {
            raum = Raum.getRaumByID(raumID);
        } catch(Exception e) {
            e.printStackTrace();
            return json;
        }
        if(raum != null) {
            input.put("raum", JsonHelper.toMap(raum));
        } else {
            return json;
        }
        return JsonHelper.getJsonStringFromMap(input);
    }

    /**
     * Erstellt eine neue Sitzung für den Benutzer mit der ID benutzerID. Sollte bereits eine Sitzung für den Benutzer
     * bestehen, wird diese gelöscht und eine neue erstellt. Im Übergabeparameter json steht die RaumID, des Raumes in
     * den sich der Benutzer einloggen will, im Json-Format. Die Endzeit für die neue Sitzung wird aus der jetzigen
     * Systemzeit des Servers + das auf dem Server abgelegten Sitzungsintervall berechnet.
     * Es wird ein Response-Objekt mit HTTP-Statuscode zurückgeliefert.
     * Statuscode Internal Server Error, falls nicht auf die Datenbank zugegeriffen werden kann und Statuscode OK,
     * zusammen mit der neu erstellten Sitzung als String im Json-Format und der RaumID im Sitzungsstring ersetzt durch
     * das zugehörigen Raum-Objekt.
     *
     * @param json Enthält die RaumID für die die Sitzung erstellt werden soll im Json-Format
     * @param benutzerID Die ID des Benutzers für den Die Sitzung erstellt werden soll
     * @return Response-Objekt mit HTTP-Statuscode und bei erfolgreichem kreieren, das neu erstellte Sitzungs-Objekt als
     * String im Json-Format
     */

    public static Response postSitzung(String json, String benutzerID) {
        connect();
        String antwort = null;
        Map input = JsonHelper.toMap(json);
        String raum = input.get("raum").toString();

        try {
            Sitzung sitz = Sitzung.findById(benutzerID);
            if (sitz != null) {
                System.out.println("Benutzer hat bereits eine aktive Sitzung:" + benutzerID);
                deleteSitzung(benutzerID);
            }

            connect();
            long endzeit = ((Long) System.currentTimeMillis() / 1000L) + (Integer.parseInt(Konfiguration.getSitzungsintervall()) * 60);
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

    /**
     * Falls ein Benutzer keine aktive Sitzung hat, wird diese Methode aufgerufen, eine Liste aller Räume erstellt
     * und als String im Json-Format zurückgegeben. Sollten keine Räume auf dem Server vorhanden sein wird ein String im
     * Json-Format mit dem Attribut räume und dem Wert null zurückgegeben.
     *
     * @return Liste aller Räume als String im Json-Format
     */

    public static String getNoActiceSession() {
        Map homescreen = JsonHelper.toMap("{\"räume\": null}");
        try{
            String raumliste = Raum.getRaum();
            System.out.println(raumliste);
            homescreen.put("räume", JsonHelper.toMaps(raumliste));
        }catch(Exception e){
            e.printStackTrace();
        }
        return JsonHelper.getJsonStringFromMap(homescreen);
    }

    /**
     * Diese Methode ermöglicht die Verlängerung einer bestehenden Sitzung und liefert ein Response-Objekt mit
     * HTTP-Statuscode zurück. Bei erfolgreicher Änderung wird ausserdem die veränderte Sitzung als String im Json-Format
     * mitgegeben. Im Antwortstring wird die RaumID durch das zugehörige Raum-Objekt als String ersetzt.
     * Sollte keine Sitzung für den Benutzer mit der ID benutzer eingetragen sein wird als
     * Statuscode 900(Eigendefinition als NO ACTIVE SESSION) zurückgegeben. Solte nicht auf die Datenbank zugegriffen
     * werden können, wird Statuscode Internal Server Error zurückgegeben.
     *
     * @param benutzer ID des Benutzers dessen Sitzung geändert werden soll
     * @return geänderte Sitzung als String im Json-Format mit RaumID ersetzt durch das zugehörige Raum-Objekt als String
     */


    public static Response putSitzung(String benutzer) {
        connect();
        String antwort = null;
        try {
            Sitzung sitz = Sitzung.findFirst("benutzer = ?", benutzer);
            if (sitz == null) {
                return Antwort.NO_ACTIVE_SESSION;
            }

            long endzeit = ((Long) System.currentTimeMillis() / 1000L) + (Integer.parseInt(Konfiguration.getSitzungsintervall()) * 60);

            sitz.set("endzeit", endzeit);
            sitz.set("notifySent", 0);
            sitz.saveIt();

            antwort = sitz.toJson(true);
            antwort = includeRaumInSitzung(antwort);
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        return Response.ok(antwort,MediaType.APPLICATION_JSON).build();
    }

    /**
     * Die Methode löscht die Sitzung des Benutzers mit der ID benutzer aus der Datenbank. Als Antwort wird ein
     * Response-Objekt mit HTTP-Statucode zurückgegeben. Falls keine Sitzung für den benutzer mit der Id benutzer
     * existiert, oder die ID nicht existiert wird Statuscode 900(Eigendefinition als NO ACTIVE SESSION) zurückgegeben,
     * falls auf die Datenbank nicht zugegriffen werden kann, Statuscode Internal Server Error und bei erfolgreichem
     * Löschen, Statuscode OK.
     *
     * @param benutzer ID des Benutzers, dessen Sitzung gelöscht werden soll
     * @return Response-Objekt mit HTTP-Statuscode
     */

    public static Response deleteSitzung(String benutzer) {
        connect();
        String antwort = null;
        try {
            Sitzung sitz = Sitzung.findFirst("benutzer = ?", benutzer);
            if (sitz == null) {
                return Antwort.NO_ACTIVE_SESSION;
            }
            if(sitz.get("hasTag").toString().equals("1")) {
                Raum.putRaumID(sitz.get("raum").toString(), null, benutzer);
            }
            sitz.delete();
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        return Response.ok().build();
    }

    /**
     * Die Methode überprüft, ob der Benutzer mit der ID userid der Ersteller und somit Besitzer des Tags des Raumes
     * mit der ID raumid ist.
     *
     * @param userid ID des Benutzers der überprüft wird
     * @param raumid ID des Raumes in dem der Benutzer sich befindet
     * @return true falls der Benutzer der Ersteller des Tags für den Raum ist, ansonsten false
     */

    public static boolean istTagBesitzer(String userid,String raumid){
        connect();
        Long count = Sitzung.count("benutzer = ? and raum = ? and hasTag == 1", userid,raumid);

        disconnect();

        return (count != null && count >0);
    }

    /**
     * Die Methode überprüft, ob für den Raum mit der ID raumID ein Benutzer existiert, der als Besitzer des Tags
     * markiert ist(hasTag == 1)
     *
     * @param raumID ID des Raumes für den geprüft wird
     * @return true falls ein Benutzer mit hasTag == 1 existiert, ansonsten false
     */

    public static boolean raumHasTag(String raumID){
        connect();
        long anz = Sitzung.count("raum = ? and hasTag = 1", raumID);
        disconnect();
        if(anz >= 1)
            return true;
        return false;
    }

    /**
     * Setzt die Flag hasTag auf der Datenbank auf 1, für den Benutzer mit der ID benutzerID
     *
     * @param benutzerID ID des Benutzers, für den die Flag hasTag gesetzt werden soll
     * @return true, falls die Flag hasTag für den Benutzer erfolgreich auf 1 gesetzt wurde, ansonsten false
     */

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

    /**
     * Überprüft ob der Benutzer mit der ID benutzerID im Raum mit der ID raumID eine aktive Sitzung hat.
     *
     * @param raumID ID des Raumes, der geprüft werden soll
     * @param benutzerID ID des Benutzers, der geprüft werden soll
     * @return true, falls der Benutzer eine auf der Datenbank eingetragene Sitzung für den angegebenen Raum hat,
     * ansonsten false
     */

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
