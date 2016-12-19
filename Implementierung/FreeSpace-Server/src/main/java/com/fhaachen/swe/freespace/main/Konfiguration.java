package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

import java.io.BufferedReader;
import java.io.File;
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

    public static  int getSitzungsintervall(){
        connect();
        int intervall = Integer.parseInt(Konfiguration.findById("sitzungsintervall").get("value").toString());
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
            body.appendElement("div").attr("class", "alert alert-danger alert-dismissable").appendElement("");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String putValuesInHTML() {
        File input = new File("/admin/einstellungen.html");
        Document doc;
        return "";
    }

    public static void main(String[] args) {
        try {
            System.out.println(fileToString("admin/login.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
