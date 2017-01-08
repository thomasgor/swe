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
import org.javalite.common.Base64;

/**
 * Created by thomas on 27.11.2016.
 */

/**
 * Die Klasse Benutzer implementiert, operationen auf der Datenbank, welche im Beuzug zu der Tabelle benutzer stehen
 *@author thomasgorgels
 */
@Table("Benutzer")
public class Benutzer extends Datenbank{

    /**
     *  Übeprüft durch einen Zugriff auf der Datenbank ob der Benutzer ein Professor ist.
     *  Dazu muss das Feld istProfessor == 1 sein!
     * @param id Die id des Nutzers
     * @return true falls Benutzer ein Professor ist, ansonsten false
     */
    public static boolean istProfessor(String id){
        connect();
        Long count = Benutzer.count("id=? AND istProfessor=1", id);
        disconnect();

        return(count != null && count >= 1);
    }

    /**
     * Ändert die Benutzerdaten in der Datenbank.
     * Dabei wird nur das Attribut masterpasswort aus dem String ausgelesen, wenn dies mit dem aktuellen Masterpasswort übereinstimmt,
     * wird istProfessor = 1 gesetzt. Somit ist der Benutzer ein Professor.
     * Stimmt das Masterpasswort nicht mit dem konfigurierten Passwort überein, wird keine Aktion ausgeführt
     * @param json String im Json-Format, aus welchem die zu ändernden Attribute gelesen werden
     * @param benutzerID ID des Benutzers, der geändert werden soll
     * @return Die geänderte Ressource als String im JSON-Format
     */
    public static String putBenutzer(String json, String benutzerID){
        String result = "";
        Map input = JsonHelper.toMap(json);

        connect();
        Benutzer b = Benutzer.findById(benutzerID);
        if(b != null){
            //Wenn Masterpasswort richtig, setze ist istProfessor
            if(input.containsKey("masterpasswort") && Konfiguration.isMaster(input.get("masterpasswort").toString())){
                b.set("istProfessor", 1);
            }

            //Wenn istAnony gesetzt ist, setze die Flag
            if(input.containsKey("istanonym")) {
                String input_istAnonym = input.get("istanonym").toString();
                if (input_istAnonym.equals("1") || input_istAnonym.equals("0")) {
                    b.set("istAnonym", input_istAnonym);
                }
            }

            //Wenn istPush gesetzt ist, setze die Flag
            if(input.containsKey("istpush")) {
                String input_istPush = input.get("istpush").toString();
                if (input_istPush != null && (input_istPush.equals("1") || input_istPush.equals("0"))) {
                    b.set("istPush", input_istPush);
                }
            }

            //Wenn tokenFCM gesetzt ist, setze die Flag
            if(input.containsKey("tokenfcm")) {
                String input_token_fcm = input.get("tokenfcm").toString();
                if (input_token_fcm != null) {
                    b.set("tokenFCM", input_token_fcm);
                }
            }

            try{
                connect();
                b.saveIt();
                result = b.toJson(true);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        disconnect();
        return result;
    }

    /**
     * Diese Methode wird zur Registrierung der Benutzer genutzt.
     * Wenn ein Benutzer seine ID im String schickt, wird überprüft, ob der Benutzer wiederkehrend ist, somit schon im System vorhanden ist,
     * is dies Der Fall, wird kein neuer Benutzer angelegt, der bestehende wird ausgegeben.
     * handelt es sich um einen neuen Benutzer, wird ein neuer Benutzer in die Datenbank geschrieben und als String im JSON-Format zurückgegeben
     * @param json Ein Benutzer als String im JSON-Format
     * @return Ein Benutzer asl String im JSON-Format
     */
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
            neu.set("tokenFCM", input.get("tokenFCM"));

            try {
                neu.insert();
                result = neu.toJson(true);
                System.out.println("postBenutzer: Neuer Benutzer angelegt "+ result);
            }catch(Exception e){
                System.out.println(e);
            }
        }
        disconnect();
        return result;
    }

    /**
     * Diese methode genieriert ein 32-Zeichen langes Token für neue Benutzer
     *
     * @return 32-zeichen langes Token, bestehende aus buchstaben und Zahlen
     */
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

    /**
     * Diese Methode prüft, ob der Benutzer ein Adrministrator ist, hier wird mit name und passwort gearbeitet, da ein adminstrator nur zugriff auf das Webinterface hat
     * Er ist kein eigentlicher Benutzer des Systems!
     * @param name Benutzername für den Administrator-Account
     * @param pw Passwort für den Administrator-Accoutn
     * @return true, falls Administartor- Benutzername und Passwort mit den, in der Datenbank hinterlegten Werten stimmen!
     */
    public static boolean istAdministrator(String name, String pw){
        connect();
        String admin_name = Konfiguration.findById("admin_name").get("value").toString();
        String admin_pw = Konfiguration.findById("admin_passwort").get("value").toString();
        disconnect();

        return(name.equals(admin_name) && pw.equals(admin_pw));
    }

    /**
     * Ermittelt die Rolle zu einem Benutzer
     * Dabei gilt, ein der Benutezr ist ein Professor, falls istProfessor == 1 ist, ansonsten ist er ein user
     * @param id Benutzer id, des zu prüfunden Benutzers
     * @return professor, falls istProfessor == 1, ansonsten user
     */
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

    /**
     * Überprüft, ob es die Benutzer-ID schon in der Datenbank gibt, hier können wiederkehrende Benutzer erkannt werden
     * @param id Benutzer-ID des Benutzers
     * @return true, falls die Benutzer-ID in der Datenbank gefunden wurde, ansonsten false
     */
    public static boolean istBenutzer(String id){
        connect();
        Long count = Benutzer.count("id=?", id);
        disconnect();
        return(count != null && count > 0);
    }

    /**
     * Übeprüft die gültigkeit eines Tokens, eines Benutezrs
     * @param id ID des Benutzers
     * @param token Token des Benutzers
     * @return true, falls die übergebenen Daten mit den in der Datenbank hintelegten übereinstimmen, ansonsten false
     */
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

    /**
     * Setzt die BenutzerRollen in den Zustand "Student/normaler Benutzer" zurück.
     */
    public static void resetBenutzerRolle(){
        connect();
        String sql = "UPDATE Benutzer SET istProfessor = 0";
        Base.exec(sql);
        disconnect();
    }

    /**
     * Übeprüft ob der angemeldete Nutzer ein Administrator ist
     * @param cookieValue Enthält den Wert des zu überprüfenden Cookies
     * @return true, falls der Benutzer ein Administrator ist
     */
    public static boolean istAdministrator(String cookieValue){
        connect();
        String admin_name = Konfiguration.findById("admin_name").get("value").toString();
        String admin_pw = Konfiguration.findById("admin_passwort").get("value").toString();
        disconnect();

        return cookieValue.equals(Base64.getEncoder().encodeToString((admin_name+":"+admin_pw).getBytes()));
    }

}
