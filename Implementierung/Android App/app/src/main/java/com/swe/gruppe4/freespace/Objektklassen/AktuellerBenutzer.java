package com.swe.gruppe4.freespace.Objektklassen;

import com.swe.gruppe4.freespace.Objektklassen.*;

/**
 * <p>Überschrift: Struktur von Aktueller Benutzer</p>
 * <p>Beschreibung: Diese Klasse dient dazu, Informationen über den aktuell eingeloggten Benutzer zu speichern
 * </p>
 * <p>Organisation: FH Aachen, FB05, SWE Gruppe 4 </p>
 *
 * @author Matthias (?)
 * @version 1.0
 *
 */

public class AktuellerBenutzer {

    //Todo = new Benutzer kann weg, wenn der aktuelle Benutzer per RestConnection geholt wird
    private static Benutzer benutzer = new Benutzer(1,"abc@def.com","Pan","Peter","https://pbs.twimg.com/profile_images/775210778/peter_400x400.JPG", "", 1, 1, 1);

    public AktuellerBenutzer(Benutzer ben) {
        benutzer = ben;
    }

    /**
     * Gibt den aktuellen Benutzer zurück, um seine Informationen verarbeiten zu könnnen.
     *
     * @return      der aktuell eingeloggte Benutzer
     * */
    public static Benutzer getAktuellerBenutzer() {

        //ToDo: Testdaten gegen valide Daten austauschen
        return benutzer;

    }

    /**
     * Setzt den aktuellen Benutzer
     *
     * @param ben Zu speicherndes Benutzerobjekt
     * */
    public static void setAktuellerBenutzer(Benutzer ben) {
        benutzer = ben;
    }



}
