package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.JsonHelper;
import com.fhaachen.swe.freespace.Server;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.annotations.Table;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Raum")
public class Raum extends Datenbank {

    public static String getRaumByID(String id){
        connect();
        String result = "";
        Raum r = Raum.findById(id);
        if(r != null){
            Map m = JsonHelper.toMap(r.toJson(true));
            m = completeRaumDetails(m);
            result = JsonHelper.getJsonStringFromMap(m);
        }
        disconnect();
        return result;
    }
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
                    e.printStackTrace();
                }
            }

            System.out.println(raeumeMap.toString());
            result = JsonHelper.getJsonStringFromMap(raeumeMap);
        }

        disconnect();
        return result;
    }

    public static String includeBenutzerInRaumdetails(String json){
        Map input = JsonHelper.toMap(json);
        String raumID = input.get("id").toString();
        connect();
        LazyList<Sitzung> sitzungen = Sitzung.find("raum=?", raumID);
        ArrayList<Map> benutzer = new ArrayList<Map>();

        for(int i=0; i< sitzungen.size(); i++){
            String benutzerID = sitzungen.get(i).get("benutzer").toString();
            Benutzer b = Benutzer.findById(benutzerID);

            if(b != null && !"1".equals(b.get("istAnonym").toString())){
                b.set("istAnonym",0);
                b.set("token",null);
                String bJson = b.toJson(true);
                Map bMap = JsonHelper.toMap(bJson);
                benutzer.add(bMap);
            }
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
                result = includeBenutzerInRaumdetails(result);
                result = includeRaumFoto(result);
            } else{
                System.out.println("Raum wurde nicht gefunden");
            }


        }
        catch(Exception e){
            e.printStackTrace();
        }

        return result;//json;
    }

    public static String putRaumID(String raumID,String tagID, String benutzerID){
        connect();
        Raum raum = Raum.findById(raumID);
        disconnect();
        if(raum == null){
            //Raum nicht vorhanden
            return null;
        }
        connect();

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

        int teilnehmer_anz = Sitzung.getRaumteilnehmer_anz(raumID);
        if(teilnehmer_anz == 0)
            System.out.println("Teilnehmer_anz ist null: " + raumID);

        raumMap.put("teilnehmer_anz",teilnehmer_anz);

        //lade Status
        int teilnehmer_max = Integer.parseInt((raumMap.get("teilnehmer_max")).toString());

        double auslastung = 100;
        System.out.println("teilnehmer_max = " + teilnehmer_max);
        if(teilnehmer_max == 0){
            auslastung = 100;
        }else{
            auslastung = ((double) teilnehmer_anz / (double) teilnehmer_max) * 100;
        }

        boolean aktiveVeranstaltung = false;

        long unixTime = System.currentTimeMillis() / 1000L;
        int unixTime_Int = Integer.parseInt(String.valueOf(unixTime));

        try{
            aktiveVeranstaltung =!Veranstaltung.istRaumFrei(unixTime_Int,unixTime_Int,raumID);
        }catch(Exception e) {
            e.printStackTrace();
        }

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

        raumMap.put("foto",Server.URL + "raum/" + raumMap.get("id") +  "/foto");
        return raumMap;
    }


    public static Map includeTag(Map raumMap){
       // Map raumMap = JsonHelper.toMap(json);
        if(raumMap.get("tag") != null) {
            String tagID = raumMap.get("tag").toString();
            //LADE TAG nach
            if(!tagID.equals("")){
                String jsonTag = Tag.getTagById(tagID);
                if (jsonTag != null) {
                    Map tagMap = JsonHelper.toMap(jsonTag);
                    String res = raumMap.put("tag", tagMap).toString(); //jsonTag
                }
            }else{
                raumMap.put("tag", null);
            }
        }

        return raumMap;
    }


    public static String includeRaumFoto(String json){
        Map raum = JsonHelper.toMap(json);
        raum.put("foto",Server.URL + "raum/" + raum.get("id") +  "/foto");
        return JsonHelper.getJsonStringFromMap(raum);
    }


}

