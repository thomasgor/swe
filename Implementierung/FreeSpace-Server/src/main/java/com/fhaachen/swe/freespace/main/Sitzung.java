package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Sitzung")
public class Sitzung extends Datenbank {


    public static String getSitzungById(String id){
        connect();
        String json = Sitzung.findById(id).toJson(true);
        disconnect();
        return json;
    }

    public static boolean istTagBesitzer(String userid,String raumid){
        connect();
        Long count = Sitzung.count("benutzer = ? and raum = ? and hasTag == 1", userid,raumid);

        disconnect();

        return (count != null && count >0);
    }

}
