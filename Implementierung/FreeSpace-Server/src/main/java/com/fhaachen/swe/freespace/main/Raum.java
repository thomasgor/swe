package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.JsonHelper;
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

    public static String getRaumdetails(String id){
        connect();
        try {
            Raum r = Raum.findById(id);

            if(r == null) {
                System.out.println("Der Raum ist null");
                return null;
            }

            return r.toJson(true);
        }
        catch(Exception e){
                System.out.println(e.toString());
        }

        disconnect();
        return null;//json;
    }

    public static String putRaumID(String raumID,int tagID){
        connect();
        Raum r = Raum.findById(raumID);
        if(r == null){
            //Raum nicht vorhanden
            return null;
        }

        r.set("tag", tagID);
        try{
            r.saveIt();
        }catch(Exception e){
            System.out.println(e.toString());
            return null;
        }


        String json = r.toJson(true);
        //LADE TAG nach
        if( r == null){
            System.out.println("r ist null");
        }
        disconnect();

        String jsonTag = Tag.getTagById(r.getId().toString());
        Map tagMap = JsonHelper.toMap(jsonTag);

        Map raumMap = JsonHelper.toMap(json);


        String res = raumMap.put("tag",tagMap).toString(); //jsonTag

        //kein Fehler
        if(res == null){
            //tag wurde nachgeladen
            System.out.print(raumMap.toString());
        }

        json = JsonHelper.getJsonStringFromMap(raumMap);


        System.out.println(" Das ist mein JsonString: " + json);
        return json;
    }

    public void MapFromJSON(String json){
    }








}

