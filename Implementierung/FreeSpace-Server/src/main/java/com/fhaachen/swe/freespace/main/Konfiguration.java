package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by thomasgorgels on 19.12.16.
 */
@Table("konfiguration")
@IdName("key")
public class Konfiguration extends Datenbank{

    public static boolean isMaster(String input){
        connect();
        String pw = Konfiguration.findById("masterpasswort").get("value").toString();

        disconnect();
        return (pw.equals(input));
    }

    public static String getSitzungsintervall(){
        connect();
        String intervall = Konfiguration.findById("sitzungsintervall").get("value").toString();
        disconnect();
        return intervall;
    }

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
        for(Tag element: tagList) {
            divTag.appendElement("label").appendElement("input").attr("type", "checkbox").attr("id", "checkboxError").attr("value", "option1").text((String)element.get("name"));
        }
        return doc.toString();
    }
}
