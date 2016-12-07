package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.CompositePK;
import org.javalite.activejdbc.annotations.Table;

/**
 * Created by thomas on 27.11.2016.
 */

@Table("Freundschaft")
@CompositePK({"Benutzer", "Freund"})
public class Freundschaft extends Datenbank {

    public static String getFreundschaftsListe() {
        connect();
        String json = Freundschaft.findAll().toJson(true);
        disconnect();
        return json;
    }

    public static String postFreundschaft(String benutzer, String freund, int status) {
        connect();
        String json = Freundschaft.createIt("Benutzer", benutzer, "Freund", freund, "Status", status).toJson(true);
        disconnect();
        return json;
    }

    public static String putFreundschaft(String benutzer, String freund, int status) {
        connect();
        Freundschaft freu = Freundschaft.findByCompositeKeys(benutzer, freund);
        freu.set("Status", status).saveIt();
        String json = freu.toJson(true);
        disconnect();
        return json;
    }

    public static String deleteFreundschaft(String benutzer, String freund) {
        connect();
        Freundschaft freu = Freundschaft.findByCompositeKeys(benutzer, freund);
        freu.delete();
        String json = freu.toJson(true);
        disconnect();
        return json;
    }
}
