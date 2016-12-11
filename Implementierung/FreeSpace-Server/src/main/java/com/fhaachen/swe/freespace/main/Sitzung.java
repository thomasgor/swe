package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Sitzung")
public class Sitzung extends Datenbank {


    public static String getSitzungById(String id){
        String result = null;
        connect();
        Sitzung sitzung = Sitzung.findById(id);

        if(sitzung == null){
            return result;
        }

        result = sitzung.toJson(true);
        disconnect();
        return result;
    }

    public static boolean istTagBesitzer(String userID,String raumID){
        connect();
        Long count = Sitzung.count("benutzer = ? and raum = ? and hasTag == 1", userID,raumID);
        disconnect();

        return (count != null && count >0);
    }

    public static long getRaumteilnehmer_anz(String raumID){
        connect();
        Long teilnehmer_anz = Sitzung.count("raum = ?", raumID);
        disconnect();
        return teilnehmer_anz;
    }

}
