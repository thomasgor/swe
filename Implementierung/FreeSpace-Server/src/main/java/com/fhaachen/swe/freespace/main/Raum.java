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
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:./freespace.db", "root", "p@ssw0rd");

        String json = Raum.findAll().toJson(true);

        Base.close();
        return json;

    }
}
