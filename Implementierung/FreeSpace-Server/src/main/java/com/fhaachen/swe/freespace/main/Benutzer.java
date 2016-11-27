package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Benutzer")
public class Benutzer extends Model{

    public static void main(String[] args) {

        Base.open("org.sqlite.JDBC", "jdbc:sqlite:./freespace.db", "root", "p@ssw0rd");
        Benutzer b = Benutzer.findById(4711);

        System.out.println(b.get("name"));
        Base.close();
    }

}
