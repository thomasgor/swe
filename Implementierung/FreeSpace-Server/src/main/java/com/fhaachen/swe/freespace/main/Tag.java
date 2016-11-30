package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Tag")
public class Tag extends Model {

    public static String getTag(){
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:./freespace.db", "root", "p@ssw0rd");
        String json = Tag.findAll().toJson(true);
        Base.close();
        return json;
    }
}
