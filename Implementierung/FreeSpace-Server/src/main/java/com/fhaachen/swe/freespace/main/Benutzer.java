package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.annotations.Table;

/**
 * Created by thomas on 27.11.2016.
 * Edited by simon on 04.12.2016.
 */
@Table("Benutzer")
public class Benutzer extends Datenbank{

//  static int test = 1234;
//  public static boolean test(){
//      List<Benutzer> b = Benutzer.where("id = ? and token=?", "4711", "abc123");
//      System.out.println(b.get(0).toJson(true));
//      Base.close();
//      return true;
//  }

    public static String postBenutzer(String id, String email, String token, String name, String vorname, String foto, int istProfessor) {
        connect();
        String json = Benutzer.createIt("id", id, "email", email, "token", token, "name", name, "vorname", vorname, "foto", foto, "istProfessor", istProfessor).toJson(true);
        disconnect();
        return json;
    }

    public static String putBenutzer(String benutzerID, String email, String token, String name, String vorname, String foto, int istProfessor) {
        connect();
        Benutzer ben = Benutzer.findFirst("id = ?",benutzerID);
        ben.set("email", email).set("token", token).set("name", name).set("vorname", vorname).set("foto", foto).set("istProfessor", istProfessor).saveIt();
        String json = ben.toJson(true);
        disconnect();
        return json;
    }
}
