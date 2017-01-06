package com.swe.gruppe4.freespace;

import com.swe.gruppe4.freespace.Objektklassen.Benutzer;
import com.swe.gruppe4.freespace.Objektklassen.Freundschaft;
import com.swe.gruppe4.freespace.Objektklassen.Raum;
import com.swe.gruppe4.freespace.Objektklassen.Tag;
import com.swe.gruppe4.freespace.Objektklassen.Veranstaltung;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

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
    public String buildPOSTbenutzerJson(String id, String email, String name, String vorname, String fotoURL)
    {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("id", id);
            jsonObj.put("email", email);
            jsonObj.put("name", name);
            jsonObj.put("vorname", vorname);
            jsonObj.put("foto", fotoURL);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /**
     * benutzer PUT: Die Ressource wird benutzt, wenn das Masterpasswort eingegeben wurde
     * Antworten:   Status 200 OK: Masterpasswort korrekt
     *              Status 403 FORBIDDEN: Masterpasswort falsch
     * @param
     * @param password
     * @return
     */
    public String buildPUTbenutzerJson(String password,int isAnonym, int isPush)
    {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("masterpasswort", password);
            jsonObj.put("istAnonym", isAnonym);
            jsonObj.put("istPush", isPush);
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
            jsonObj.put("email", email);
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
     * @param
     * @return
     */
    public String buildPOSTveranstaltungJson(String name, long von, long bis, Raum raum)
    {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("name", name);
            jsonObj.put("von", von);
            jsonObj.put("bis", bis);
            jsonObj.put("raum", raum);
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
     * @param
     * @return
     */
    public String buildPUTveranstaltungJson(String name, long von, long bis, Raum raum)
    {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("name", name);
            jsonObj.put("von", von);
            jsonObj.put("bis", bis);
            jsonObj.put("raum", raum);
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
     * @param
     * @return
     */
    public String buildPOSTsitzungJson(int raumID)
    {
        JSONObject jsonObj = new JSONObject();
        try {

            jsonObj.put("raum", raumID);

        } catch(JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    //TODO buildPOSTkarteJson?


    /**
     * Diese Methode wird dazu verwendet, ein Datum aus einem Json String zu extrahieren
     * @param jsonString
     * @param
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
    public ArrayList getFreundschaftFromJson(String jsonString) {
        ArrayList<Freundschaft> list = new ArrayList<Freundschaft>();
        Log.d("mylog24342","in freundschaftvonjson");
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = new JSONArray();
            jsonObj.toJSONArray(jsonArray);
            Log.d("mylog24342","" + jsonArray);
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    list.add(new Freundschaft(jsonArray.get(i).toString()));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return list;


    }
    public ArrayList getTagFromJson(String jsonString) {
        ArrayList<Tag> list = new ArrayList<Tag>();
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = new JSONArray();
            jsonObj.toJSONArray(jsonArray);
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    list.add(new Tag((jsonArray.get(i).toString())));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return list;


    }

    public ArrayList getRaumFromJson(String jsonString) {
        ArrayList<Raum> list = new ArrayList<Raum>();
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = new JSONArray();
            jsonObj.toJSONArray(jsonArray);
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    list.add(new Raum(jsonArray.get(i).toString(),false));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return list;


    }

    public ArrayList getVeranstaltungFromJson(String jsonString) {
        ArrayList<Veranstaltung> list = new ArrayList<Veranstaltung>();
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = new JSONArray();
            jsonObj.toJSONArray(jsonArray);
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    list.add(new Veranstaltung (jsonArray.get(i).toString()));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return list;


    }

    public ArrayList getBenutzerFromJson(String jsonString) {
        ArrayList<Benutzer> list = new ArrayList<Benutzer>();
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = new JSONArray();
            jsonObj.toJSONArray(jsonArray);
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    list.add(new Benutzer (jsonArray.get(i).toString()));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return list;


    }
}
