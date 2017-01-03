package com.swe.gruppe4.freespace;

import com.swe.gruppe4.freespace.Objektklassen.Benutzer;
import com.swe.gruppe4.freespace.Objektklassen.Raum;
import com.swe.gruppe4.freespace.Objektklassen.Tag;
import com.swe.gruppe4.freespace.Objektklassen.Veranstaltung;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Mit der Klasse JsonStringBuilder können die Json Objekte als String erstellt werden, die fuer die
 * Server Requests gebraucht werden.
 *
 * benoetigt wurden lt. Systementwurf:
 * buildPOSTbenutzerJson(Benutzer benutzer)
 * buildPUTbenutzerJson(Benutzer benutzer, String password)
 * buildPOSTtagJson(Tag tag)
 * buildPOSTfreundschaftJson(String email)
 * buildPUTraumJson(Tag tag)
 * buildPOSTveranstaltungJson(Veranstaltung veranstaltung)
 * buildPUTveranstaltungJson(Veranstaltung veranstaltung)
 * buildPOSTsitzungJson(List<Raum> raumList)
 *
 * mit der Methode getFromJson(String jsonString, String metaData) kann man ein Datum aus dem
 * Json String extrahieren
 *
 * Created by Eduard on 31.12.2016.
 * Last time modified: 03.01.2017
 */

public class JsonStringBuilder {

    /**
     * benutzer POST: Diese Ressource wird benutzt, wenn der Benutzer auf einloggen klickt oder die App öffnet
     * Antworten:   Status 200 OK: Wenn der Benutzer widerkehrend ist
     *              Status 201 CREATED: Wenn der Benutzer neu ist
     */
    public String buildPOSTbenutzerJson(Benutzer benutzer)
    {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("id", benutzer.getId());
            jsonObj.put("email", benutzer.getEmail());
            jsonObj.put("name", benutzer.getName());
            jsonObj.put("vorname", benutzer.getVorname());
            jsonObj.put("fotoURL", benutzer.getFotoURL());
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /**
     * benutzer PUT: Die Ressource wird benutzt, wenn das Masterpasswort eingegeben wurde
     * Antworten:   Status 200 OK: Masterpasswort korrekt
     *              Status 403 FORBIDDEN: Masterpasswort falsch
     * @param benutzer
     * @param password
     * @return
     */
    public String buildPUTbenutzerJson(Benutzer benutzer, String password)
    {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("masterpasswort", password);
            jsonObj.put("isAnonym", benutzer.isAnonymous());
            jsonObj.put("isPush", benutzer.isPush());
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /**
     * tag POST: Die Ressource wird benutzt, um einen neuen Tag hinzuzufügen
     * Antworten:   Status 201 CREATED: Tag erfolgreich erstellt
     * @param tag
     * @return
     */
    public String buildPOSTtagJson(Tag tag)
    {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("name", tag.getName());
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /**
     * freundschaft POST: Die Ressource wird benutzt, um einen neuen Freund hinzuzufügen.
     * Antworten:   Status 200 OK:-	Der Server erstellt eine neue Freundschaft mit dem Status „anfrage“
     *                            -	Besteht schon eine Freundschaft zwischen den Benutzern, wird trotzdem 200 OK ohne Änderungen am Datenbestand zurückgegeben
                                  -	Gibt es keinen Benutzer mit der angegebenen Email-Adresse, wird trotzdem 200 OK zurückgegeben.

     * @param email
     * @return
     */
    public String buildPOSTfreundschaftJson(String email)
    {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("emal", email);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /**
     * raum PUT: Die Ressource wird benutzt, um einen Tag in einem Raum zu setzen
     * Antworten:   Status 200 OK: Tag wurde gesetzt
     *              Status 403 FORBIDDEN: Wenn schon ein Tag gesetzt wurde, bevor der Client den
     *                                    Tag gesetzt hat. Antwort enthält trotzdem Raumobjekt.
     * @param tag
     * @return
     */
    public String buildPUTraumJson(Tag tag)
    {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("tag", tag.getId());
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /**
     * raum POST: Die Ressource wird benutzt, um eine Veranstaltung zu erstellen.
     * Antworten:   Status 201 CREATED: Verantstaltung erfolgreich erstellt
     *              Status 403 FORBIDDEN:
     *              Status 910 ROOM BLOCKED:
     * @param veranstaltung
     * @return
     */
    public String buildPOSTveranstaltungJson(Veranstaltung veranstaltung)
    {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("name", veranstaltung.getName());
            jsonObj.put("von", veranstaltung.getVon());
            jsonObj.put("bis", veranstaltung.getBis());
            jsonObj.put("raum", veranstaltung.getRaum().getId());
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /**
     * raum PUT: Die Ressource wird benutzt, um eine Veranstaltung zu bearbeiten.
     * Antworten:   Status 200 OK: Verantstaltung erfolgreich geaendert
     *              Status 403 FORBIDDEN:
     *              Status 910 ROOM BLOCKED:
     * @param veranstaltung
     * @return
     */
    public String buildPUTveranstaltungJson(Veranstaltung veranstaltung)
    {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("name", veranstaltung.getName());
            jsonObj.put("von", veranstaltung.getVon());
            jsonObj.put("bis", veranstaltung.getBis());

            List<String> raumList = new ArrayList<String>();
            raumList.add(String.valueOf(veranstaltung.getRaum().getId()));
            JSONArray array = new JSONArray();
            for (int i = 0; i < raumList.size(); i++) {
                array.put(raumList.get(i));
            }
            jsonObj.put("raum", array);
            //jsonObj.put("raum", veranstaltung.getRaum());
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /**
     * sitzung POST: Diese Ressource wird benutzt, wenn eine Sitzung gestartet wird.
     * Antworten:   Status 201 CREATED: Besteht bereits eine aktive Sitzung des Benutzers, wird
     *                                  diese überschrieben und trotzdem 201 zurückgegeben! Hier
     *                                  greift also nicht „insert und update“
     * @param raumList
     * @return
     */
    public String buildPOSTsitzungJson(List<Raum> raumList)
    {
        JSONObject jsonObj = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            for (int i = 0; i < raumList.size(); i++) {
                array.put(raumList.get(i).getId());
            }
            jsonObj.put("raum", array);

        } catch(JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    //TODO buildPOSTkarteJson?


    /**
     * Diese Methode wird dazu verwendet, ein Datum aus einem Json String zu extrahieren
     * @param jsonString
     * @param metaData
     * @return
     */
    public String getFromJson(String jsonString, String metaData)
    {
        String value = null;
        if(jsonString != null){
            try {
                JSONObject jsonObj = new JSONObject(jsonString);
                value = (String)jsonObj.get(metaData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}
