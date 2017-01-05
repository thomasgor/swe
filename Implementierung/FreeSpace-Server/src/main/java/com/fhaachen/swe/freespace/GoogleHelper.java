package com.fhaachen.swe.freespace;

import com.fhaachen.swe.freespace.main.Benutzer;
import com.fhaachen.swe.freespace.main.Sitzung;
import org.javalite.activejdbc.LazyList;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ClientBuilder;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.fhaachen.swe.freespace.main.Datenbank.connect;
import static com.fhaachen.swe.freespace.main.Datenbank.disconnect;


/**
 * Die Klasse GoogleHelper ist eine Hilfsklasse zum Kommunizieren mit FCM zum Organisieren von Push-Benachrichtigungen
 * Created by xpd on 02.01.2017.
 */
public class GoogleHelper implements Runnable{

    private static final String URL = "https://fcm.googleapis.com/fcm/send";
    private static final String AUTH = "";

    private static final String testAUTH = "key=AAAA9gPq8VM:APA91bGXgmQjlEpt0bv8-QTBm29004NOnekWWPS0R8T6rvvPIICAuXPc4xpxJfqFWmhQesCqLXxemmhpuvEBN3ooNZZ8TDF6wZOIFEsPSUETHNlE1NO9_YtQ1T5WH6TE3deMDrJS3KV3wbyjuOWK5E8tjTk9zBhHrQ";
    private static final String testTO = "cpOBRNZuxtk:APA91bGRe_8hNIJr9TPAlxz7wsbIxaqwcy09XGsYg9GnrcZCB6go9IK1PV96V9PlY1t75b7aZHDgcbmMao-ToVFBA24itGNUMAENLrW1m6ZN5l_ktRyGP8StBTg3K3RH39-UxTTalyRA";

    private static final String titleSitzung = "Achtung: Deine Sitzung läuft bald ab!";
    private static final String bodySitzung = "Denk daran, Sie zum Beispiel zu verlängern! :)";

    private static final long min5 = 300000;
    private boolean active;

    public GoogleHelper() {
        this.active = true;
    }

    public void run() {
        System.out.println("Start run ThreadGoogleHelper");
        //while (true) {
            if (!this.active) {
                System.out.println("End Loop");
                //break;
            }
            checkDB();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        //}
        System.out.println("End run ThreadGoogleHelper");
    }

    public void stop () {
        System.out.println("Set ThreadGoogleHelper.active = false");
        this.active = false;
    }

    public void checkDB() {
        System.out.println("Start checkDB");
        connect();
        long time = (System.currentTimeMillis() + min5) / 1000L;
        try {
            long anz = Sitzung.count("sendNotify=?",false);
            System.out.println("anz: " + anz);
            LazyList<Sitzung> list = Sitzung.find("sendNotify=?", false);
            for (Sitzung s: list) {
                long endzeit = Long.parseLong(s.get("endzeit").toString(),10);
                System.out.println(endzeit);
                if (endzeit > (System.currentTimeMillis()/1000L)
                        && !Boolean.getBoolean(s.get("sendNotify").toString())) {
                    Benutzer b = Benutzer.findById(s.get("benutzer"));
                    notifyThreads runnable = new notifyThreads(b,titleSitzung, bodySitzung);
                    Thread t = new Thread(runnable);
                    t.setName(s.get("benutzer").toString() + "Thread");
                    t.start();
                    s.set("sendNotify",true).saveIt();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();
        System.out.println("End checkDB");
    }

    class notifyThreads implements Runnable {

        private Benutzer b;
        private String title;
        private String body;
        private String json;

        notifyThreads(Benutzer b, String title, String body){
            this.b = b;
            this.title = title;
            this.body = body;
        }

        public void run(){
            System.out.println("start notifyThread run");
            getJsonString();
            sendToFCM();
            System.out.println("end notifyThread run");
        }

        synchronized void getJsonString() {
            System.out.println("getJsonString");
            Map<String, Object> notify = new HashMap<String, Object>();
            notify.put("body", body);
            notify.put("title", title);

            String to = this.b.get("token").toString();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("to",testTO);
            param.put("notification", notify);
            this.json = JsonHelper.getJsonStringFromMap(param);
        }

        synchronized void sendToFCM() {
            System.out.println("sendToFCM");
            Response response = ClientBuilder.newClient()
                    .target(URL)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization",testAUTH)
                    .post(Entity.json(this.json));

            if (response.getStatus() != 200) {
                System.out.println("Status: " + response.getStatus());
                System.out.println("Response: " + response.readEntity(String.class));
            }
        }
    }

    public static void main (String[] args) {
        System.out.println(
                (System.currentTimeMillis()+min5)/1000L
                );
    }
}

