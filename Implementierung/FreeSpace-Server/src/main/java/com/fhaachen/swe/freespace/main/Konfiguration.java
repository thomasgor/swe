package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

/**
 * Created by thomasgorgels on 19.12.16.
 */
@Table("konfiguration")
@IdName("key")
public class Konfiguration extends Datenbank{

    public static boolean isMaster(String input){
        connect();
        String pw = Konfiguration.findById("masterpasswort").get("value").toString();

        disconnect();
        return (pw.equals(input));
    }

    public static  int getSitzungsintervall(){
        connect();
        int intervall = Integer.parseInt(Konfiguration.findById("sitzungsintervall").get("value").toString());
        disconnect();
        return intervall;
    }
}
