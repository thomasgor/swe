package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.annotations.Table;

/**
 * Created by thomas on 27.11.2016.
 * Edited by simon on 04.12.2016.
 */
@Table("Sitzung")
public class Sitzung extends Datenbank {

    public static String getSitzungsListe() {
        connect();
        String json = Tag.findAll().toJson(true);
        disconnect();
        return json;
    }

    public static String postSitzung(int benutzerID, int raumID, int endzeit) {
        connect();
        String json = Sitzung.createIt("benutzer", benutzerID, "raum", raumID, "endzeit", endzeit).toJson(true);
        disconnect();
        return json;
    }

    public static String putSitzung(int benutzerID, int endzeit) {
        connect();
        Sitzung sitz = Sitzung.findById(benutzerID);
        sitz.set("endzeit", endzeit).saveIt();
        String json = sitz.toJson(true);
        disconnect();
        return json;
    }

    public static String deleteSitzung(int id) {
        connect();
        Sitzung sitz = Sitzung.findFirst("benutzer", id);
        sitz.delete();
        String json = sitz.toJson(true);
        disconnect();
        return json;
    }
}
