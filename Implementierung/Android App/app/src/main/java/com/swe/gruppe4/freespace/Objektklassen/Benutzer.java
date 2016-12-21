package com.swe.gruppe4.freespace.Objektklassen;

import java.io.Serializable;

/**
 *
 */

public class Benutzer implements Serializable {
    private long id;
    private String email;
    private String name;
    private String vorname;
    private String fotoURL;
    private String token;
    private int isProfessor;
    private int istAnonym;

    public Benutzer(long id, String email, String name, String vorname, String fotoURL, String token, int isProfessor, int istAnonym) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.vorname = vorname;
        this.fotoURL = fotoURL;
        this.token = token;
        this.isProfessor = isProfessor;
        this.istAnonym = istAnonym;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean istProfessor() {if(this.isProfessor == 1) return true; return false;}

    public void setProfessor(boolean professor) { if(professor){this.isProfessor = 1;}else{this.isProfessor = 0;}}

    public boolean isAnonymous(){if(this.istAnonym == 1) return true; return false;}

    public void setAnonymous(boolean Anonym){ if(Anonym == true){ this.istAnonym = 1;}else{ this.istAnonym = 0;}}
}
