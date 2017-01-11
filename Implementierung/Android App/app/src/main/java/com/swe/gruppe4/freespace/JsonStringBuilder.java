package com.swe.gruppe4.freespace;

import com.swe.gruppe4.freespace.Objektklassen.Benutzer;
import com.swe.gruppe4.freespace.Objektklassen.Freundschaft;
import com.swe.gruppe4.freespace.Objektklassen.Raum;
import com.swe.gruppe4.freespace.Objektklassen.Tag;
import com.swe.gruppe4.freespace.Objektklassen.Veranstaltung;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.util.Log;
import java.text.DateFormat;

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
            //jsonObj.put("tokenFCM", "123abc"); //TODO Firebase Token
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
            jsonObj.put("istanonym", isAnonym);
            jsonObj.put("istpush", isPush);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        Log.d("edu", "benutzerPut with bla: " + jsonObj.toString());
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
     * @param
     * @return
     */
    public String buildPUTraumJson(int tagID)
    {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("tag", tagID);
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



            //long timevon = von.getTime()/1000;
            //long timebis = bis.getTime()/1000;




            jsonObj.put("name", name);
            jsonObj.put("von", String.valueOf(von/1000));
            jsonObj.put("bis", String.valueOf(bis/1000));
            //JSONObject raumObj = new JSONObject();
            jsonObj.put("raum", String.valueOf(raum.getId()));
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

            JSONObject raumObj = new JSONObject();

            raumObj.put("id", raum.getId());
            raumObj.put("raumnummer", raum.getRaumname());
            raumObj.put("teilnehmer_max", raum.getTeilnehmer_max());
            raumObj.put("teilnehmer_aktuell", raum.getTeilnehmer_aktuell()); // TODO teilnehmer_aktuell oder teilnehmer_akt?
            raumObj.put("status", raum.getStatus());
            raumObj.put("foto", raum.getFotoURL());

            JSONObject tagObj = new JSONObject();
            tagObj.put("id", raum.getTag().getId());
            tagObj.put("name", raum.getTag().getName());
            raumObj.put("tag", tagObj);

            JSONArray benutzerArr = new JSONArray(); // TODO Sollen überhaubt benutzer übertragen werden????
            raumObj.put("benutzer", benutzerArr);


            jsonObj.put("raum", raumObj);
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
            JSONObject raumObj = new JSONObject();
            raumObj.put("raum", String.valueOf(raumID));

            jsonObj.put("raum", String.valueOf(raumID));
            //jsonObj.put("raum", String.valueOf(raumID));

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
        //Log.d("edu","in freundschaftvonjson");

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
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
            Log.d("edu","EXCEPTION in JsonStringBuilder/getFreundschaftFromJson");
        }
        return list;


    }
    public ArrayList getTagFromJson(String jsonString) {
        ArrayList<Tag> list = new ArrayList<Tag>();
        try {

            JSONArray jsonArray = new JSONArray(jsonString);
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

    /*
    public Raum getRaumFromJson(String jsonString) {
        try {
            JSONObject jsonObj = new JSONObject(jsonString);

            return new Raum()
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("edu", "EXCEPTION in getRaum!");
        }

        return null;
    }
    */

    //jsonString muss ein JsonArray als String sein, kein JsonObjekt! / Raum OHNE Benutzer!
    public ArrayList getRaumListFromJson(String jsonString) {
        ArrayList<Raum> list = new ArrayList<Raum>();
        //Log.d("edu", "in getRaumFromJson" + jsonString);
        try {
            //JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArr = new JSONArray(jsonString);
            //JSONArray jsonArray = new JSONArray();

            //jsonObj.toJSONArray(jsonArray);
            if (jsonArr != null) {
                int len = jsonArr.length();
                //Benutzer[] benList = new Benutzer[0];
                for (int i = 0; i < len; i++) {
                    //list.add(new Raum(jsonArray.get(i).toString(),false));
                    JSONObject raumObj = jsonArr.getJSONObject(i);

                    Tag tag;

                    if(raumObj.isNull("tag")) {
                        tag = null;
                        //Log.d("edu", "RaumTEST: Tag ist NULL! bei : " + i);
                    } else {
                        JSONObject tagObj = raumObj.getJSONObject("tag");
                        tag = new Tag(tagObj.getInt("id"), tagObj.getString("name"));
                    }

                    list.add(new Raum(  raumObj.getInt("id"),
                                        raumObj.getString("raumnummer"),
                                        raumObj.getInt("teilnehmer_max"),
                                        raumObj.getInt("teilnehmer_anz"),
                                        raumObj.getString("foto"),
                                        tag,
                                        null, //Keine Benutzer????
                                        raumObj.getString("status")));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.d("edu", "EXCEPTION in JsonStringBuidler/getRaumListe!");
        }
        return list;
    }

    public ArrayList getVeranstaltungFromJson(String jsonString) {
        ArrayList<Veranstaltung> list = new ArrayList<Veranstaltung>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
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
            JSONArray jsonArray = new JSONArray(jsonString);
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

    public ArrayList getWegListFromJson(String jsonString) {
        ArrayList<String> list = new ArrayList<String>();
        //Log.d("edu", "in getRaumFromJson" + jsonString);
        try {
            //JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArr = new JSONArray(jsonString);
            //JSONArray jsonArray = new JSONArray();

            //jsonObj.toJSONArray(jsonArray);
            if (jsonArr != null) {
                int len = jsonArr.length();
                //Benutzer[] benList = new Benutzer[0];
                for (int i = 0; i < len; i++) {
                    //list.add(new Raum(jsonArray.get(i).toString(),false));
                    JSONObject wegObj = jsonArr.getJSONObject(i);

                    list.add(wegObj.getString("id"));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.d("edu", "EXCEPTION in JsonStringBuidler/getRaumListe!");
        }
        return list;
    }
}
