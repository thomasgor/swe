package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

/**
 * Created by thomasgorgels on 30.11.16.
 */

public class Datenbank extends Model {
    public static void connect(){
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:./freespace.db", "root", "p@ssw0rd");
        Base.exec("PRAGMA foreign_keys = ON");
    }

    public static void disconnect(){
        Base.close();
    }
}
