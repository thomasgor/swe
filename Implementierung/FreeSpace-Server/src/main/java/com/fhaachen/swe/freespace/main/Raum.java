package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.JsonHelper;
import org.codehaus.jackson.map.ObjectMapper;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
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
        String result = "[]";
        connect();
        LazyList<Raum> raeume = Raum.findAll();

        if (raeume != null) {
            Map[] raeumeMap = JsonHelper.toMaps(raeume.toJson(true));
            for( Map raumMap : raeumeMap){
                try{
                     raumMap = completeRaumDetails(raumMap);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }

            System.out.println(raeumeMap.toString());
            result = JsonHelper.getJsonStringFromMap(raeumeMap);
        }

        disconnect();
        return result;
    }

    //TODO: TO TEST, SOLLTE ABER SO GEHEN !!!!!!!!!!!
    public static String includeBenutzerInRaumdetails(String json){
        Map input = JsonHelper.toMap(json);
        String raumID = input.get("raum").toString();
        connect();
        LazyList<Sitzung> sitzungen = Sitzung.find("raum=?", raumID);
        Map[] benutzer = new Map[sitzungen.size()];

        for(int i=0; i< benutzer.length; i++){
            String benutzerID = sitzungen.get(i).get("benutzer").toString();
            Benutzer b = Benutzer.findById(benutzerID);
            String bJson = b.toJson(true);
            Map bMap = JsonHelper.toMap(bJson);
            benutzer[i] = bMap;
        }

        input.put("benutzer", benutzer);
        String outputJSON = JsonHelper.getJsonStringFromMap(input);

        disconnect();
        return outputJSON;
    }
    public static String getRaumdetails(String id){
        String result = null;

        try {
            connect();
            Raum raum = Raum.findById(id);
            disconnect();

            if(raum != null) {
                String json = raum.toJson(true);
                Map raumMap = JsonHelper.toMap(json);
                System.out.println("completeRaumDetails");
                raumMap = completeRaumDetails(raumMap);

                if(raumMap == null){
                    System.out.println("RaumMap ist null");
                }

                result = JsonHelper.getJsonStringFromMap(raumMap);

            } else{
                System.out.println("Raum wurde nicht gefunden");
            }
        }
        catch(Exception e){
                System.out.println(e.toString());
        }

        return result;//json;
    }

    public static String putRaumID(String raumID,String tagID){
        connect();
        Raum raum = Raum.findById(raumID);
        if(raum == null){
            //Raum nicht vorhanden
            return null;
        }

        raum.set("tag", tagID);
        try{
            raum.saveIt();
        }catch(Exception e){
            //TAG nicht vorhanden
            System.out.println(e.toString());
            return null;
        }
        disconnect();
        String json = raum.toJson(true);

        //lade Details nach..
        Map raumMap = JsonHelper.toMap(json);
        raumMap = completeRaumDetails(raumMap);

        json = JsonHelper.getJsonStringFromMap(raumMap);
        System.out.println(" Das ist mein JsonString: " + json);
        return json;
    }


    public static Map completeRaumDetails(Map raumMap){
        //Lade Tag nach
        raumMap = includeTag(raumMap);


        String raumID = raumMap.get("id").toString();
        //lade Teilnehmer_anz
        long teilnehmer_anz = Sitzung.getRaumteilnehmer_anz(raumID);
        if(teilnehmer_anz == 0)
        System.out.println("Teilnehmer_anz ist null: " + raumID) ;
        raumMap.put("teilnehmer_anz",teilnehmer_anz);

        //lade Status
        long teilnehmer_max = Long.parseLong((raumMap.get("teilnehmer_max")).toString());

        double auslastung = 100;
        System.out.println("teilnehmer_max = " + teilnehmer_max);
        if(teilnehmer_max == 0){
            auslastung = 100;
        }else{
            auslastung = (teilnehmer_anz / teilnehmer_max) * 100;
        }

        boolean aktiveVeranstaltung = false;

        //eigentlich long
        //TODO millisekungen oder sekunden?
        long unixTime = System.currentTimeMillis() / 1000L;
        int unixTime_Int = Integer.parseInt(String.valueOf(unixTime));

        //TODO muss von THomas noch gemacht werden -> die Methode verfügt noch nicht über connect /disconnect usw.
        try{
            aktiveVeranstaltung =Veranstaltung.istRaumFrei( unixTime_Int,unixTime_Int,raumID);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        //


        if(aktiveVeranstaltung){
            raumMap.put("status","grau");
        }else{

            if(auslastung >= 80)
                raumMap.put("status","rot");
            else if(auslastung >= 40){
                raumMap.put("status","gelb");
            } else{
                raumMap.put("status","grün");
            }
        }

        return raumMap;
    }


    public static Map includeTag(Map raumMap){
       // Map raumMap = JsonHelper.toMap(json);
        if(raumMap.get("tag") != null) {
            String tagID = raumMap.get("tag").toString();
            //LADE TAG nach
            String jsonTag = Tag.getTagById(tagID);
            if (jsonTag != null) {
                Map tagMap = JsonHelper.toMap(jsonTag);
                String res = raumMap.put("tag", tagMap).toString(); //jsonTag

                //kein Fehler
                if (res == null) {
                    //tag wurde nachgeladen
                    System.out.print(raumMap.toString());
                }
            }
            //TAG ist nachgeladen
        }
        return raumMap;
    }



}

