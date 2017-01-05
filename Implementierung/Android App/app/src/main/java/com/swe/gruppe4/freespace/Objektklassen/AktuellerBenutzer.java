package com.swe.gruppe4.freespace.Objektklassen;

import com.swe.gruppe4.freespace.Objektklassen.*;

/**
 *
 */

public class AktuellerBenutzer {
    private static Benutzer benutzer;

    public AktuellerBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
    }

    public static Benutzer getAktuellerBenutzer() {

        //ToDo: Testdaten gegen valide Daten austauschen
        return new Benutzer(1,"abc@def.com","Pan","Peter","https://pbs.twimg.com/profile_images/775210778/peter_400x400.JPG", "", 1, 0, 0);
        //return this.benutzer;
    }



}
