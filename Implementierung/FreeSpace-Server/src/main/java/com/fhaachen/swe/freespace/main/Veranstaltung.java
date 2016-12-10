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

    public static String getVeranstaltung(String professorID) {
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

    public boolean istRaumBlockiert(int von, int bis, String raumid){

        LazyList v = Veranstaltung.find("");
        return false;
    }

    public static String postVeranstaltung(String json, String professorID){
        String result = null;
        connect();

        Veranstaltung v = new Veranstaltung();
        Map input = JsonHelper.toMap(json);
        //TODO: Überprüfun ob zu dieser Zeit in diesem Raum bereits eine Veranstaltung vorliegt
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

        disconnect();
        return result;
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
