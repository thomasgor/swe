package com.swe.gruppe4.freespace;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Base64.*;
import android.util.Base64;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swe.gruppe4.freespace.Objektklassen.AktuellerBenutzer;
import com.swe.gruppe4.freespace.Objektklassen.Benutzer;
import com.swe.gruppe4.freespace.Objektklassen.Freundschaft;
import com.swe.gruppe4.freespace.Objektklassen.Raum;
import com.swe.gruppe4.freespace.Objektklassen.Sitzung;
import com.swe.gruppe4.freespace.Objektklassen.Tag;
import com.swe.gruppe4.freespace.Objektklassen.Veranstaltung;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.lang.Object;
import android.os.AsyncTask;
import 	java.io.BufferedOutputStream;




/**
 * Created by Merlin on 04.12.2016.
 */


public class RestConnection {
    private Context context;

    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";
    private static final String HTTP_PUT = "PUT";
    private static final String HTTP_DELETE = "DELETE";

    private static final String FREUNDSCHAFT = "freundschaft";
    private static final String BENUTZER = "benutzer";
    private static final String SITZUNG = "sitzung";
    private static final String VERANSTALTUNG = "veranstaltung";
    private static final String RAUM = "raum";
    private static final String TAG = "tag";
    private static final String KARTE = "karte";
    JsonStringBuilder builder = new JsonStringBuilder();



    public static String id;
    public static String token;
    private ProgressDialog mProgressDialog;


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
                res.append(200,0);
                switch (httpMethod){
                    case HTTP_POST:
                        res.append(201,0);
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
        return res;
    }
    private String restRequest(final String restRessource, final String httpMethod, final String input){
        String res = "";
        class RestCon extends AsyncTask<String,Integer,String> {
            String resp;

            protected String doInBackground(String... params) {
                String response = "false";
                publishProgress(0);
                try {
                    java.net.URL url = new java.net.URL("http://192.168.178.31:8888/" + restRessource + "/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    String userPass = id+":"+token;
                    String encoding = Base64.encodeToString(userPass.getBytes(), Base64.DEFAULT);
                    conn.setRequestProperty("Authorization", "Basic " + encoding);
                    conn.setRequestMethod(httpMethod);
                    conn.setChunkedStreamingMode(0);
                    conn.setRequestProperty("Content-Type","application/json");
                    publishProgress(0);
                    OutputStream os = conn.getOutputStream();
                    publishProgress(0);
                    if(!Objects.equals(input, "")){
                        byte[] inputJsonBytes = input.getBytes("UTF-8");
                        os.write(inputJsonBytes);
                    }
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    Log.d("myTag3",""+ responseCode);
                    SparseIntArray acceptableCodes = acceptableCodes(restRessource,httpMethod);
                    if(acceptableCodes != null && acceptableCodes.indexOfKey(responseCode) >= 0){
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                        in.close();
                    } else {
                        //showErrorMessage(responseCode);
                    }
                    conn.disconnect();


                } catch (IOException e) {
                    Log.d("myTag20","fail");
                    e.printStackTrace();
                }
                Log.d("myTag2",response);
                return response;

            }

            protected void onProgressUpdate(Integer... progress) {
                showProgressDialog();
            }

            protected void onPostExecute(String result) {
                hideProgressDialog();
                resp = result;


            }

        }

        RestCon connn = new RestCon();
        try {
            res = connn.execute(input).get();
        }catch (Exception e)
        {}

        return res;


    }

    private String restRequest(final String restRessource, final String httpMethod, final String input, final String idn) {
        String res = "";
        class RestCon extends AsyncTask<String, Integer, String> {
            String resp;

            protected String doInBackground(String... params) {
                String response = "false";
                publishProgress(0);
                try {
                    java.net.URL url = new java.net.URL("http://192.168.178.31:8888/" + restRessource + "/" + idn);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    String userPass = id + ":" + token;
                    String encoding = Base64.encodeToString(userPass.getBytes(), Base64.DEFAULT);
                    conn.setRequestProperty("Authorization", "Basic " + encoding);
                    conn.setRequestMethod(httpMethod);
                    conn.setChunkedStreamingMode(0);
                    conn.setRequestProperty("Content-Type", "application/json");
                    Log.d("myTag3", "now aftersetRequestMethod");
                    OutputStream os = conn.getOutputStream();
                    Log.d("myTag5", "now before if");
                    if (!Objects.equals(input, "")) {
                        byte[] inputJsonBytes = input.getBytes("UTF-8");
                        os.write(inputJsonBytes);
                        Log.d("myTag6", "now in if");
                    }
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    Log.d("myTag3", "" + responseCode);
                    SparseIntArray acceptableCodes = acceptableCodesID(restRessource, httpMethod);
                    if (acceptableCodes != null && acceptableCodes.indexOfKey(responseCode) >= 0) {
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                        in.close();
                    } else {
                        //showErrorMessage(responseCode);
                    }
                    conn.disconnect();


                } catch (IOException e) {
                    Log.d("myTag20", "fail");
                    e.printStackTrace();
                }
                Log.d("myTag2", response);
                return response;

            }
            protected void onProgressUpdate(Integer... progress) {
                showProgressDialog();
            }

            protected void onPostExecute(String result) {
                //hideProgressDialog();
                resp = result;


            }
        }
        RestCon connn = new RestCon();
        try {
            res = connn.execute(input).get();
        }catch (Exception e)
        {}

        return res;
    }

    private String restRequest(final String restRessource, final String httpMethod, String input, final int idn) {
        String res = "";
        class RestCon extends AsyncTask<String,Integer,String> {
            String resp;

            protected String doInBackground(String... params) {
                String response = "false";
                publishProgress(0);
                try {
                    java.net.URL url = new java.net.URL("http://192.168.178.31:8888/" + restRessource +"/" + idn + "/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    String userPass = id+":"+token;
                    String encoding = Base64.encodeToString(userPass.getBytes(), Base64.DEFAULT);
                    conn.setRequestProperty("Authorization", "Basic " + encoding);
                    conn.setRequestMethod(httpMethod);
                    conn.setChunkedStreamingMode(0);
                    Log.d("myTag3","now aftersetRequestMethod");
                    conn.setRequestProperty("Content-Type","application/json");
                    OutputStream os = conn.getOutputStream();
                    Log.d("myTag5","now before if");
                    if(!Objects.equals(params[0], "")){
                        byte[] inputJsonBytes = params[0].getBytes("UTF-8");
                        os.write(inputJsonBytes);
                    }
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    SparseIntArray acceptableCodes = acceptableCodesID(restRessource,httpMethod);
                    if(acceptableCodes != null && acceptableCodes.indexOfKey(responseCode) >= 0){
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                        in.close();
                    } else {
                        //showErrorMessage(responseCode);
                    }
                    conn.disconnect();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("myTag2",response);
                return response;

            }

            protected void onProgressUpdate(Integer... progress) {
                showProgressDialog();
            }

            protected void onPostExecute(String result) {
                //hideProgressDialog();
                resp = result;


            }

        }

        RestCon connn = new RestCon();
        try {
            res = connn.execute(input).get();
        }catch (Exception e)
        {}

        return res;


    }

    private String restPOSTBenutzer(final String input) {
        String res = "";


        class RestCon extends AsyncTask<String,Integer,String> {
             String resp;

            protected String doInBackground(String... params) {
                String response = "false";
                publishProgress(0);

                try {
                    java.net.URL url = new java.net.URL("http://192.168.178.31:8888/" + BENUTZER +"/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //String userPass = ""+":"+"";
                    //byte[] encoding = Base64.decode(userPass, Base64.DEFAULT);
                    //conn.setRequestProperty("Authorization", "Basic " + Arrays.toString(encoding));
                    conn.setRequestMethod(HTTP_POST);
                    conn.setChunkedStreamingMode(0);
                    conn.setRequestProperty("Content-Type","application/json");
                    OutputStream os = conn.getOutputStream();
                    if(!Objects.equals(input, "")){
                        byte[] inputJsonBytes = input.getBytes("UTF-8");
                        os.write(inputJsonBytes);
                    }
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    Log.d("myTag3",""+ responseCode);
                    SparseIntArray acceptableCodes = acceptableCodes(BENUTZER,HTTP_POST);
                    if(acceptableCodes != null && acceptableCodes.indexOfKey(responseCode) >= 0){
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                        in.close();
                    } else {
                        //showErrorMessage(responseCode);
                    }
                    conn.disconnect();


                } catch (IOException e) {
                    Log.d("myTag20","fail");
                    e.printStackTrace();
                }

                Log.d("myTag2",response);
                return response;

            }

            protected void onProgressUpdate(Integer... progress) {
                showProgressDialog();
            }

            protected void onPostExecute(String result) {
                //hideProgressDialog();
                resp = result;


            }

        }

        RestCon connn = new RestCon();
        try {
            res = connn.execute(input).get();
        }catch (Exception e)
        {}

        return res;


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
            case BENUTZER:
                res.append(200,0);
                res.append(201,0);
                res.append(403,0);
                break;
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
        return res;
    }

    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Sitzung benutzt
     */
    public Sitzung sitzungGet(){
        String antwortJSon = restRequest(SITZUNG, HTTP_GET, null);
        return new Sitzung(antwortJSon);

    }
    public Sitzung sitzungPost(int raumID){
        String jSon = builder.buildPOSTsitzungJson(raumID);
        String antwortJSon = restRequest(SITZUNG, HTTP_POST, jSon);
        return new Sitzung(antwortJSon);
    }

    public Sitzung sitzungPut(int id){

        String antwortJSon = restRequest(SITZUNG, HTTP_PUT, "", id);
        return new Sitzung(antwortJSon);
    }

    public void sitzungDelete(int id){
        String antwortJSon = restRequest(SITZUNG, HTTP_DELETE, "", id);
    }

    public ArrayList<Freundschaft> freundschaftGet() {
        String antwortJSon = restRequest(FREUNDSCHAFT, HTTP_GET, "");
        return builder.getFreundschaftFromJson(antwortJSon);

        /*ArrayList<Freundschaft> freunde = new ArrayList<Freundschaft>();
        Benutzer[] benutzer = new Benutzer[10];
        for(int i = 0; i < 3; i++){
            Benutzer tmp = new Benutzer(i,"abc@def.com","Peter","Pan","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",0, 0, 0);
            benutzer[i]=tmp;
        }
        Raum room = new Raum(101,"G101",22,3,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Präsentation"),benutzer, "grün");
        Raum room2 = new Raum(4711,"G102",22,15,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Ruhe"),benutzer, "gelb");

        freunde.add(new Freundschaft(new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",0, 0, 0), true, room));
        freunde.add(new Freundschaft(new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",0, 0, 0), false, room));
        freunde.add(new Freundschaft(new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",0, 0, 0), true, room2));
        freunde.add(new Freundschaft(new Benutzer(3,"abc@def.com","Yoda","Meister","http://starwars.gamona.de/wp-content/gallery/yoda-bilder/13_Yoda.jpg", "",0, 0, 0), false, room));
        return freunde;*/
    }

    public void freundschaftPost(String email) {
        String JSon = builder.buildPOSTfreundschaftJson(email);
        String answerJSon = restRequest(FREUNDSCHAFT, HTTP_POST,JSon);



    }

    public void freundschaftPut(Benutzer benutzer){
        String answerJSon = restRequest(FREUNDSCHAFT, HTTP_PUT, "", benutzer.getId());


    }

    public void freundschaftDelete(Benutzer benutzer){
        String answerJSon = restRequest(FREUNDSCHAFT, HTTP_DELETE, "", benutzer.getId());


    }

    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Tag benutzt
     */

    public ArrayList<Tag> tagGet() {
        String antwortJSon = restRequest(TAG, HTTP_GET, "");
        return builder.getTagFromJson(antwortJSon);

    }

    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Raum benutzt
     */

    public ArrayList<Raum> raumGet() {
        String antwortJSon = restRequest(RAUM, HTTP_GET, "");
        return builder.getRaumFromJson(antwortJSon);
    }



    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Raum(id) benutzt
     */
    public Raum raumGet(int id) {
        String antwortJSon = restRequest(RAUM, HTTP_GET, "", id);
        return new Raum(antwortJSon,true);


    }
    public Raum raumPut(Tag tag){
        String jSon = builder.buildPUTraumJson(tag);
        String antwortJSon = restRequest(RAUM, HTTP_PUT, jSon);

        return new Raum(antwortJSon,false);


    }

    public ArrayList<Veranstaltung> lecturesGet() {
        String antwortJSon = restRequest(VERANSTALTUNG, HTTP_GET, "");
        return builder.getVeranstaltungFromJson(antwortJSon);

    }



    public Veranstaltung lectureGet(int id){
        String antwortJSon = restRequest(VERANSTALTUNG, HTTP_GET, "", id);

        //return new Veranstaltung(antwortJSon);
        return null;
    }


    public void lecturePost(String name, long von, long bis, Raum raum){
        String jSon = builder.buildPOSTveranstaltungJson(name, von, bis, raum);
        String antwortJSon = restRequest(VERANSTALTUNG, HTTP_POST, jSon);


    }

    public void lecturePut(int id, String name, long von, long bis, Raum raum){
        String jSon = builder.buildPUTveranstaltungJson(name, von, bis, raum);
        String antwortJSon = restRequest(VERANSTALTUNG, HTTP_PUT, jSon);


    }

    public void lectureDelete(int id){
        String antwortJSon = restRequest(VERANSTALTUNG, HTTP_DELETE,"", id);


    }

    public Benutzer benutzerPut (String PW, int isAnonym, int isPush){
        String jSON = builder.buildPUTbenutzerJson(PW, isAnonym, isPush);
        String antwortJSon = restRequest(BENUTZER, HTTP_PUT, jSON);

        return new Benutzer(antwortJSon);

    }

    public void benutzerPost (String idn, String email, String name, String vorname, String fotoURL) {
        String jSon = builder.buildPOSTbenutzerJson(idn, email, name, vorname, fotoURL);

        String antwortJSon = restPOSTBenutzer(jSon);

        token = builder.getFromJson(antwortJSon, "token");
        id = builder.getFromJson(antwortJSon, "id");
        AktuellerBenutzer.setAktuellerBenutzer(new Benutzer(antwortJSon));
        Log.d("myTag" ,token + " " + id);




    }

    private void showProgressDialog() {
        if(mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Lade...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    /**
     * Entfernt Progressdialog, wenn vorhanden
     */
    private void hideProgressDialog() {
        if(mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

}
