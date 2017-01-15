package com.swe.gruppe4.freespace.Objektklassen;

import android.util.Log;

import java.io.Serializable;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * <p>Überschrift: Struktur von Tag</p>
 * <p>Beschreibung: Tag beschreibt die aktuelle Funktion eines Raumes. Er kann von Professoren gebucht sein
 * oder ein Student kann einen TAG für einen Raum setzten und so beschreiben wofür der Raum
 * genutzt wird. Z.B. Ruhe, Präsentation usw.
 * Enthält nur Attribute mit Gettern und Settern
 * </p>
 * <p>Organisation: FH Aachen, FB05, SWE Gruppe 4 </p>
 *
 * @author Merlin
 * @version 1.0
 *
 */

public class Tag implements Serializable {
    private int id;
    private String name;


    public Tag(int id, String name) {
        /**
         * Eindeutige ID des Tags
         */
        this.id = id;
        /**
         * Name des Tags für Ausgabe
         */
        this.name = name;
    }

    public Tag(String jsonTag) {
        try {
            JSONObject jsonObj = new JSONObject(jsonTag);

            this.id = jsonObj.getInt("id");
            this.name = jsonObj.getString("name");

        } catch (JSONException e) {
            //jsonTag enthält keinen Tag!
            Log.d("edu", "EXCEPTION in Tag!");
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
