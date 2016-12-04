package com.swe.gruppe4.mockup2.Objektklassen;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Merlin on 22.11.2016.
 */

public class Raum implements Serializable{
    private int id;
    private String raumname;
    private int teilnehmer_max;
    private int teilnehmer_aktuell;
    private enum status{ROT, GELB, GRÜN, GRAU}
    private String fotoURL;
    private Tag tag;
    private Benutzer[] benutzer;

    public Raum(int id, String raumname, int teilnehmer_max, int teilnehmer_aktuell, String fotoURL, Tag tag, Benutzer[] benutzer) {
        this.id = id;
        this.raumname = raumname;
        this.teilnehmer_max = teilnehmer_max;
        this.teilnehmer_aktuell = teilnehmer_aktuell;
        this.fotoURL = fotoURL;
        this.tag = tag;
        this.benutzer = benutzer;
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

    public Benutzer[] getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(Benutzer[] benutzer) {
        this.benutzer = benutzer;
    }
}
