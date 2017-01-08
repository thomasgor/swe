package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by thomasgorgels on 19.12.16.
 */
@Table("konfiguration")
@IdName("key")
public class Konfiguration extends Datenbank{

    /**
     * Methode validiert den cookie, überprüft also, ob die Daten (User:PW) für den Administrator-Account übereinstimmen
     * @param value Base64 codiert: (Username:passwort)
     * @return true, falls die Daten mit den Werten aus der Datenbank übereinstimen, ansonsten false
     */
    public static boolean validiereCookie(String value){
        @SuppressWarnings("Since15") String decoded = new String(DatatypeConverter.parseBase64Binary(value), Charset.forName("ASCII"));
        String[] values = decoded.split(":");

        //Falsche Syntax
        if(values.length < 2){
            return false;
        }
        String admin_name = values[0];
        String admin_pw = values[1];

        return Benutzer.istAdministrator(admin_name, admin_pw);
    }

    /**
     * Überprüft, ob der übergebene String gleich dem gesetzetn Masterpasswort aus der Datenbank ist
     * @param input zu prüfundes Masterpasswort
     * @return true, falss input == masterpasswort, ansonsten flase
     */
    public static boolean isMaster(String input){
        connect();
        String pw = Konfiguration.findById("masterpasswort").get("value").toString();

        disconnect();
        return (pw.equals(input));
    }

    /**
     * Liest den aktuell konfigurierten Sitzungsintervall aus der Datenbank aus
     * @return Aktueller Sitzungsintervall in Minuten
     */
    public static String getSitzungsintervall(){
        connect();
        String intervall = Konfiguration.findById("sitzungsintervall").get("value").toString();
        disconnect();
        return intervall;
    }

    /**
     * Liest eine Datei ein, und wandelt den Inhalt dieser in ein variable vom Typ String um
     * @param pathname Pfad der einzulesenden Datei
     * @return Inhalt der Datei
     * @throws IOException Falls die Datei nicht gefunden wurde
     */
    public static String fileToString(String pathname) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(pathname));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

    /**
     * Liest den Inhalt der Login-HTML-Seite ein, erweitert den body um eine Fehlerbox
     * @return HTML-Inhalt der Loginseite inkl. Fehlerbox
     */
    public static  String getLoginFehlerHTML(){
        try {
            String html = fileToString("admin/login.html");
            Document doc = Jsoup.parse(html, "UTF-8");

            Element userform = doc.getElementById("login_form");
            String userform_class = userform.attr("class");
            userform.attr("class", userform_class += " has-error");

            Element body = doc.getElementById("body");
            body.prepend("<div class = \"alert alert-danger alert-dismissable\">" +
                    "<button type = \"button\" class = \"close\" data-dismiss = \"alert\" aria-hidden = \"true\">" +
                    " &times;" +
                    " </button>" +
                    " <strong>FEHLER: </strong> Bitte pr&uuml;fen sie Ihre Eingaben" +
                    "</div>");
            return doc.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  "";
    }

    /**
     * Liest den Inhalt der Login-HTML-Seite ein
     * Der Inhalt wird um einen Succes-Box erweitert
     * @return HTML für die Logot-Seite
     */
    public static String getLogoutHTML(){
        try {
            String html = fileToString("admin/login.html");
            Document doc = Jsoup.parse(html, "UTF-8");

            Element userform = doc.getElementById("login_form");
            String userform_class = userform.attr("class");
            Element body = doc.getElementById("body");
            body.prepend("<div class = \"alert alert-success alert-dismissable\">" +
                    "<button type = \"button\" class = \"close\" data-dismiss = \"alert\" aria-hidden = \"true\">" +
                    " &times;" +
                    "</button>" +
                    " <strong>Sie haben sich erfolgreich abgemeldet.</strong> Bis zum n&auml;chsten mal!" +
                    "</div>");
            return doc.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  "";
    }

    /**
     * Liest den Inhalt der Einstellungen-HTML-Seite ein.
     * Anschließend wird der Inhalt der eingelesenen Datei um die Tag-Liste und den Wert der Textboxes erweitert.
     * @return Inhalt der Einstellungen-HTML-Seite inkl. tags und Vorbelegungen der Werte-Felder
     */
    public static String getEinstellungenHTML() {
        String input = null;
        try {
            input = fileToString("admin/einstellungen.html");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Document doc = Jsoup.parse(input, "UTF-8");
        Element intervall = doc.getElementById("input_sitzungsintervall");
        intervall.attr("value", getSitzungsintervall());
        Element divTag = doc.getElementById("tags");
        LazyList<Tag> tagList = Tag.getTagList();
        Integer count = new Integer(1);
        for(Tag element: tagList) {
            divTag.appendElement("label").appendElement("input").attr("type", "checkbox").attr("id", "tag" + count.toString()).attr("name", "tags").attr("value", element.getId().toString()).text((String)element.get("name"));
            count++;
        }
        return doc.toString();
    }

    /**
     * Liest die Einstellungen HTML-Seite ein und erweitert diese um eine Succes-Box
     * @return HTML-Inhalt der Einstellungen-Seite inkl. tags und Vorbelegung und Succesbox
     */
    public static String getEinstellungenSaved() {
        String html = Konfiguration.getEinstellungenHTML();
        Document doc = Jsoup.parse(html, "UTF-8");
        Element body = doc.getElementById("body");
        body.prepend("<div class = \"alert alert-success alert-dismissable\">" +
                    "<button type = \"button\" class = \"close\" data-dismiss = \"alert\" aria-hidden = \"true\">" +
                    " &times;" +
                    "</button>" +
                    " <strong>Einstellungen erfolgreich gespeichert!</strong>" +
                    "</div>");
        return doc.toString();
    }
    /**
     * Liest den Inhalt der Einstellungen-HTML-Seite ein und erweitert den body um eine Fehlerbox.
     * @return Inhalt der Einstellungen-HTML-Seite inkl. Fehlerbox
     */
    public static String getEinstellungen_FehlerhaftHTML() {
        String input = null;
        try {
            input = fileToString("admin/einstellungen.html");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String html = Konfiguration.getEinstellungenHTML();
        Document doc = Jsoup.parse(html, "UTF-8");
        Element body = doc.getElementById("body");
        body.prepend("<div class = \"alert alert-danger alert-dismissable\"> "+
                "<button type = \"button\" class = \"close\" data-dismiss = \"alert\" aria-hidden = \"true\"> &times;</button> "+
                "<strong>Fehler: Bitte geben sie g&uuml;ltige Daten ein!</strong>"+
                "</div>");

        return doc.toString();
    }
    /**
     * Speichert die neuen Konfigurationen.
     * @param altPasswort Das alte Passwort
     * @param neuPasswort1 Das neue Passwort
     * @param neuPasswort2 Das wiederholte neue Passwort
     * @param tags Die Liste der neuen Tags
     * @param neuerTag Der neuanzulegende Tag
     * @param sitzungsintervall Die Dauer des neuen Sitzungsintervalls
     * @return true, wenn das Speichern erfolgreich war.
     */
    public static boolean setKonfiguration(String altPasswort, String neuPasswort1, String neuPasswort2, List<String> tags, String neuerTag, String sitzungsintervall){
        try{
            if(neuPasswort1 != null && neuPasswort1.length() > 0){
                if(isMaster(altPasswort)) {
                    System.out.println("Ändere Passwort");
                    if (!setNeuesMasterpasswort(neuPasswort1, neuPasswort2)) {
                        return false;
                    }
                }else{
                    return false;
                }
            }

            //Neuen Tag hinzufügen
            if(!neuerTag.equals("")){Tag.addNeuerTag(neuerTag);}

            //Tags löschen
            Tag.deleteTags(tags);

            //Sitzungsintervall ändern
            setSitzungsIntervall(sitzungsintervall);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
    /**
     * Speichert die neuen Konfigurationen.
     * @param neuPW1 Das neue Passwort.
     * @param neuPW2 Das wiederholte neue Passwort.
     * @return true, wenn das Speichern erfolgreich war.
     */
    private static boolean setNeuesMasterpasswort(String neuPW1, String neuPW2){
        try {
            if(neuPW1 == null && neuPW1.length()<=0)
                return false;
            if(neuPW2 == null){
                return false;
            }

            if(neuPW1.equals(neuPW2) ){
                System.out.println("Ändere Passwort2");
                connect();
                System.out.println("UPDATE Konfiguration SET value = ? WHERE key == 'masterpasswort'");
                Base.exec("UPDATE Konfiguration SET value = ? WHERE key == 'masterpasswort'", neuPW1);
                disconnect();
                // Benutzer.resetBenutzerRolle();
            }else{
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * Speichert die neuen Konfigurationen.
     * @param neuesIntervall Das neue Intervall der Sitzung.
     * @return true, wenn das Speichern erfolgreich war.
     */
    private static boolean setSitzungsIntervall(String neuesIntervall){
        try {
            if(neuesIntervall == null || Integer.parseInt(neuesIntervall) <= 1 ){
                return false;
            }
            connect();
            Base.exec("UPDATE Konfiguration SET value = ? WHERE key == 'sitzungsintervall' ", neuesIntervall);
            disconnect();

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
