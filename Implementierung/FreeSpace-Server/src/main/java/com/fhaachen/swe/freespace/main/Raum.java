package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * Created by thomas on 27.11.2016.
 */

@Table("Raum")
public class Raum extends Model {

    public static String getRaum(){
        Datenbank.connect();
        String json = Raum.findAll().toJson(true);

        Datenbank.disconnect();
        return json;
    }
}
