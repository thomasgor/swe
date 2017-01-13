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
        System.out.println("getVeranstaltungByID "+  id);
        String result = null;
        connect();

        try {
            Veranstaltung v = Veranstaltung.findById(id);
            if (v != null) {
                result = v.toJson(true);
                result = includeRaum(result);
            }
        }catch(Exception e){
            System.out.println(e);
        }
        disconnect();
        return result;
    }

    public static boolean istRaumFrei(int von, int bis, String raumid){
        return istRaumFrei(von,bis, raumid, "");
    }

    public static boolean istRaumFrei(int von, int bis, String raumid, String nichtBeachten){
        /*
        * Ein Raum ist blockiert, wenn von oder bis inner halb des Zeitraums einer anderen veranstaltung liegen!
        */
        connect();
        LazyList<Veranstaltung> veranstaltungen = Veranstaltung.find("raum=?", raumid);

        if(veranstaltungen == null){
            return true;
        }

        for(Veranstaltung v : veranstaltungen){
            int von1 = Integer.parseInt(v.get("von").toString());
            int bis1 = Integer.parseInt(v.get("bis").toString());

            //Bei der Änderung einer Veranstaltung muss diese Bei der überprüfung ignoriert werden!
            if(nichtBeachten.equals(v.get("id").toString())){
                continue;
            }
            /*
            if(von >= von1 && von <= bis1){
                return false;
            }

            if(bis >= von1 && bis <=bis1){
                return false;
            }

            if(von1 >= von && von <= bis){
                return false;
            }

            if(bis1 >= von && bis <=bis){
                return false;
            }*/

            if(! ((von < von1 && bis <= von1) || (von >= bis1 && bis >=bis1))){
                return false;
            }
        }

        disconnect();
        return true;
    }

    /*
        Gibt null zurück, wenn der Raum blockiert ist, oder eine Exception aufgetereten ist!
        Ansonsten wird die neu erstellte Veranstlaltung zurück gegeben!
    */
    public static String postVeranstaltung(String json, String professorID){
        String result = null;
        connect();

        Veranstaltung v = new Veranstaltung();
        Map input = JsonHelper.toMap(json);
        int von = Integer.parseInt(input.get("von").toString());
        int bis = Integer.parseInt(input.get("bis").toString());
        String raum = input.get("raum").toString();

        if(Veranstaltung.istRaumFrei(von,bis,raum)){
            connect();
            v.set("benutzer",professorID);
            v.set("raum", input.get("raum"));
            v.set("name", input.get("name"));
            v.set("bis", input.get("bis"));
            v.set("von", input.get("von"));

            try{
                v.saveIt();
                String id = v.get("id").toString();
                result = getVeranstaltungByID(id);
            } catch (Exception e){
                //Es ist eine SQL-Exception aufgetreten!
                //Wahrscheinlich ist die raumid falsch!
                System.out.println(e);
            }
        }

        disconnect();
        return result;
    }

    public static String putVeranstaltungByID(String id, String json, String professorID){
        connect();
        String result = "OK";
        Map input = JsonHelper.toMap(json);
        Veranstaltung v = Veranstaltung.findById(id);

        int von = Integer.parseInt(input.get("von").toString());
        int bis = Integer.parseInt(input.get("bis").toString());

        String raumID = ((Map)input.get("raum")).get("id").toString();

        if(v == null){
            result = "NOT_FOUND";
        }

        else if(!v.get("benutzer").equals(professorID)){
            result = "FORBIDDEN";
        }
        else if(!Veranstaltung.istRaumFrei(von,bis, raumID, id)){
            result = "ROOM_BLOCKED";
        }
        else {
            connect();

            v.set("raum", raumID);
            v.set("name", input.get("name"));
            v.set("von", input.get("von"));
            v.set("bis", input.get("bis"));

            try {
                v.saveIt();
                result = getVeranstaltungByID(id);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        disconnect();
        return result;
    }

    public static String deleteVeranstaltung(String id, String professorID){
        connect();
        String result = null;
        Veranstaltung v = Veranstaltung.findById(id);
        if(v == null){
            result = "NOT_FOUND";
        }else if(!v.get("benutzer").equals(professorID)){
            result = "FORBIDDEN";
        }else{
            v.delete();
            result ="OK";
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
        String raumJSON = Raum.getRaumByID(input.get("raum").toString());
        if(raumJSON != null){
            input.put("raum", JsonHelper.toMap(raumJSON));
        }
        disconnect();
        return JsonHelper.getJsonStringFromMap(input);
    }
}
