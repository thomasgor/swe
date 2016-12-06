package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Sitzung")
public class Sitzung extends Datenbank {


    public static String getSitzungById(String id){
        connect();
        String json = Sitzung.findById(id).toJson(true);
        disconnect();
        return json;
    }

    public static boolean hasActiveSession(String id){
      //  connect();
    //    boolean hasActiveSession = Sitzung.findById(id).gethasSitzung();
    //    disconnect();
    //    return json;
        return false;
    }

}
