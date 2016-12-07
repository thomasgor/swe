package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.annotations.Table;

/**
 * Created by thomas on 27.11.2016.
 * Edited by simon on 04.12.2016.
 */
@Table("Veranstaltung")
public class Veranstaltung extends Datenbank {

    public static String getVeranstaltungsListe() {
        connect();
        String json = Veranstaltung.findAll().toJson(true);
        disconnect();
        return json;
    }

    public static String postVeranstaltung(String benutzer, int von, int bis, int datum, int raumID) {
        connect();
        String json = Veranstaltung.createIt("benutzer", benutzer, "von", von, "bis", bis, "datum", datum, "raumID", raumID).toJson(true);
        disconnect();
        return json;
    }

    public static String getVeranstaltungByID(int veranstaltungID) {
        connect();
        String json = Veranstaltung.findById(veranstaltungID).toJson(true);
        disconnect();
        return json;
    }

    public static String putVeranstaltung(int veranstaltungID, int von, int bis, int datum, int raumID) {
        connect();
        Veranstaltung ver = Veranstaltung.findById(veranstaltungID);
        ver.set("von", von).set("bis", bis).set("datum", datum).set("raum", raumID).saveIt();
        String json = ver.toJson(true);
        disconnect();
        return json;
    }

    public static String deleteVeranstaltung(int veranstaltungID) {
        connect();
        Veranstaltung veranstaltung = Veranstaltung.findFirst("id = ?", veranstaltungID);
        veranstaltung.deleteCascade();
        String json = veranstaltung.toJson(true);
        disconnect();
        return json;
    }
}
