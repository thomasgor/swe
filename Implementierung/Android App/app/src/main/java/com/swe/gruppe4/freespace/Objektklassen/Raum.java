package com.swe.gruppe4.freespace.Objektklassen;

import java.io.Serializable;

/**
 * @author Merlin
 * @version 1.0
 * Stellt einen Raum dar, der für Veranstaltung oder nur den Studenten genutzt werden kann.
 * Enthält nur Attribute mit Gettern und Settern
 */

public class Raum implements Serializable{
    /**
     * Eindeutige ID des Raumes
     */
    private int id;
    private String raumname;
    private int teilnehmer_max;
    private int teilnehmer_aktuell;

    /**
     * Auslastung des Raumes in Form einer Farbe. z.B. green, red
     */
    private String status;

    /**
     * URL vom Bild des Google Account
     */
    private String fotoURL;
    /**
     * Gesetzter Tag das Raums
     */
    private Tag tag;
    private Benutzer[] benutzer;

    public Raum(int id, String raumname, int teilnehmer_max, int teilnehmer_aktuell, String fotoURL, Tag tag, Benutzer[] benutzer, String status) {
        this.id = id;
        this.raumname = raumname;
        this.teilnehmer_max = teilnehmer_max;
        this.teilnehmer_aktuell = teilnehmer_aktuell;
        this.fotoURL = fotoURL;
        this.tag = tag;
        this.benutzer = benutzer;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRaumname() {
        return raumname;
    }

    public void setRaumname(String raumname) {
        this.raumname = raumname;
    }

    public int getTeilnehmer_max() {
        return teilnehmer_max;
    }

    public void setTeilnehmer_max(int teilnehmer_max) {
        this.teilnehmer_max = teilnehmer_max;
    }

    public int getTeilnehmer_aktuell() {
        return teilnehmer_aktuell;
    }

    public void setTeilnehmer_aktuell(int teilnehmer_aktuell) {
        this.teilnehmer_aktuell = teilnehmer_aktuell;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Benutzer[] getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(Benutzer[] benutzer) {
        this.benutzer = benutzer;
    }
}
