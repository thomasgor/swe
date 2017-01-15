package com.swe.gruppe4.freespace.Objektklassen;

import android.util.Log;

import java.io.Serializable;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * <p>Überschrift: Struktur von Freundschaft</p>
 * <p>Beschreibung: Diese Klasse dient dazu, Freundschaftsobjekte zu Verarbeiten
 * </p>
 * <p>Organisation: FH Aachen, FB05, SWE Gruppe 4 </p>
 *
 * @author Merlin
 * @version 1.0
 *
 */

public class Freundschaft implements Serializable{
    private Benutzer benutzer;
    private boolean status;
    private Raum raum;

    /**
     * Nur zur Erstellung von DummyObjekten gedacht
     *
     * @deprecated jsonBenutzer benutzen
     */
    public Freundschaft(Benutzer benutzer, boolean status, Raum raum) {
        this.benutzer = benutzer;
        this.status = status;
        this.raum = raum;
    }

    /**
     * Erstellt ein Benutzerobjekt aus einem JSON-String
     *
     * @param jsonFreundschaft JSON-String eines Benutzerobjekts*/
    public Freundschaft(String jsonFreundschaft) {
        try {
            JSONObject jsonObj = new JSONObject(jsonFreundschaft);

            this.benutzer = new Benutzer(jsonObj.getString("benutzer"),1);
            if(jsonObj.getInt("status") == 1){
                this.status = true;
            }
            else
            {
                this.status = false;
            }

            if(status) {
                this.raum = new Raum(jsonObj.getString("raum"),false);
            }
            else
            {
                this.raum = null;
            }

        } catch (JSONException e) {
            Log.d("edu","EXCEPTION in Freundschaft");
            //jsonTag enthält keinen Tag!
            e.printStackTrace();
        }
    }

    public Raum getRaum() {
        return raum;
    }

    public void setRaum(Raum raum) {
        this.raum = raum;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


}
