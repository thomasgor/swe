package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.Base;

/**
 * Created by thomasgorgels on 30.11.16.
 */
public class Datenbank {
    public static void connect(){
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:./freespace.db", "root", "p@ssw0rd");
    }

    public static void disconnect(){
        Base.close();
    }
}
