package com.swe.gruppe4.freespace.Objektklassen;

import java.io.Serializable;

/**
 * <p>Überschrift: Struktur von Verbindung </p>
 * <p>Beschreibung: Speichert Objekte zur Verbindung
 * </p>
 * <p>Organisation: FH Aachen, FB05 </p>
 *
 * @author Merlin
 * @version 1.0
 *
 * @deprecated  In der aktuellen Version durch statische Attribute in der Klasse RestConnection ersetzt
 */

@Deprecated
public final class Verbindung implements Serializable{
    private static long id;
    private static String token;

    private static Verbindung verbindung;

    private Verbindung() {
    }

    public static long getId() {
        return id;
    }

    public static void setId(long id) {
        Verbindung.id = id;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Verbindung.token = token;
    }
}
