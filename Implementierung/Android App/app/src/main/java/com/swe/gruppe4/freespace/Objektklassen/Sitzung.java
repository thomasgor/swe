package com.swe.gruppe4.freespace.Objektklassen;

import android.util.Log;

import java.io.Serializable;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * <p>Überschrift: Struktur von Sitzung</p>
 * <p>Beschreibung: Diese Klasse dient dazu, Sitzungsobjekte zu verarbeiten
 * </p>
 * <p>Organisation: FH Aachen, FB05, SWE Gruppe 4 </p>
 *
 * @author Merlin
 * @version 1.0
 *
 */

public class Sitzung implements Serializable{
    private String id;
    private Raum raum;
    private boolean myTag;
    private long endzeit;

    /**
     * Nur zur Erstellung von Dummyobjekten
     * @deprecated Sitzung(jsonSitzung) benutzen
     */
    public Sitzung(String id, Raum raum, boolean myTag, long endzeit) {
        this.id = id;
        this.raum = raum;
        this.myTag = myTag;
        this.endzeit = endzeit;
    }

    /**
     * Nur zur Erstellung von Dummyobjekten
     * @deprecated Sitzung(jsonSitzung) benutzen
     */
    public Sitzung(int id, Raum raum, boolean myTag, long endzeit) {
        this.id = String.valueOf(id);
        this.raum = raum;
        this.myTag = myTag;
        this.endzeit = endzeit;
    }

    /**
     * Erstellt ein Sitzungsobjekt aus einem JSONString
     *
     * @param jsonSitzung JSON-String des Sitzungsobjekts
     */
    public Sitzung(String jsonSitzung) {
        try {
            JSONObject jsonObj = new JSONObject(jsonSitzung);

            this.id = jsonObj.getString("benutzer");
            this.myTag = (jsonObj.getInt("hastag") != 0);
            this.endzeit = jsonObj.getLong("endzeit");
            this.raum = new Raum(jsonObj.getString("raum"),true);




        } catch (JSONException e) {
            //jsonSitzung enthält keine Sitzung!
            Log.d("edu", "EXCEPTION in Sitzung!");
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Raum getRaum() {
        return raum;
    }

    public void setRaum(Raum raum) {
        this.raum = raum;
    }

    public boolean isMyTag() {
        return myTag;
    }

    public void setMyTag(boolean myTag) {
        this.myTag = myTag;
    }

    public long getEndzeit() {
        return endzeit;
    }

    public void setEndzeit(long endzeit) {
        this.endzeit = endzeit;
    }
}
