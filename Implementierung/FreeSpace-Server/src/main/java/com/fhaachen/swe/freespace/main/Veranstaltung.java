package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.JsonHelper;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Veranstaltung")
public class Veranstaltung extends Datenbank {
    //TODO: Es soll möglich sein ein Tag objekt zu inkludieren

    public static String getVeranstaltung(String professorID) {
        String result = "{}";
        connect();

        LazyList veranstaltungen = Veranstaltung.find("benutzer=?", professorID);
        disconnect();

        //Es wurden keine Veranstsltungen für den Professor gefunden
        if(veranstaltungen == null){
            System.out.println("Keine Veranstaltung von Professor " + professorID);
            return result;
        }

        //Es wurden Veranstaltungen gefunden
        result = veranstaltungen.toJson(true);

        return result;
    }

    public String getVeranstaltungId(String id, String professorID){
        String result = null;
        connect();
        Veranstaltung v = Veranstaltung.findById(id);
        disconnect();

        //Es wurde eine Veranstlung gefunden und es ist eine Vom professor!
        if(v != null && v.get("benutzer").equals(professorID)){
            result = v.toJson(true);
        }
        return result;
    }

    public String postVeranstaltung(String json){
        String result = null;
        connect();

        Veranstaltung v = new Veranstaltung();
        Map input = JsonHelper.toMap(json);
        //TODO: Überprüfun ob zu dieser Zeit in diesem Raum bereits eine Veranstaltung vorliegt
        v.set("benutzer", input.get("benutzer"));
        v.set("raum", input.get("raum"));
        v.set("name", input.get("name"));
        v.set("bis", input.get("bis"));
        v.set("von", input.get("von"));

        v.saveIt(); //Todo: Konnte die Veranstaltung eingefügt werden?



        disconnect();
        return result;
    }

    public String includeRaum(String json){
        return "";
    }
}
