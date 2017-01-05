package com.swe.gruppe4.freespace.Objektklassen;

import java.io.Serializable;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * Created by Merlin on 22.11.2016.
 *
 */

public class Freundschaft implements Serializable{
    private Benutzer benutzer;
    private boolean status;
    private Raum raum;

    public Freundschaft(Benutzer benutzer, boolean status, Raum raum) {
        this.benutzer = benutzer;
        this.status = status;
        this.raum = raum;
    }

    public Freundschaft(String jsonFreundschaft) {
        try {
            JSONObject jsonObj = new JSONObject(jsonFreundschaft);

            this.benutzer = new Benutzer(jsonObj.getString("benutzer"));
            this.status = jsonObj.getBoolean("status");
            this.raum = new Raum(jsonObj.getString("raum"));

        } catch (JSONException e) {
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
