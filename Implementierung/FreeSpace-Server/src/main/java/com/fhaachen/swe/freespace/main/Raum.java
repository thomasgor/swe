package com.fhaachen.swe.freespace.main;

import org.codehaus.jackson.map.ObjectMapper;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Raum")
public class Raum extends Datenbank {

    public static String getRaum(){
        connect();
        String json = Raum.findAll().toJson(true);
        disconnect();
        return json;
    }

    public static String getRaumdetails(int id){
        connect();
        String json = Raum.findById(id).toJson(true);
        System.out.println(json);
        disconnect();
        return json;
    }

    public static String putRaumID(int raumID,int tagID){
        connect();
        Raum r = Raum.findById(raumID);
        r.set("tag", tagID);
        r.saveIt();

        String json = r.toJson(true);

        System.out.println("JSON: " + json);
        disconnect();
        return json;
    }

    public void MapFromJSON(String json){
        ObjectMapper mapper = new ObjectMapper();

    }








}

