package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import javax.annotation.security.RolesAllowed;

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

    public String includeRaum(String json){
        return "";
    }
}
