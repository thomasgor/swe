package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.JsonHelper;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.annotations.Table;

import java.util.Map;

/**
 * Created by thomas on 27.11.2016.
 */

@Table("Freundschaft")

public class Freundschaft extends Datenbank{

    private static String includeBenutzer(String json) {
        Map[] map = JsonHelper.toMaps(json);
        System.out.println(map[0].toString());
        Benutzer b = Benutzer.findById(map[0].get("benutzer"));
        if(b != null) {
            String json12 = b.toJson(true);
            Map map2 = JsonHelper.toMap(json12);
            map[0].put("benutzer", map2);
        }
        System.out.println(map[0].toString());
        return "";
    }

    public static String getFreundschaften(String benutzerID) {
        connect();
        String antwort = "[]";
        LazyList freunde = Freundschaft.find("benutzer = ? OR freund = ?", benutzerID, benutzerID);

        if(freunde != null) {
            antwort = freunde.toJson(true);
        }
        includeBenutzer(antwort);
        disconnect();
        return antwort;
    }

}
