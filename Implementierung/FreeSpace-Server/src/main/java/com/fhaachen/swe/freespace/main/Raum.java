package com.fhaachen.swe.freespace.main;

import org.codehaus.jackson.map.ObjectMapper;
import org.javalite.activejdbc.annotations.Table;

/**
 * Created by thomas on 27.11.2016.
 * Edited by simon on 04.12.2016.
 */
@Table("Raum")
public class Raum extends Datenbank {

    public static String getRaumListe(){
        connect();
        String json = Raum.findAll().toJson(true);
        disconnect();
        return json;
    }

    public static String getRaumByID(int id){
        connect();
        String json = Raum.findById(id).toJson(true);
        disconnect();
        return json;
    }

    public static String putRaum(int raumID, int tagID){
        connect();
        Raum raum = Raum.findById(raumID);
        raum.set("tag", tagID).saveIt();
        String json = raum.toJson(true);
        disconnect();
        return json;
    }


    public void MapFromJSON(String json){
        ObjectMapper mapper = new ObjectMapper();

    }








}

