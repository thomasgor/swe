package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.JsonHelper;
import jdk.nashorn.internal.runtime.JSONFunctions;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import javax.xml.ws.WebServiceException;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Benutzer")
public class Benutzer extends Datenbank{

    public static boolean istProfessor(String id){
        connect();
        Long count = Benutzer.count("id=? AND istProfessor=1", id);
        disconnect();

        return(count != null && count >= 1);
    }

    public static String putBenutzer(String json, String benutzerID){
        String result = "";
        Map input = JsonHelper.toMap(json);
        String input_pw = input.get("masterpasswort").toString();

        /*
        if(!"admin".equals(input_pw)){
            return null;
        }*/

        if(!Konfiguration.isMaster(input_pw)){
            return null;
        }

        connect();
        Benutzer b = Benutzer.findById(benutzerID);
        if(b != null){
            b.set("istProfessor", 1);
            try{
                b.saveIt();
                result = b.toJson(true);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        disconnect();
        return result;
    }

    public static String postBenutzer(String json){
        String result = null;
        Map input = JsonHelper.toMap(json);
        String benutzerID = input.get("id").toString();

        if(istBenutzer(benutzerID)){
            connect();
            System.out.println("Benutzer mit der id="+benutzerID+" ist schon vorhanden");
            Benutzer b = Benutzer.findById(benutzerID);
            result = b.toJson(true);
        }else{
            connect();
            Benutzer neu = new Benutzer();
            neu.set("id",benutzerID);
            neu.set("email", input.get("email"));
            neu.set("token", generiereToken());
            neu.set("name", input.get("name"));
            neu.set("vorname", input.get("vorname"));
            neu.set("foto", input.get("foto"));

            try {
                neu.saveIt();
                result = neu.toJson(true);
            }catch(Exception e){
                System.out.println(e);
            }
        }

        disconnect();
        return result;
    }

    public static String generiereToken(){
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 32; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }
    public static boolean istAdministrator(String name, String pw){
        connect();
        String admin_name = Konfiguration.findById("admin_name").get("value").toString();
        String admin_pw = Konfiguration.findById("admin_passwort").get("value").toString();
        disconnect();

        return(name.equals(admin_name) && pw.equals(admin_pw));
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

    public static boolean prüfeToken(String id, String token){
        System.out.println("Prüfue token benutzer="+id+" token="+token);
        connect();
        Benutzer b = Benutzer.findById(id);
        String t = null;
        if(b != null){
            t = b.get("token").toString();
        }

        disconnect();
        return (token.equals(t));
    }

    public static void main(String[] args) {

    }

}
