package com.fhaachen.swe.freespace;

import com.fhaachen.swe.freespace.main.Benutzer;
import com.fhaachen.swe.freespace.main.Raum;
import com.fhaachen.swe.freespace.main.Sitzung;
import com.fhaachen.swe.freespace.main.Veranstaltung;
import org.javalite.activejdbc.LazyList;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ClientBuilder;

import java.util.HashMap;
import java.util.Map;

import static com.fhaachen.swe.freespace.main.Datenbank.connect;
import static com.fhaachen.swe.freespace.main.Datenbank.disconnect;


/**
 * Die Klasse GoogleHelper ist eine Hilfsklasse zum Kommunizieren mit FCM zum Organisieren von Push-Benachrichtigungen
 * Created by xpd on 02.01.2017.
 */
public class GoogleHelper {

    private static final String URL = "https://fcm.googleapis.com/fcm/send";
    private static final String AUTH = "key=AAAAsSJNqV4:APA91bFvIDiNxLq1cUxth2_omCbf64Sa3IuIii5koTLzpOkJEGR3XdfOxomVXBW0C1lccB3YXcxu7tALvriaLP3cJearIP4c3pUuGlRGKOEw7NiXzBJGLXY3-Lb9qWhvTzQXi2kotD3GLPleaAumCeB33fK_hJaSTA";
    //FreeSpace TestToken
    private static final String testTO = "eDi76uv21oc:APA91bGBcGxqrxC1U1FnYR5GJyiJwf4mVzCS3sbx01ERX87ePbLoI_fRnL2kX5LCcJ5PCE4F730brLsph5aaM90LkjeD4RN8Hv15dvHvKG1aWa5jr7Se4XsyZZruIUZi-jIhm_Jvi4ZS";
    //testProject
    private static final String testAUTH = "key=AAAA9gPq8VM:APA91bGXgmQjlEpt0bv8-QTBm29004NOnekWWPS0R8T6rvvPIICAuXPc4xpxJfqFWmhQesCqLXxemmhpuvEBN3ooNZZ8TDF6wZOIFEsPSUETHNlE1NO9_YtQ1T5WH6TE3deMDrJS3KV3wbyjuOWK5E8tjTk9zBhHrQ";
    private static final String testTO2 = "cpOBRNZuxtk:APA91bGRe_8hNIJr9TPAlxz7wsbIxaqwcy09XGsYg9GnrcZCB6go9IK1PV96V9PlY1t75b7aZHDgcbmMao-ToVFBA24itGNUMAENLrW1m6ZN5l_ktRyGP8StBTg3K3RH39-UxTTalyRA";

    private static final String bodySitzung = "5minutes";
    private static final String bodyVeranstaltung = "roomBlocked";
    private static final long min5 = 300000;
    private static final long min1 = 60000;

    private sitzungThread runnableS = new sitzungThread();
    private veranstaltungThread runnableV = new veranstaltungThread();

    public GoogleHelper() {
        System.out.println("Starting GoogleHelperThreads.");

        Thread t1 = new Thread(this.runnableS);
        Thread t2 = new Thread(this.runnableV);
        t1.start();
        t2.start();
    }

    public void stop () {
        this.runnableS.stop();
        this.runnableV.stop();
    }

    class sitzungThread implements Runnable {
        private boolean active;
        public sitzungThread(){
            this.active = true;
        }
        public void run() {
            System.out.println("Start sitzungThread");
            while (true) {
                if (!this.active) {
                    System.out.println("End sitzungThread");
                    break;
                }
                checkDB();
                try {
                    Thread.sleep(min1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public void stop(){
            this.active = false;
        }
        private void checkDB() {
            connect();
            try {
                LazyList<Sitzung> list = Sitzung.find("notifySent=?", false);
                long curTime = System.currentTimeMillis();
                for (Sitzung s: list) {
                    long endzeit = Long.parseLong(s.get("endzeit").toString(),10);
                    if (endzeit > (curTime/1000L)
                            && endzeit < ((curTime + min5) / 1000L)) {
                        Benutzer b = Benutzer.findById(s.get("benutzer"));
                        if ("1".equals(b.get("istPush").toString())) {
                            erstelleNotfiyThread(b.get("tokenFCM").toString(), bodySitzung);
                            s.set("notifySent", true).saveIt();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            disconnect();
        }
    }

    class veranstaltungThread implements Runnable {
        private boolean active;
        public veranstaltungThread() {
            this.active = true;
        }
        public void run() {
            System.out.println("Start veranstaltungThread");
            //while (true) {
            if (!this.active) {
                System.out.println("End veranstaltungThread");
                //break;
            }
            checkDB();
            try {
                //zum testen
                Thread.sleep(3000);
                //Thread.sleep(min1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //}
        }
        public void stop(){
            this.active = false;
        }
        private void checkDB(){
            connect();
            long curTime = System.currentTimeMillis();
            try {
                LazyList<Veranstaltung> listV = Veranstaltung.find("notifySent=?", false);
                for (Veranstaltung v: listV) {
                    long begin = Long.parseLong(v.get("von").toString(),10);
                    if (begin > (curTime/1000L)
                            && begin < ((curTime + min5) / 1000L)) {
                        LazyList<Sitzung> listS = Sitzung.find("raum=?", v.get("raum"));
                        for (Sitzung s: listS) {
                            Benutzer b = Benutzer.findById(s.get("benutzer"));
                            if ("1".equals(b.get("istPush").toString())) {
                                erstelleNotfiyThread(b.get("tokenFCM").toString(), bodyVeranstaltung);
                            }
                        }
                        v.set("notifySent",true).saveIt();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            disconnect();
        }
    }

    synchronized void erstelleNotfiyThread(String token, String body) {
        notifyThreads runnable = new notifyThreads(token, body);
        Thread t = new Thread(runnable);
        t.start();
    }

    class notifyThreads implements Runnable {

        private String body;
        private String token;
        private String json;

        notifyThreads(String token, String body){
            this.token = token;
            this.body = body;
        }

        public void run(){
            getJsonString();
            sendToFCM();
        }

        synchronized void getJsonString() {
            Map<String, Object> notify = new HashMap<String, Object>();
            notify.put("body", body);

            String to = this.token;
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("to",to);
            param.put("notification", notify);
            this.json = JsonHelper.getJsonStringFromMap(param);
        }
        synchronized void sendToFCM() {
            Response response = ClientBuilder.newClient()
                    .target(URL)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization",AUTH)
                    .post(Entity.json(this.json));

            System.out.println("Status: " + response.getStatus());
            System.out.println("Response: " + response.readEntity(String.class));
        }
    }
}

