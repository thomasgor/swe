package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.JsonHelper;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Veranstaltung")
public class Veranstaltung extends Datenbank {

    public static String getVeranstaltungsListe(String professorID) {
        String result = "[]";
        connect();

        LazyList veranstaltungen = Veranstaltung.find("benutzer=?", professorID);

        //Es wurden Veranstaltungen gefunden
        if(veranstaltungen != null){
            result = veranstaltungen.toJson(true);
            result = includeRaumInListe(result);
        }

        disconnect();
        return result;
    }

    public static String getVeranstaltungByID(String id){
        String result = null;
        connect();

        Veranstaltung v = Veranstaltung.findById(id);
        if(v != null){
            result = v.toJson(true);
            result = includeRaum(result);
        }

        disconnect();
        return result;
    }


    public static boolean istRaumFrei(int von, int bis, String raumid){
        /*
        * Ein Raum ist blockiert, wenn von oder bis inner halb des Zeitraums einer anderen veranstaltung liegen!
        */
        LazyList<Veranstaltung> veranstaltungen = Veranstaltung.find("raum=?", raumid);

        if(veranstaltungen == null){
            return true;
        }

        for(Veranstaltung v : veranstaltungen){
            int von1 = Integer.parseInt(v.get("von").toString());
            int bis1 = Integer.parseInt(v.get("bis").toString());

            if(von >= von1 && von <= bis1){
                return false;
            }

            if(bis >= von1 && bis <=bis1){
                return false;
            }
        }
        return true;
    }

    public static String postVeranstaltung(String json, String professorID){
        String result = null;
        connect();

        Veranstaltung v = new Veranstaltung();
        Map input = JsonHelper.toMap(json);
        int von = Integer.parseInt(input.get("von").toString());
        int bis = Integer.parseInt(input.get("bis").toString());
        String raum = input.get("raum").toString();

        if(Veranstaltung.istRaumFrei(von,bis,raum)){
            v.set("benutzer", Integer.parseInt(professorID));
            v.set("raum", input.get("raum"));
            v.set("name", input.get("name"));
            v.set("bis", input.get("bis"));
            v.set("von", input.get("von"));

            try{
                v.saveIt();
                result = v.toJson(true);
            } catch (Exception e){
                System.out.println(e);
            }
        }

        disconnect();
        return result;
    }

    public static String putVeranstaltungByID(String id, String json){
        connect();
        String result = null;
        Map input = JsonHelper.toMap(json);
        Veranstaltung v = Veranstaltung.findById(id);
        if(v != null){
            int von = Integer.parseInt(input.get("von").toString());
            int bis = Integer.parseInt(input.get("bis").toString());
            String raum = input.get("raum").toString();

            if(Veranstaltung.istRaumFrei(von,bis, raum)) {
                v.set("raum", input.get("raum"));
                v.set("von", input.get("von"));
                v.set("bis", input.get("bis"));

                try {
                    v.saveIt();
                    result = v.toJson(true);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        disconnect();
        return result;
    }

    public static String deleteVeranstaltung(String id){
        return "";
    }

    public static String includeRaumInListe(String json){
        connect();
        Map[] input = JsonHelper.toMaps(json);
        Map[] output = new Map[input.length];

        for(int i = 0; i < input.length; i++){
            String mJSON = JsonHelper.getJsonStringFromMap(input[i]);
            String mitRaum = includeRaum(mJSON);
            output[i] = JsonHelper.toMap(mitRaum);

        }
        disconnect();
        return JsonHelper.getJsonStringFromMap(output);
    }

    public static String includeRaum(String json){
        connect();
        System.out.println("includeRAUM");
        Map input = JsonHelper.toMap(json);
        String raumJSON = Raum.getRaumdetails(input.get("raum").toString());
        if(raumJSON != null){
            input.put("raum", JsonHelper.toMap(raumJSON));
        }
        disconnect();
        return JsonHelper.getJsonStringFromMap(input);
    }
}
