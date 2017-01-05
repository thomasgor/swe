package com.swe.gruppe4.freespace.Objektklassen;

import com.swe.gruppe4.freespace.Objektklassen.*;

/**
 *
 */

public class AktuellerBenutzer {

    //Todo = new Benutzer kann weg, wenn der aktuelle Benutzer per RestConnection geholt wird
    private static Benutzer benutzer = new Benutzer(1,"abc@def.com","Pan","Peter","https://pbs.twimg.com/profile_images/775210778/peter_400x400.JPG", "", 0, 1, 1);

    public AktuellerBenutzer(Benutzer ben) {
        benutzer = ben;
    }

    public static Benutzer getAktuellerBenutzer() {

        //ToDo: Testdaten gegen valide Daten austauschen
        return benutzer;

    }

    public static void setAktuellerBenutzer(Benutzer ben) {
        benutzer = ben;
    }



}
