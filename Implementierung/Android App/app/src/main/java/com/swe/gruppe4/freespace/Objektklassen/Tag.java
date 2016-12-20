package com.swe.gruppe4.freespace.Objektklassen;

import java.io.Serializable;

/**
 * @author Merlin
 * @version 1.0
 * Tag beschreibt die aktuelle Funktion eines Raumes. Er kann von Professoren gebucht sein
 * oder ein Student kann einen TAG für einen Raum setzten und so beschreiben wofür der Raum
 * genutzt wird. Z.B. Ruhe, Präsentation usw.
 * Enthält nur Attribute mit Gettern und Settern
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
