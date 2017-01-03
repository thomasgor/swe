package com.swe.gruppe4.freespace;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.util.SparseIntArray;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swe.gruppe4.freespace.Objektklassen.Benutzer;
import com.swe.gruppe4.freespace.Objektklassen.Freundschaft;
import com.swe.gruppe4.freespace.Objektklassen.Raum;
import com.swe.gruppe4.freespace.Objektklassen.Sitzung;
import com.swe.gruppe4.freespace.Objektklassen.Tag;
import com.swe.gruppe4.freespace.Objektklassen.Veranstaltung;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Merlin on 04.12.2016.
 */


public class RestConnection {
    private Context context;

    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";
    private static final String HTTP_PUT = "PUT";
    private static final String HTTP_DELETE = "DELETE";

    private static final String FREUNDSCHAFT = "Freundschaft";
    private static final String BENUTZER = "Benutzer";
    private static final String SITZUNG = "Sitzung";
    private static final String VERANSTALTUNG = "Veranstaltung";
    private static final String RAUM = "Raum";
    private static final String TAG = "Tag";
    private static final String KARTE = "Karte";
    JsonStringBuilder builder = new JsonStringBuilder();


    public static String id;
    public static String token;


    private static ArrayList<Tag> tagList = new ArrayList<Tag>(){{
        add(new Tag(4711,"Präsentation"));
        add(new Tag(4712,"Lernen"));
        add(new Tag(4713,"Ruhe"));
    }};

    public RestConnection(Context context) {
        this.context = context;
    }



    /**
     * Die Methode wird benutzt um REST Methoden ohne ID Parameter aufzurufen
     */
    private SparseIntArray acceptableCodes(String restRessource, String httpMethod){
        SparseIntArray res = new SparseIntArray();
        switch(restRessource){
            case FREUNDSCHAFT:
                res.append(200,0);
                break;
            case BENUTZER:
                res.append(201,0);
                switch (httpMethod){
                    case HTTP_POST:
                        res.append(200,0);
                        break;
                    case HTTP_PUT:
                        res.append(403,0);
                    default:
                        return null;
                }
                break;
            case SITZUNG:
                switch (httpMethod){
                    case HTTP_GET:
                        res.append(200,0);
                        res.append(900,0);
                        break;
                    case HTTP_POST:
                        res.append(201,0);
                        break;
                    default:
                        return null;
                }
                break;
            case VERANSTALTUNG:
                switch (httpMethod){
                    case HTTP_GET:
                        res.append(200,0);
                        break;
                    case HTTP_POST:
                        res.append(201,0);
                        res.append(910,0);
                        break;
                    default:
                        return null;
                }
                break;
            case RAUM:
                res.append(200,0);
                break;
            case TAG:
                res.append(200,0);
                break;
            case KARTE:
                switch (httpMethod){
                    case HTTP_GET:
                        break;
                    case HTTP_POST:
                        break;
                    case HTTP_PUT:
                        break;
                    case HTTP_DELETE:
                        break;
                    default:
                        return null;
                }
                break;
            default:
                return null;
        }
        return null;
    }
    private String restRequest(String restRessource, String httpMethod, String inputJson){

        String response = "false";
        try {
            java.net.URL url = new java.net.URL("http://example-server.com/" + restRessource +"/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String userPass = id+":"+token;
            byte[] encoding = Base64.decode(userPass, Base64.DEFAULT);
            conn.setRequestProperty("Authorization", "Basic " + Arrays.toString(encoding));
            conn.setRequestMethod(httpMethod);
            OutputStream os = conn.getOutputStream();

            if(!Objects.equals(inputJson, "")){
                conn.setRequestProperty("Content-Type","application/json");
                byte[] inputJsonBytes = inputJson.getBytes("UTF-8");
                os.write(inputJsonBytes);
            }
            os.flush();

            int responseCode = conn.getResponseCode();
            SparseIntArray acceptableCodes = acceptableCodes(restRessource,httpMethod);
            if(acceptableCodes != null && acceptableCodes.indexOfKey(responseCode) >= 0){
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            } else {
                showErrorMessage(responseCode);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private String restRequest(String restRessource, String httpMethod, String inputJson, int id) {
        String response = "false";
        try {
            java.net.URL url = new java.net.URL("http://example-server.com/" + restRessource +"/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String userPass = id+":"+token;
            byte[] encoding = Base64.decode(userPass, Base64.DEFAULT);
            conn.setRequestProperty("Authorization", "Basic " + Arrays.toString(encoding));
            conn.setRequestMethod(httpMethod);
            OutputStream os = conn.getOutputStream();
            //TODO: ID Mit schicken.
            if(!Objects.equals(inputJson, "")){
                conn.setRequestProperty("Content-Type","application/json");
                byte[] inputJsonBytes = inputJson.getBytes("UTF-8");
                os.write(inputJsonBytes);
            }
            os.flush();

            int responseCode = conn.getResponseCode();
            SparseIntArray acceptableCodes = acceptableCodes(restRessource,httpMethod);
            if(acceptableCodes != null && acceptableCodes.indexOfKey(responseCode) >= 0){
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            } else {
                showErrorMessage(responseCode);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }



    private void showErrorMessage(int responseCode) {

        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.errormsgTitle, responseCode))
                .setMessage("BlablablaMrFreeman")
                .setPositiveButton("Verstanden", null)
                .create()
                .show();
    }

    private String connectDUMMY(String restRessource, String httpMethod, String inputJson){

        switch(restRessource){
            case FREUNDSCHAFT:
                break;
            case BENUTZER:
                break;
            case SITZUNG:
                Benutzer[] benutzer = new Benutzer[5];
                benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","https://pbs.twimg.com/profile_images/775210778/peter_400x400.JPG", "",0,0,1);
                benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",0,0,1);
                benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",0,0,1);
                benutzer[3] = new Benutzer(4,"abc@def.com","Yoda","Meister","http://starwars.gamona.de/wp-content/gallery/yoda-bilder/13_Yoda.jpg", "",0,0,1);
                benutzer[4] = new Benutzer(5,"abc@def.com","Spock","Kommandant","https://pbs.twimg.com/profile_images/424886311078469632/8sKG_p8v_400x400.jpeg", "",0,0,1);
                //benutzer[5] = new Benutzer(6,"abc@def.com","Schnee","Jon","http://static.giantbomb.com/uploads/original/3/39164/2865551-reasons-people-love-game-thrones-jon-snow-video.jpg", "",false);

                long endzeit=(System.currentTimeMillis()/1000L)+2700;    //aktuelle Zeit + 45 Minuten
                Sitzung sitzung =  new Sitzung(4711,new Raum(100,"G102",22,6,"http://i.imgur.com/LyzIuVj.jpg", new Tag(1,"Präsentation"), benutzer, "grün"),false,endzeit);

                showErrorMessage(403);

                return new Gson().toJson(sitzung);
            case VERANSTALTUNG:
                break;
            case RAUM:
                break;
            case TAG:
                break;
            case KARTE:
                break;
            default:
                return "false";
        }

        switch(httpMethod){
            //TODO: HTTP-Builder richtige Methode geben
            case HTTP_GET:
                break;
            case HTTP_POST:
                break;
            case HTTP_PUT:
                break;
            case HTTP_DELETE:
                break;
            default:
                return "false";
        }


        return "false";
    }

    /**
     * Die Methode wird benutzt um REST Methoden mit ID Parameter aufzurufen
     */
    private String connectDUMMY(String restRessource, String httpMethod, String inputJson, int id){

        //TODO: HTTP-Builder erstellen und initialisieren

        switch(httpMethod){
            //TODO: HTTP-Builder richtige Methode geben
            case HTTP_GET:
                break;
            case HTTP_POST:
                break;
            case HTTP_PUT:
                break;
            case HTTP_DELETE:
                break;
            default:
                return "false";
        }

        switch(restRessource){
            case FREUNDSCHAFT:
                break;
            case BENUTZER:
                break;
            case SITZUNG:
                break;
            case VERANSTALTUNG:
                break;
            case RAUM:
                break;
            case TAG:
                break;
            case KARTE:
                break;
            default:
                return "false";
        }
        return "false";
    }
    private SparseIntArray acceptableCodesID(String restRessource, String httpMethod){
        SparseIntArray res = new SparseIntArray();
        switch(restRessource){
            case FREUNDSCHAFT:
                res.append(200,0);
                res.append(400,0);
                break;
            case SITZUNG:
                res.append(200,0);
                break;
            case VERANSTALTUNG:
                res.append(200,0);
                switch (httpMethod){
                    case HTTP_PUT:
                        res.append(910,0);
                }
                break;
            case RAUM:
                res.append(200,0);
                break;
            case KARTE:
                switch (httpMethod){
                    case HTTP_GET:
                        break;
                    case HTTP_POST:
                        break;
                    case HTTP_PUT:
                        break;
                    case HTTP_DELETE:
                        break;
                    default:
                        return null;
                }
                break;
            default:
                return null;
        }
        return null;
    }

    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Sitzung benutzt
     */
    public Sitzung sitzungGet(){
        String antwortJSon = restRequest(SITZUNG, HTTP_GET, null);
        //TODO: Antwort verarbeiten
        return null;

    }
    public Sitzung sitzungPost(int raumID){
        //String jSon = builder.buildPOSTsitzungJson(raumID);//TODO buildPOSTsitzung ändern
        //String antwortJSon = restRequest(SITZUNG, HTTP_POST, jSon);
        //TODO: Antwort verarbeiten
        return null;
    }

    public Sitzung sitzungPut(int id){

        String antwortJSon = restRequest(SITZUNG, HTTP_PUT, null, id);
        //TODO: Antwort verarbeiten
        return null;
    }

    public void sitzungDelete(int id){
        String antwortJSon = restRequest(SITZUNG, HTTP_DELETE, null, id);
    }

    public ArrayList<Freundschaft> freundschaftGet() {
        ArrayList<Freundschaft> freunde = new ArrayList<Freundschaft>();
        String antwortJSon = restRequest(FREUNDSCHAFT, HTTP_GET, null);
        //TODO: Antwort verarbeiten
        return freunde;
    }

    public void freundschaftPost(String email) {
        String JSon = builder.buildPOSTfreundschaftJson(email);
        String answerJSon = restRequest(FREUNDSCHAFT, HTTP_POST,JSon);



    }

    public void freundschaftPut(Benutzer benutzer){
        String answerJSon = restRequest(FREUNDSCHAFT, HTTP_PUT, benutzer.getId());


    }

    public void freundschaftDelete(Benutzer benutzer){
        String answerJSon = restRequest(FREUNDSCHAFT, HTTP_DELETE, benutzer.getId());


    }

    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Tag benutzt
     */

    public ArrayList<Tag> tagGet() {
        String antwortJSon = restRequest(TAG, HTTP_GET, null);
        //TODO: Antwort verarbeiten

        return tagList;
    }

    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Raum benutzt
     */

    public ArrayList<Raum> raumGet() {
        ArrayList<Raum> raumListe = new ArrayList<>();
        String antwortJSon = restRequest(RAUM, HTTP_GET, null);
        //TODO: Antwort verarbeiten
        return null;
    }



    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Raum(id) benutzt
     */
    public Raum raumGet(int id) {
        String antwortJSon = restRequest(RAUM, HTTP_GET, null, id);
        //TODO: Antwort verarbeiten


        return null;
    }
    public Raum raumPut(Tag tag){
        String jSon = builder.buildPUTraumJson(tag);
        String antwortJSon = restRequest(RAUM, HTTP_PUT, jSon);
        //TODO: Antwort verarbeiten



        return null;
    }

    public ArrayList<Veranstaltung> lecturesGet() {
        ArrayList<Veranstaltung> lectures = new ArrayList<Veranstaltung>();
        String antwortJSon = restRequest(VERANSTALTUNG, HTTP_GET, null);
        //TODO: Antwort verarbeiten
        return null;
    }



    public Veranstaltung lectureGet(long id){
        //String antwortJSon = restRequest(VERANSTALTUNG, HTTP_GET, null, id);//TODO: id auf INT ändern in Lecture Activities
        //TODO: Antwort verarbeiten
        return null;
    }


    public void lecturePost(String name, long von, long bis, Raum raum){
        //String jSon = builder.buildPOSTveranstaltungJson(veranstaltung); //TODO: buildPOSTveranstaltung ändern
        //String antwortJSon = restRequest(VERANSTALTUNG, HTTP_POST, jSon);
        //TODO: Antwort verarbeiten

    }

    public void lecturePut(long id, String name, long von, long bis, Raum raum){
        //String jSon = builder.buildPUTveranstaltungJson(veranstaltung);TODO: buildPUTveranstaltung ändern
        //String antwortJSon = restRequest(VERANSTALTUNG, HTTP_PUT, jSon);
        //TODO: Antwort verarbeiten

    }

    public void lectureDelete(long id){
        //String antwortJSon = restRequest(VERANSTALTUNG, HTTP_DELETE, null, id);


    }

    public Benutzer benutzerPut (String PW, int isProfessor, int isPush){

        //TODO: Daten an Server senden
        //TODO: Benutzerobjekt des eingeloggten Benutzers zurückgeben
        return new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",0,0,1);

    }

    public Benutzer benutzerPost (String id, String email, String name, String vorname, String fotoURL) {
        //TODO: Daten an Server senden
        //TODO: Benutzerobjekt des eingeloggten Benutzers zurückgeben
        //TODO: Benutzer kann neu sein oder schon bestehen

        Log.d(TAG, "UserID: " + id);
        Log.d(TAG, "email: " + email);
        Log.d(TAG, "Vorname: " + vorname);
        Log.d(TAG, "Nachname:" + name);
        Log.d(TAG, "FotoURL:" + fotoURL);

        //Hier später Serverantwort
        return new Benutzer(id, email, name, vorname, fotoURL, "servertoken" , 0, 0, 1);
    }
}
