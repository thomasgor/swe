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

    static int test = 1234;
    public static boolean test(){
        //Überptüfen ob token und id in DB stehen;

        List<Benutzer> b = Benutzer.where("id = ? and token=?", "4711", "abc123");
        System.out.println(b.get(0).toJson(true));
        Base.close();
        return true;
    }

    public static void main(String[] args) {

    }

}
