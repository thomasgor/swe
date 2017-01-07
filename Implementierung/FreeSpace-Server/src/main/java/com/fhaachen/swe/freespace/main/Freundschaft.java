package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.JsonHelper;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.annotations.CompositePK;
import org.javalite.activejdbc.annotations.Table;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Klasse Freundschaft beinhaltet die Logik um die HTTP-Methoden GET, POST, PUT, DELETE des Restservices umzusetzen.
 * Die Logik wurde anhand des Systementwurfs umgesetzt.
 *
 * @author Simon Catley
 * @version 1.3
 */

@Table("Freundschaft")
@CompositePK({ "Benutzer", "Freund" })

public class Freundschaft extends Datenbank{

    /**
     * Überprüft das vorkommen aller Attribute, die nötig sind um in JavaLite einen Composite Key zu realisieren.
     * Wird benötigt, da in der Datenbank der Primary Key der Freundschaft-Tabelle als zusammengesetzer Primary Key
     * realisiert wurde
     */

    static {
        validatePresenceOf("Benutzer", "Freund").message("ein oder mehrere Primärschlüssel fehlen!!!");
    }

    /**
     * Der Übergabeparameter json beinhaltet eine Liste aller Freundschaften des aufrufenden Benutzers als String-Objekt
     * im Json-Format. Diese Methode ersetzt alle Benutzer- und Freunde-ID's in der Liste durch deren zugehörige
     * Benutzer-Objekte im Json-Format und maskiert deren Token- und istAnonym-Felder.
     *
     * @param json Liste aller Freundesschaften eines Benutzers als String-Objekt im Json-Format
     * @return Liste aller Freundesschaften eines Benutzers als String-Objekt im Json-Format mit eingefügten Benutzer-Objekten
     */

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
                        if(ben.get("istAnonym") == null){
                            ben.set("istAnonym", 0);
                        }
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
                        if(fre.get("istAnonym") == null){
                            fre.set("istAnonym", 0);
                        }
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

    /**
     * Der Übergabeparameter json beinhaltet ein einzelnes Freundschafts-Objekt aus der Datenbank im Json-Format. Diese
     * Methode ersetzt die Benutzer- und Freund-ID durch deren zugehörige Benutzer-Objekte im Json-Format und maskiert deren
     * Token- und istAnonym-Felder.
     *
     * @param json Freundschaftseintrag aus der Datenbank als String-Objekt im Json-Format
     * @return Freundschaftseintrag aus der Datenbank als String-Objekt im Json-Format mit eingefügten Benutzer-Objekten
     */

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
                    if(ben.get("istAnonym") == null){
                        ben.set("istAnonym", 0);
                    }
                    ben.set("token", null);
                    String jsonBenutzer = ben.toJson(true);
                    Map benutzer = JsonHelper.toMap(jsonBenutzer);
                    map.put("benutzer", benutzer);
                }
                if (fre != null) {
                    if(fre.get("istAnonym") == null){
                        fre.set("istAnonym", 0);
                    }
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

    /**
     * Erstellt die Freundesliste des aufrufenden Benutzers als String-Objekt im Json-Formatund und liefert bei Erfolg
     * ein Response-Objekt mit HTTP-Statuscode OK und dem String-Objekt zurück.
     * Falls keine Freundschaften bestehen ist das String-Objekt eine leere Liste im Json-Format.
     * Sollte nicht auf die Datenbank zugegriffen werden können, wird eine Response mit HTTP-Statuscode
     * Internal-Server-Error, aber ohne String-Objekt geliefert.
     *
     * @param benutzerID Die ID des aufrufenden Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und bei Erfolg auch mit Freundesliste des aufrufenden Benutzers
     * als String-Objekt im Json-Format. Das String-Objekt enthält anstatt ID's die Benutzer-Objekte im Json-Format.
     */

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

    /**
     * Stellt eine Freundschaftsanfrage dar unbd erstellt somit einen Eintrag in der Freundschaftstabelle der
     * Datenbank zwischen dem Benutzer mit der ID: benutzerID und dem Benutzer mit der Email, welche im json Objekt
     * enthalten sein soll.
     * Als Antwort wird ein Response-Objekt geliefert, mit zugehörigem HTTP-Statuscode und dem
     * erstellen Freundschaftseintrag(Status = 0) als String-Objekt im Json-Format.
     * Sollte das json Attibut leer sein, die Email falsch sein, ein Eintrag mit den beiden Benutzern bereits in der
     * Datenbank existieren, oder auf die Datenbank nicht zugegriffen werden können, wird das Response-Objekt nur mit
     * Fehlercode als HTTP-Statuscode zurückgegeben.
     *
     * @param benutzerID Benutzer der die Freundschaft eintragen möchte
     * @param json String-Objekt im Json-Format, das die Email des Freundes enthält
     * @return Response-Objekt mit HTTP-Statuscode und bei Erfolg auch mit erstellter Freundschaft als String-Objekt im
     * Json-Format. Das String-Objekt enthält anstatt ID's die Benutzer-Objekte im Json-Format.
     */

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
        //Ist ein Benutzer mit dieser Email in der DB?
        if(freund == null) {
            return Antwort.BAD_REQUEST;
        }
        String freundID = freund.getId().toString();
        //Freundschaft mit sich selbst?
        if(benutzerID.equals(freundID)) {
            return Antwort.BAD_REQUEST;
        }
        try {
            //Freundschaft besteht bereits?
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


    /**
     * Setzt den Status eines in der DB befindlichen Freundschafteintrages auf 1 und stellt somit die Bestätigung einer
     * Freundschaftsanfrage dar. Als Antwort wird ein Response-Objekt geliefert, mit zugehörigem HTTP-Statuscode und dem
     * geänderten Freundschaftseintrag(Status = 1) als String-Objekt im Json-Format.
     * Sollte kein Eintrag mit den beiden Benutzern in der Datenbank existieren, oder auf die Datenbank nicht
     * zugegriffen werden können, wird das Response-Objekt nur mit Fehlercode als HTTP-Statuscode zurückgegeben.
     *
     * @param benutzerID ID des Benutzers der die Freundschaft akzeptiert
     * @param freund ID des Benutzers der die Freundschaftsanfrage gestellt hat
     * @return Response-Objekt mit HTTP-Statuscode und bei Erfolg auch mit geänderter Freundschaft als String-Objekt im
     * Json-Format. Das String-Objekt enthält anstatt ID's die Benutzer-Objekte im Json-Format.
     */
    public static Response putFreundschaft(String benutzerID, String freund) {
        connect();
        String antwort = null;
        //prüfen ob Freundschaft bereits besteht
        try {
            //Reihenfolge von freund und benutzerID vertauscht, da freund die ID des Benutzers ist,der die Freundschaft
            // in die DB eingetragen hat und somit im Feld Benutzer steht
            Freundschaft freu = Freundschaft.findByCompositeKeys(freund, benutzerID);
            //Freundschaft existiert?
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

    /**
     * Löscht eine bestehende Freundschaft aus der Datenbank und stellt somit gelichzeitig auch eine Ablehnung einer
     * Freunschaftsanfrage dar. Als Antwort wird ein Response-Objekt geliefert, mit zugehörigem HTTP-Statuscode und dem
     * gelöschten Freundschaftseintrag als String-Objekt im Json-Format.
     * Sollte kein Eintrag mit den beiden Benutzern in der Datenbank existieren, oder auf die Datenbank nicht
     * zugegriffen werden können, wird das Response-Objekt nur mit Fehlercode als HTTP-Statuscode zurückgegeben.
     *
     * @param benutzerID ID des Benutzers, der die Freundschaft löschen möchte
     * @param freund ID des zweiten Benutzers der Teil dieser Freundschaft ist
     * @return Response-Objekt mit HTTP-Statuscode und bei Erfolg auch mit gelöschter Freundschaft als String-Objekt im Json-Format.
     */

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
