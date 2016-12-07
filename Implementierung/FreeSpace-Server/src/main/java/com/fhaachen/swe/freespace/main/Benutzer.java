package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import javax.xml.ws.WebServiceException;
import java.util.List;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Benutzer")
public class Benutzer extends Datenbank{

    public static void getDataFromGoogle(int idToken){


    }

    public static boolean istProfessor(String id){
        connect();
        Long count = Benutzer.count("id=? AND istProfessor=1", id);
        disconnect();

        return(count != null && count >= 1);
    }

    public static String getRolle(String id){
        connect();
        Benutzer b = Benutzer.findById(id);
        String rolle = null;

        if((Integer) b.get("istProfessor") == 1){
            rolle = "professor";
        }else if((Integer) b.get("istProfessor") == 0){
            rolle = "user";
        }

        disconnect();

        return rolle;
    }

    public static boolean istBenutzer(String id){
        connect();
        Long count = Benutzer.count("id=?", id);
        disconnect();
        return(count != null && count > 0);
    }

    public static void main(String[] args) {

    }

}
