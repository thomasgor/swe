package com.swe.gruppe4.freespace.Objektklassen;

import java.io.Serializable;

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
