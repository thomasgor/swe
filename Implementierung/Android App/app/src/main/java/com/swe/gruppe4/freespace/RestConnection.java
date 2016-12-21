package com.swe.gruppe4.freespace;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
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
                benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","https://pbs.twimg.com/profile_images/775210778/peter_400x400.JPG", "",0,0);
                benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",0,0);
                benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",0,0);
                benutzer[3] = new Benutzer(4,"abc@def.com","Yoda","Meister","http://starwars.gamona.de/wp-content/gallery/yoda-bilder/13_Yoda.jpg", "",0,0);
                benutzer[4] = new Benutzer(5,"abc@def.com","Spock","Kommandant","https://pbs.twimg.com/profile_images/424886311078469632/8sKG_p8v_400x400.jpeg", "",0,0);
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
        //DUMMY! Später dann vom Server holen.
        return new Gson().fromJson(connectDUMMY(SITZUNG, HTTP_GET, ""),Sitzung.class);
        //TODO: Daten vom Server statt DummyDaten

    }
    public Sitzung sitzungPost(int raumID){
        //TODO: Daten an Server senden
        //DUMMY! Später dann vom Server holen.
        Benutzer[] benutzer = new Benutzer[3];
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",0,0);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",0,0);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",0,0);

        long endzeit=(System.currentTimeMillis()/1000L)+2700;    //aktuelle Zeit + 45 Minuten
        return new Sitzung(4711,new Raum(raumID,"W014",8,5,"http://i.imgur.com/LyzIuVj.jpg", new Tag(1,"Lernen"), benutzer, "gelb"),false,endzeit);

        //TODO: Daten vom Server statt DummyDaten
    }

    public Sitzung sitzungPut(int id){

        ArrayList<Raum> raume = new ArrayList<>(this.raumGet());

        for(int i = 0; i < raume.size(); i++) {
            if(100 == raume.get(i).getId()) {
                Raum meinRaum = new Raum(raume.get(i).getId(),
                        raume.get(i).getRaumname(),
                        raume.get(i).getTeilnehmer_max(),
                        raume.get(i).getTeilnehmer_aktuell(),
                        raume.get(i).getFotoURL(),
                        raume.get(i).getTag(),
                        raume.get(i).getBenutzer(),
                        raume.get(i).getStatus());
                return new Sitzung(4711,meinRaum,true,(System.currentTimeMillis()/1000L)+2700);
                //TODO: Daten vom Server statt DummyDaten
            }

        }

        //TODO: Daten an Server senden
        //DUMMY! Später dann vom Server holen.
        Benutzer[] benutzer = new Benutzer[6];
        /*for(int i = 0; i < 3; i++){
            Benutzer tmp = new Benutzer(i,"abc@def.com","Pan" + i,"Peter","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",false);
            benutzer[i]=tmp;
        }*/
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","https://pbs.twimg.com/profile_images/775210778/peter_400x400.JPG", "",0,0);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",0,0);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",0,0);
        benutzer[3] = new Benutzer(4,"abc@def.com","Yoda","Meister","http://starwars.gamona.de/wp-content/gallery/yoda-bilder/13_Yoda.jpg", "",0,0);
        benutzer[4] = new Benutzer(5,"abc@def.com","Spock","Kommandant","https://pbs.twimg.com/profile_images/424886311078469632/8sKG_p8v_400x400.jpeg", "",0,0);
        benutzer[5] = new Benutzer(6,"abc@def.com","Schnee","Jon","http://static.giantbomb.com/uploads/original/3/39164/2865551-reasons-people-love-game-thrones-jon-snow-video.jpg", "",0,0);

        long endzeit=(System.currentTimeMillis()/1000L)+2700;    //aktuelle Zeit + 45 Minuten
        return new Sitzung(4711,new Raum(4711,"W014",8,7,"http://i.imgur.com/LyzIuVj.jpg", new Tag(1,"Präsentation"), benutzer, "gelb"),true,endzeit);
        //TODO: Daten vom Server statt DummyDaten
    }

    public void sitzungDelete(int id){
        //TODO: Daten an Server senden
    }

    public ArrayList<Freundschaft> freundschaftGet() {
        ArrayList<Freundschaft> freunde = new ArrayList<Freundschaft>();
        Benutzer[] benutzer = new Benutzer[10];
        for(int i = 0; i < 3; i++){
            Benutzer tmp = new Benutzer(i,"abc@def.com","Peter","Pan","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",0,0);
            benutzer[i]=tmp;
        }
        Raum room = new Raum(101,"G101",22,3,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Präsentation"),benutzer, "grün");
        Raum room2 = new Raum(4711,"G102",22,15,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Ruhe"),benutzer, "gelb");

        freunde.add(new Freundschaft(new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",0,0), true, room));
        freunde.add(new Freundschaft(new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",0,0), false, room));
        freunde.add(new Freundschaft(new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",0,0), true, room2));
        freunde.add(new Freundschaft(new Benutzer(3,"abc@def.com","Yoda","Meister","http://starwars.gamona.de/wp-content/gallery/yoda-bilder/13_Yoda.jpg", "",0,0), false, room));
        return freunde;
    }
    //TODO: Daten vom Server statt DummyDaten

    public void freundschaftPost(String email) {
        //TODO: Daten an Server senden

    }

    public void freundschaftPut(Benutzer benutzer, boolean accepted){
        //TODO: Daten an Server senden

    }

    public void freundschaftDelete(Benutzer benutzer){
        //TODO: Daten an Server senden

    }

    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Tag benutzt
     */

    public ArrayList<Tag> tagGet() {


        //Mokupdaten, später über GET vom Server
        return tagList;
    }

    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Raum benutzt
     */

    public ArrayList<Raum> raumGet() {
        ArrayList<Raum> raumListe = new ArrayList<>();
        Benutzer[] benutzer = new Benutzer[6];
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","https://pbs.twimg.com/profile_images/775210778/peter_400x400.JPG", "",0,0);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",0,0);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",0,0);
        benutzer[3] = new Benutzer(4,"abc@def.com","Yoda","Meister","http://starwars.gamona.de/wp-content/gallery/yoda-bilder/13_Yoda.jpg", "",0,0);
        benutzer[4] = new Benutzer(5,"abc@def.com","Spock","Kommandant","https://pbs.twimg.com/profile_images/424886311078469632/8sKG_p8v_400x400.jpeg", "",0,0);
        benutzer[5] = new Benutzer(6,"abc@def.com","Schnee","Jon","http://static.giantbomb.com/uploads/original/3/39164/2865551-reasons-people-love-game-thrones-jon-snow-video.jpg", "",0,0);


        //Mokupdaten, später über GET vom Server
        raumListe.add(new Raum(100,"G100",22,6,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Präsentation"),benutzer,"grün"));
        raumListe.add(new Raum(101,"G101",22,3,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Präsentation"),benutzer,"grün"));
        raumListe.add(new Raum(4711,"G102",22,15,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Ruhe"),benutzer,"gelb"));
        raumListe.add(new Raum(103,"G103",22,0,"http://i.imgur.com/LyzIuVj.jpg",new Tag(0,""),new Benutzer[0],"grün"));
        raumListe.add(new Raum(104,"G104",22,0,"http://i.imgur.com/LyzIuVj.jpg",new Tag(0,""),new Benutzer[0],"grün"));
        raumListe.add(new Raum(4711,"G105",22,22,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Präsentation"),benutzer,"rot"));
        raumListe.add(new Raum(106,"G106",22,22,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Präsentation"),benutzer,"rot"));
        raumListe.add(new Raum(107,"G107",22,15,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Präsentation"),benutzer,"gelb"));
        raumListe.add(new Raum(108,"G108",22,0,"http://i.imgur.com/LyzIuVj.jpg",new Tag(0,""),new Benutzer[0],"grün"));

        return raumListe;
    }

    /*
    public Raum raumGet(int id){
        //TODO: Daten an Server senden
        //DUMMY! Später dann vom Server holen.
        Benutzer[] benutzer = new Benutzer[3];
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",false);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",false);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",false);

        return new Raum(id,"W014",8,5,"http://i.imgur.com/LyzIuVj.jpg", new Tag(1,"Hallo Welt"), benutzer);

        //TODO: Daten vom Server statt DummyDaten
    }
    */

    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Raum(id) benutzt
     */
    public Raum raumGet(int id) {
        //TODO: Daten an Server senden
        //Mokupdaten, später über GET vom Server
        ArrayList<Raum> raume = new ArrayList<>(this.raumGet());
        //Raum(int id, String raumname, int teilnehmer_max, int teilnehmer_aktuell, String fotoURL, Tag tag, Benutzer[] benutzer)

        Object meinRaum = new Object();
        for(int i = 0; i < raume.size(); i++) {
            if(id == raume.get(i).getId()) {
                meinRaum = new Raum(raume.get(i).getId(),
                        raume.get(i).getRaumname(),
                        raume.get(i).getTeilnehmer_max(),
                        raume.get(i).getTeilnehmer_aktuell(),
                        raume.get(i).getFotoURL(),
                        raume.get(i).getTag(),
                        raume.get(i).getBenutzer(),
                        raume.get(i).getStatus());
                return (Raum)meinRaum;
                //TODO: Daten vom Server statt DummyDaten
            }

        }
        return null;
    }
    public Raum raumPut(int tagID, int raumID){
        //TODO: Daten an Server senden
        //DUMMY! Später dann vom Server holen.

        ArrayList<Raum> raume = new ArrayList<>(this.raumGet());
        Tag tag = null;
        for (Tag tmp : tagList){
            if(tagID == tmp.getId()){
                tag = tmp;
                break;
            }
        }
        if(tag == null){
            return null;
        }

        for(int i = 0; i < raume.size(); i++) {
            if(raumID == raume.get(i).getId()) {
                Raum meinRaum = new Raum(raume.get(i).getId(),
                        raume.get(i).getRaumname(),
                        raume.get(i).getTeilnehmer_max(),
                        raume.get(i).getTeilnehmer_aktuell(),
                        raume.get(i).getFotoURL(),
                        tag,
                        raume.get(i).getBenutzer(),
                        raume.get(i).getStatus());

                return meinRaum;
                //TODO: Daten vom Server statt DummyDaten
            }

        }

        return null;

        //TODO: Daten vom Server statt DummyDaten
    }

    public ArrayList<Veranstaltung> lecturesGet() {
        ArrayList<Veranstaltung> lectures = new ArrayList<Veranstaltung>();
        Veranstaltung[] veranstaltungen = new Veranstaltung[3];
        Benutzer[] benutzer = new Benutzer[5];
        Raum room =  new Raum(4711,"W014",8,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.freespace.Objektklassen.Tag(1,"Hallo Welt"), benutzer, "gelb");
        for(int i = 0; i < 3; i++){
            Veranstaltung tmp = new Veranstaltung(i,"SWE Veranstaltung", new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",0,0), 1293840001000L, 1481546700000L,room );
            lectures.add(i, tmp);
        }
        //TODO: Daten vom Server statt DummyDaten

        return lectures;
    }



    public Veranstaltung lectureGet(long id){
        //TODO: Daten vom Server holen
        Benutzer[] benutzer = new Benutzer[5];
        Raum room =  new Raum(4711,"W014",8,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.freespace.Objektklassen.Tag(1,"Hallo Welt"), benutzer, "grün");
        Veranstaltung veranstaltung = new Veranstaltung(1,"SWE Veranstaltung", new Benutzer(1,"abc@def.com","Pan","Prof","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",0,0), 1293840001000L, 1481546700000L,room);
        return veranstaltung;
    }


    public void lecturePost(String name, long von, long bis, Raum raum){
        //TODO: Daten an Server senden

    }

    public void lecturePut(long id, String name, long von, long bis, Raum raum){
        //TODO: Daten an Server senden

    }

    public void lectureDelete(long id){
        //TODO: Daten an Server senden

    }

    public Benutzer benutzerPut (String PW){

        //TODO: Daten an Server senden
        //TODO: Benutzerobjekt des eingeloggten Benutzers zurückgeben
        return new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",0,0);

    }
}
