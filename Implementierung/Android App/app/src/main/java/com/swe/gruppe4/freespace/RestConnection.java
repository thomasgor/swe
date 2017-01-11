package com.swe.gruppe4.freespace;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.Base64.*;
import android.util.Base64;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import com.swe.gruppe4.freespace.Objektklassen.*;





import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    private static final String WEG = "weg";
    JsonStringBuilder builder = new JsonStringBuilder();



    public static String id;
    public static String token;
    public static int lastStatusCode = 0;
    private ProgressDialog mProgressDialog;

    // Hostname und Port des Servers
    // TODO Hostname anpassen
    //private final String hostname = "192.168.178.36";
    private final String hostname = "mtn-it.de";
    private final String port = "8888";

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
            case WEG:
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

    private String doRestRequest(final String restRessource, final String httpMethod, final String outputJson, final String value, final boolean expectResponseJson, final boolean isAuthorized) {
        String res = "";
        //showProgressDialog();
        class RestCon extends AsyncTask<String, Integer, String> {
            String resp;

            protected String doInBackground(String... params) {
                String response = "false";
                publishProgress(0);
                try {
                    java.net.URL url = new java.net.URL("http://" + hostname + ":" + port + "/" + restRessource + "/" + value);
                    Log.d("edu", "doRestRequest URL: " + url.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    if(isAuthorized) {
                        String userPass = id + ":" + token;
                        String encoding = Base64.encodeToString(userPass.getBytes(), Base64.DEFAULT);
                        conn.setRequestProperty("Authorization", "Basic " + encoding);
                    }
                    conn.setRequestMethod(httpMethod);
                    Log.d("edu","doRestRequest Method: " + conn.getRequestMethod());
                    if(!httpMethod.equals(HTTP_DELETE)) {
                        conn.setChunkedStreamingMode(0);
                    }

	                if(!httpMethod.equals(HTTP_GET) && !httpMethod.equals(HTTP_DELETE)) {
                        conn.setRequestProperty("Content-Type", "application/json");
		            }

                    if(!Objects.equals(outputJson, "")) {
                        OutputStream os = conn.getOutputStream();
                        byte[] inputJsonBytes = outputJson.getBytes("UTF-8");
                        os.write(inputJsonBytes);
                        os.flush();
                        os.close();
                    }
                    else {
                        conn.connect();
                    }

                    //Log.d("edu","Output Objekt: " + new Gson().toJson(conn));

                    int responseCode = conn.getResponseCode();
                    lastStatusCode = responseCode;
                    Log.d("edu", "doRestRequest ResponseCode: " + responseCode);
                    SparseIntArray acceptableCodes = acceptableCodesID(restRessource, httpMethod);
                    if (acceptableCodes != null && acceptableCodes.indexOfKey(responseCode) >= 0 && expectResponseJson) {
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                        in.close();
                    } else {
                        Log.d("edu","Responsecode unbekannt");
                        //showErrorMessage(responseCode);
                    }
                    conn.disconnect();


                } catch (IOException e) {
                    Log.d("edu", "EXCEPTION IN doRestRequest!");
                    e.printStackTrace();
                }

                return response;

            }
            protected void onProgressUpdate(Integer... progress) {
                //showProgressDialog();
            }

            protected void onPostExecute(String result) {
                hideProgressDialog();
                resp = result;


            }
        }
        RestCon connn = new RestCon();
        try {
            res = connn.execute(outputJson).get();
        }catch (Exception e)
        {}


        if(res.equals("false")){
            //showErrorMessage(500);
        }
        return res;
    }

    private String restRequest(final String restRessource, final String httpMethod, final String input) {
        String res = "";
        class RestCon extends AsyncTask<String, Integer, String> {
            String resp;

            protected String doInBackground(String... params) {
                String response = "false";
                publishProgress(0);
                try {
                    java.net.URL url = new java.net.URL("http://" + hostname + ":" + port + "/" + restRessource + "/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    String userPass = id + ":" + token;
                    String encoding = Base64.encodeToString(userPass.getBytes(), Base64.DEFAULT);
                    conn.setRequestProperty("Authorization", "Basic " + encoding);
                    conn.setRequestMethod(httpMethod);
                    conn.setChunkedStreamingMode(0);
                    conn.setRequestProperty("Content-Type", "application/json");

                    if(httpMethod != HTTP_GET) {
                        OutputStream os = conn.getOutputStream();

                        if (!Objects.equals(input, "")) {
                            byte[] inputJsonBytes = input.getBytes("UTF-8");
                            os.write(inputJsonBytes);

                        }
                        os.flush();
                        os.close();
                    }
                    else
                    conn.connect();

                    //Log.d("edu", "restRequest! versendet");
                    int responseCode = conn.getResponseCode();
                    Log.d("edu", "restRequest! ResponseCode: " + responseCode);
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
                    Log.d("edu", "EXCEPTION IN restRequest!");
                    e.printStackTrace();
                }
                Log.d("myTag2", response);
                //Log.d("edu", "das ist der Response: " + response);
                return response;

            }
            protected void onProgressUpdate(Integer... progress) {
                //showProgressDialog();
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

                    java.net.URL url = new java.net.URL("http://" + hostname + ":" + port + "/" + restRessource + "/" + idn);


                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    String userPass = id + ":" + token;
                    String encoding = Base64.encodeToString(userPass.getBytes(), Base64.DEFAULT);
                    conn.setRequestProperty("Authorization", "Basic " + encoding);
                    conn.setRequestMethod(httpMethod);
                    conn.setChunkedStreamingMode(0);
                    conn.setRequestProperty("Content-Type", "application/json");
                    Log.d("myTag3", "now aftersetRequestMethod");
                    Log.d("edu", "RestRequest a");

                    if(httpMethod != HTTP_GET && httpMethod != HTTP_DELETE) {
                        OutputStream os = conn.getOutputStream();

                        if (!Objects.equals(input, "")) {
                            byte[] inputJsonBytes = input.getBytes("UTF-8");
                            os.write(inputJsonBytes);

                        }
                        os.flush();
                        os.close();
                    }
                    else
                        conn.connect();

                    /*
                    OutputStream os = conn.getOutputStream();

                    Log.d("myTag5", "now before if");
                    if (!Objects.equals(input, "")) {
                        byte[] inputJsonBytes = input.getBytes("UTF-8");
                        os.write(inputJsonBytes);
                        Log.d("myTag6", "now in if");
                    }
                    os.flush();
                    os.close();
                    */

                    int responseCode = conn.getResponseCode();
                    Log.d("edu", "RestRequest Response Code: " + responseCode);
                    Log.d("myTag3", "" + responseCode);
                    SparseIntArray acceptableCodes = acceptableCodesID(restRessource, httpMethod);

                    if (acceptableCodes != null && acceptableCodes.indexOfKey(responseCode) >= 0 && responseCode != 400) {
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                        in.close();
                    } else {
                        //showErrorMessage(responseCode);
                    }
                    conn.disconnect();


                } catch (IOException e) {
                    Log.d("myTag20", "fail");
                    Log.d("edu", "EXCEPTIOn in RestRequest");
                    e.printStackTrace();
                }
                Log.d("myTag2", response);

                return response;

            }
            protected void onProgressUpdate(Integer... progress) {
                //showProgressDialog();
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

    private String restRequest(final String restRessource, final String httpMethod, final String input, final int idn) {
        String res = "";
        class RestCon extends AsyncTask<String,Integer,String> {
            String resp;

            protected String doInBackground(String... params) {
                String response = "false";
                publishProgress(0);
                try {
                    java.net.URL url = new java.net.URL("http://" + hostname + ":" + port + "/" + restRessource +"/" + idn);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    String userPass = id+":"+token;
                    String encoding = Base64.encodeToString(userPass.getBytes(), Base64.DEFAULT);
                    conn.setRequestProperty("Authorization", "Basic " + encoding);
                    conn.setRequestMethod(httpMethod);
                    conn.setChunkedStreamingMode(0);
                    Log.d("myTag3","now aftersetRequestMethod");
                    conn.setRequestProperty("Content-Type","application/json");
                    //OutputStream os = conn.getOutputStream();
                    if(httpMethod != HTTP_GET) {
                        OutputStream os = conn.getOutputStream();

                        if (!Objects.equals(input, "")) {
                            byte[] inputJsonBytes = input.getBytes("UTF-8");
                            os.write(inputJsonBytes);

                        }
                        os.flush();
                        os.close();
                    }
                    else
                        conn.connect();
                    //conn.connect();
                    Log.d("myTag5","now before if");

                    /*
                    if(!Objects.equals(params[0], "")){
                        byte[] inputJsonBytes = params[0].getBytes("UTF-8");
                        os.write(inputJsonBytes);
                    }
                    os.flush();
                    os.close();
                    */

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
                //showProgressDialog();
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

    private String restPOSTBenutzer(final String input) {
        String res = "";

        class RestCon extends AsyncTask<String,Integer,String> {
             String resp;

            protected String doInBackground(String... params) {
                String response = "false";
                publishProgress(0);

                try {
                    java.net.URL url = new java.net.URL("http://" + hostname + ":" + port + "/" + BENUTZER +"/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //String userPass = ""+":"+"";
                    //byte[] encoding = Base64.decode(userPass, Base64.DEFAULT);
                    //conn.setRequestProperty("Authorization", "Basic " + Arrays.toString(encoding));
                    conn.setRequestMethod(HTTP_POST);
                    conn.setChunkedStreamingMode(0);
                    conn.setRequestProperty("Content-Type","application/json");
                    OutputStream os = conn.getOutputStream();
                    Log.d("myTag", "conn aufgebaut");
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



    private void showErrorMessage(final int responseCode) {

        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.errormsgTitle, responseCode))
                .setMessage("Leider ist ein Fehler aufgetreten.\nBitte starten Sie die App neu.")
                .setPositiveButton("Verstanden", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
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
            case WEG:
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
            case WEG:
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
            case TAG:
                res.append(200,0);
                break;
            case WEG:
                switch (httpMethod){
                    case HTTP_GET:
                        res.append(200,0);
                        res.append(400,0);
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
        Log.d("edu", "sitzungGet Request..");
        //String antwortJSon = restRequest(SITZUNG, HTTP_GET, null);
        String antwortJSon = doRestRequest(SITZUNG, HTTP_GET, "", "", true, true);
        Log.d("edu", "sitzungGet Response: " + antwortJSon);

        if(antwortJSon == "false") {
            return null;
        } else {
            return new Sitzung(antwortJSon);
        }


    }
    public Sitzung sitzungPost(int raumID){
        String jSon = builder.buildPOSTsitzungJson(raumID);



        Log.d("edu", "sitzungPost Request.." + jSon);
        //String antwortJSon = restRequest(SITZUNG, HTTP_POST, jSon);
        String antwortJSon = doRestRequest(SITZUNG, HTTP_POST, jSon, "", true, true);
        Log.d("edu", "sitzungPost Response: " + antwortJSon);

        if(antwortJSon == "false") {
            return null;
        } else {
            return new Sitzung(antwortJSon);
        }

    }

    public Sitzung sitzungPut(String id){
        Log.d("edu", "sitzungPut Request..");
        //String antwortJSon = restRequest(SITZUNG, HTTP_PUT, "", id);
        String antwortJSon = doRestRequest(SITZUNG, HTTP_PUT, "", id, true, true);
        Log.d("edu", "sitzungPut Response: " + antwortJSon);
        return new Sitzung(antwortJSon);
    }

    public void sitzungDelete(final String id){

        Log.d("edu", "sitzungDelete Request..");

        /*class RestCon extends AsyncTask<String, Integer, String> {
            String resp;

            protected String doInBackground(String... params) {
                String response = "false";
                publishProgress(0);
                try {
                    java.net.URL url = new java.net.URL("http://" + hostname + ":" + port + "/" + SITZUNG + "/" + id);
                    Log.d("edu", "doRestRequest URL: " + url.toString());

                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                    String userPass = id + ":" + token;
                    String encoding = Base64.encodeToString(userPass.getBytes(), Base64.DEFAULT);
                    httpCon.setRequestProperty("Authorization", "Basic " + encoding);
                    //httpCon.setDoOutput(true);
                    //httpCon.setRequestProperty(
                    //        "Content-Type", "application/x-www-form-urlencoded" );
                    httpCon.setRequestMethod("DELETE");
                    httpCon.connect();


                    int responseCode = httpCon.getResponseCode();
                    lastStatusCode = responseCode;
                    Log.d("edu","doRestRequest" + HTTP_DELETE +" Response: " + responseCode);



                } catch (IOException e) {
                    Log.d("edu", "EXCEPTION IN doRestRequest!");
                    e.printStackTrace();
                }

                return response;

            }
            protected void onProgressUpdate(Integer... progress) {
                //showProgressDialog();
            }

            protected void onPostExecute(String result) {
                hideProgressDialog();
                resp = result;


            }
        }

        RestCon con = new RestCon();
        con.execute("");*/
        //String antwortJSon = restRequest(SITZUNG, HTTP_DELETE, "", id);
        String antwortJSon = doRestRequest(SITZUNG, HTTP_DELETE, "", id, false, true);
        Log.d("edu", "sitzungDelete Response: " + antwortJSon);
    }

    public ArrayList<Freundschaft> freundschaftGet() {
        Log.d("edu", "FreundschaftGet(liste) Request..");
        //String antwortJSon = restRequest(FREUNDSCHAFT, HTTP_GET, "");
        String antwortJSon = doRestRequest(FREUNDSCHAFT, HTTP_GET, "", "", true, true);
        Log.d("edu", "FreundschaftGet(liste) Response: " + antwortJSon);
        return builder.getFreundschaftFromJson(antwortJSon);
    }



    public void freundschaftPost(String email) {
        String JSon = builder.buildPOSTfreundschaftJson(email);

        Log.d("edu", "freundschaftPost Request.." + email);
        //String answerJSon = restRequest(FREUNDSCHAFT, HTTP_POST,JSon);
        String antwortJSon = doRestRequest(FREUNDSCHAFT, HTTP_POST, JSon, "", false, true);
        Log.d("edu", "freundschaftPost Response: " + antwortJSon);
    }

    public void freundschaftPut(Benutzer benutzer){

        Log.d("edu", "freundschaftPut Request..");
        //String answerJSon = restRequest(FREUNDSCHAFT, HTTP_PUT, "", benutzer.getId());
        String antwortJSon = doRestRequest(FREUNDSCHAFT, HTTP_PUT, "", benutzer.getId(), false, true);
        Log.d("edu", "freundschaftPut Response: " + antwortJSon);
    }

    public void freundschaftDelete(Benutzer benutzer){
        Log.d("edu", "freundschaftDelete Request..");
        //String antwortJSon = restRequest(FREUNDSCHAFT, HTTP_DELETE, "", benutzer.getId());
        String antwortJSon = doRestRequest(FREUNDSCHAFT, HTTP_DELETE, "", benutzer.getId(), false, true);
        Log.d("edu", "freundschaftDelete Response: " + antwortJSon);

    }

    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Tag benutzt
     */

    public ArrayList<Tag> tagGet() {
        Log.d("edu", "tagGet(liste) Request..");
        //String antwortJSon = restRequest(TAG, HTTP_GET, "");
        String antwortJSon = doRestRequest(TAG, HTTP_GET, "", "", true, true);
        Log.d("edu", "tagGet(liste) Response: " + antwortJSon);
        return builder.getTagFromJson(antwortJSon);

    }

    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Raum benutzt
     */

    public ArrayList<Raum> raumGet() {
        Log.d("edu", "raumGet(liste) Request..");
        //String antwortJSon = restRequest(RAUM, HTTP_GET, "");
        String antwortJSon = doRestRequest(RAUM, HTTP_GET, "", "", true, true);
        Log.d("edu", "raumGet(liste) Response: " + antwortJSon);
        return builder.getRaumListFromJson(antwortJSon);
    }



    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Raum(id) benutzt
     */
    public Raum raumGet(int id) {
        Log.d("edu", "raumGet(id) Request..");
        //String antwortJSon = restRequest(RAUM, HTTP_GET, "", id);
        String antwortJSon = doRestRequest(RAUM, HTTP_GET, "", String.valueOf(id), true, true);
        Log.d("edu", "raumGet(id) Response: " + antwortJSon);
        if(antwortJSon == "false") { // kein raum mit der id gefunden :code 400
            return null;
        } else {
            return new Raum(antwortJSon,true);
        }

    }
    public Raum raumPut(int tagID, int raumID){
        String jSon = builder.buildPUTraumJson(tagID);

        Log.d("edu", "raumPut Request...");
        //String antwortJSon = restRequest(RAUM, HTTP_PUT, jSon);
        String antwortJSon = doRestRequest(RAUM, HTTP_PUT, jSon, String.valueOf(raumID), true, true);
        Log.d("edu", "raumPut Response: " + antwortJSon);

        return new Raum(antwortJSon,false);
    }

    // Wird wahrscheinlich nicht gebraucht, da die Foto URL im Raumobjekt gespeichert ist. Siehe raumGet(int id)
    public String raumGetFoto(int raumId) {
        String antwortJSon = doRestRequest(RAUM, HTTP_GET, "", String.valueOf(raumId) + "/foto", true, true);
        return antwortJSon; // Bild ULR?
    }
    public ArrayList<Veranstaltung> lecturesGet() {

        Log.d("edu", "lectureGet Request...");
        //String antwortJSon = restRequest(VERANSTALTUNG, HTTP_GET, "");
        String antwortJSon = doRestRequest(VERANSTALTUNG, HTTP_GET, "", "", true, true);
        Log.d("edu", "lecturesGet Response: " + antwortJSon);

        return builder.getVeranstaltungFromJson(antwortJSon);

    }



    public Veranstaltung lectureGet(int id){

        Log.d("edu", "lectureGet(id) Request...");
        //String antwortJSon = restRequest(VERANSTALTUNG, HTTP_GET, "", id);
        String antwortJSon = doRestRequest(VERANSTALTUNG, HTTP_GET, "", String.valueOf(id), true, true);
        Log.d("edu", "lectureGet(id) Response ACHTUNG RETURN NULL!: " + antwortJSon);
        return new Veranstaltung(antwortJSon);

    }


    public boolean lecturePost(String name, long von, long bis, Raum raum){
        String jSon = builder.buildPOSTveranstaltungJson(name, von, bis, raum);

        Log.d("edu", "lecturePost Request..." + jSon);
        //String antwortJSon = restRequest(VERANSTALTUNG, HTTP_POST, jSon);
        String antwortJSon = doRestRequest(VERANSTALTUNG, HTTP_POST, jSon, "", true, true);
        Log.d("edu", "lecturePost Response: " + antwortJSon);
        if(antwortJSon.equals("false")){
            return false;
        }
        return true;

    }

    public boolean lecturePut(int id, String name, long von, long bis, Raum raum){
        String jSon = builder.buildPUTveranstaltungJson(name, von, bis, raum);

        Log.d("edu", "lecturePut Request... with id:" + id);
        //String antwortJSon = restRequest(VERANSTALTUNG, HTTP_PUT, jSon);
        String antwortJSon = doRestRequest(VERANSTALTUNG, HTTP_PUT, jSon, String.valueOf(id), true, true);
        Log.d("edu", "lecturePut Response: " + antwortJSon);
        if(antwortJSon.equals("false")){
            return false;
        }
        return true;

    }

    public void lectureDelete(int id){

        Log.d("edu", "lectureDelete Request... with id:" + id);
        //String antwortJSon = restRequest(VERANSTALTUNG, HTTP_DELETE,"", id);
        String antwortJSon = doRestRequest(VERANSTALTUNG, HTTP_DELETE, "", String.valueOf(id), false, true);
        Log.d("edu", "lectureDelete Response: " + antwortJSon);
    }

    public Benutzer benutzerPut (String PW, int isAnonym, int isPush){
        String jSON = builder.buildPUTbenutzerJson(PW, isAnonym, isPush);

        Log.d("edu", "benutzerPut Request...with:" + PW + isAnonym + isPush);
        //String antwortJSon = restRequest(BENUTZER, HTTP_PUT, jSON);
        String antwortJSon = doRestRequest(BENUTZER, HTTP_PUT, jSON, "", true, true);
        Log.d("edu", "benutzerPut Response: " + antwortJSon);

        return new Benutzer(antwortJSon);

    }

    public void benutzerPut (String token){
        JSONObject json = new JSONObject();
        try {
            json.put("tokenfcm",token);
            String antwortJSon = doRestRequest(BENUTZER, HTTP_PUT, json.toString(), "", true, true);
            Log.d("edu", "benutzerPut Response: " + antwortJSon);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("edu", "benutzerPut Request to send Token...with:" + token);
        //String antwortJSon = restRequest(BENUTZER, HTTP_PUT, jSON);

    }

    public void benutzerPost (String idn, String email, String name, String vorname, String fotoURL) {
        String jSon = builder.buildPOSTbenutzerJson(idn, email, name, vorname, fotoURL);

        Log.d("edu", "benutzerPost Request...");
        //String antwortJSon = restPOSTBenutzer(jSon);
        String antwortJSon = doRestRequest(BENUTZER, HTTP_POST, jSon, "", true, false);
        Log.d("edu", "benutzerPost Response: " + antwortJSon);

        token = builder.getFromJson(antwortJSon, "token");
        id = builder.getFromJson(antwortJSon, "id");
        AktuellerBenutzer.setAktuellerBenutzer(new Benutzer(antwortJSon));
        Log.d("myTag" ,token + " " + id);

    }

    public Karte karteGet (int start, int ziel){

        return null;
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

    public ArrayList<String> wegGet(String start, String ende) {
        String antwortJSon = doRestRequest(WEG, HTTP_GET, "",  (start+"/"+ende), true, true);
        if(antwortJSon == "false") { // kein raum mit der id gefunden :code 400
            return null;
        } else {
            return builder.getWegListFromJson(antwortJSon);
        }

    }

    public ArrayList<RoomEnterance> raumEingangGet() {
        Karte roomEnterance = new Karte();
        roomEnterance.addRoomEnterance(1111,"G1111",(float)1.3,700);
        roomEnterance.addRoomEnterance(1110,"G1110",(float)1.3,550);
        roomEnterance.addRoomEnterance(5,"G111",(float)1.3,550);
        roomEnterance.addRoomEnterance(7,"G116",(float)2.5,700);
        roomEnterance.addRoomEnterance(6,"G115",(float)2,750);
        roomEnterance.addRoomEnterance(4,"G112",(float)2.5,550);
        roomEnterance.addRoomEnterance(1071,"G1071",(float)1.3,380);
        roomEnterance.addRoomEnterance(1070,"G1070",(float)1.3,240);
        roomEnterance.addRoomEnterance(3,"G107",(float)1.3,240);
        roomEnterance.addRoomEnterance(2,"G102",(float)2.5,240);
        roomEnterance.addRoomEnterance(1,"G101",(float)2,200);
        return roomEnterance.getRooms();
    }


}
