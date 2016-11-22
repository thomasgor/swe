package com.swe.gruppe4.mockup2.Objektklassen;

/**
 *
 */

public class Benutzer {
    private long id;
    private String email;
    private String name;
    private String vorname;
    private String fotoURL;
    private String token;
    private boolean isProfessor;

    public Benutzer(long id, String email, String name, String vorname, String fotoURL, String token, boolean isProfessor) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.vorname = vorname;
        this.fotoURL = fotoURL;
        this.token = token;
        this.isProfessor = isProfessor;
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

    public boolean isProfessor() {
        return isProfessor;
    }

    public void setProfessor(boolean professor) {
        isProfessor = professor;
    }
}
