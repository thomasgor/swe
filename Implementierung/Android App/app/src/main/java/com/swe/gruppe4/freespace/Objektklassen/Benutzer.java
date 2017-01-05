package com.swe.gruppe4.freespace.Objektklassen;

import java.io.Serializable;
import org.json.JSONObject;
import org.json.JSONException;

/**
 *
 */

public class Benutzer implements Serializable {
    private String id;
    private String email;
    private String name;
    private String vorname;
    private String fotoURL;
    private String token;
    private int isProfessor;
    private int istAnonym;
    private int istPush;

    public Benutzer(long id, String email, String name, String vorname, String fotoURL, String token, int isProfessor, int istAnonym, int istPush) {
        this.id = String.valueOf(id);
        this.email = email;
        this.name = name;
        this.vorname = vorname;
        this.fotoURL = fotoURL;
        this.token = token;
        this.isProfessor = isProfessor;
        this.istAnonym = istAnonym;
        this.istPush = istPush;
    }

    public Benutzer(String id, String email, String name, String vorname, String fotoURL, String token, int isProfessor, int istAnonym, int istPush) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.vorname = vorname;
        this.fotoURL = fotoURL;
        this.token = token;
        this.isProfessor = isProfessor;
        this.istAnonym = istAnonym;
        this.istPush = istPush;
    }

    public Benutzer(String jsonBenutzer) {
        try {
            JSONObject jsonObj = new JSONObject(jsonBenutzer);

            this.id = jsonObj.getString("id");
            this.email = jsonObj.getString("email");
            this.name = jsonObj.getString("name");
            this.vorname = jsonObj.getString("vorname");
            this.fotoURL = jsonObj.getString("foto");
            this.token = jsonObj.getString("token");
            this.isProfessor = jsonObj.getInt("istProfessor");
            this.istAnonym = jsonObj.getInt("istAnonym");
            this.istPush = jsonObj.getInt("istPush");

        } catch (JSONException e) {
            //jsonBenutzer enthält keinen Benutzer!
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public boolean isPush() {if(this.istPush == 1) return true; return false;}

    public void setPush(boolean push) { if(push){this.istPush = 1;}else{this.istPush = 0;}}
}
