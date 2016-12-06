package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import javax.annotation.security.RolesAllowed;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Veranstaltung")
@RolesAllowed({"user", "professor"})
public class Veranstaltung extends Datenbank {

    public static String getVeranstaltung() {
        connect();

        LazyList veranstaltungen = Veranstaltung.findAll();

        disconnect();
        return "";
    }
}
