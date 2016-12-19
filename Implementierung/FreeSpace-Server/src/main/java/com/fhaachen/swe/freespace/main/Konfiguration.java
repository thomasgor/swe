package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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

    private static String fileToString(String pathname) throws IOException {
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
