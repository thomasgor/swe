package com.swe.gruppe4.mockup2.Objektklassen;

/**
 * <p>Überschrift: Struktur von Verbindung </p>
 * <p>Beschreibung:
 * </p>
 * <p>Copyright: Merlin Copyright (c) 2016</p>
 * <p>Organisation: FH Aachen, FB05 </p>
 *
 * @author Merlin
 * @version 1.0
 *          Created on 22.11.2016.
 */
public final class Verbindung {
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
