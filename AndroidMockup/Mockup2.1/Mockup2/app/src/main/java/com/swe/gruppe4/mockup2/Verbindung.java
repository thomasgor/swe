package com.swe.gruppe4.mockup2;

import com.swe.gruppe4.mockup2.Objektklassen.*;
import java.util.ArrayList;

import java.util.ArrayList;

/**
 * Created by Merlin on 04.12.2016.
 */


public class Verbindung {




    /**
     * Die folgenden Methoden werden für die Sitzung benutzt
     */
    public Sitzung sitzungGet(){
        //DUMMY! Später dann vom Server holen.
        Benutzer[] benutzer = new Benutzer[3];
        for(int i = 0; i < 3; i++){
            Benutzer tmp = new Benutzer(i,"abc@def.com","Pan" + i,"Peter","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",false);
            benutzer[i]=tmp;
        }

        long endzeit=(System.currentTimeMillis()/1000L)+2700;    //aktuelle Zeit + 45 Minuten
        return new Sitzung(4711,new Raum(4711,"W014",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), benutzer),false,endzeit);

        //TODO: Daten vom Server statt DummyDaten

    }
    public Sitzung sitzungPost(int raumID){
        //TODO: Daten an Server senden
        //DUMMY! Später dann vom Server holen.
        Benutzer[] benutzer = new Benutzer[3];
        for(int i = 0; i < 3; i++){
            Benutzer tmp = new Benutzer(i,"abc@def.com","Pan" + i,"Peter","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",false);
            benutzer[i]=tmp;
        }

        long endzeit=(System.currentTimeMillis()/1000L)+2700;    //aktuelle Zeit + 45 Minuten
        return new Sitzung(4711,new Raum(4711,"W014",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), benutzer),false,endzeit);

        //TODO: Daten vom Server statt DummyDaten
    }

    public Sitzung sitzungPut(int id){
        //TODO: Daten an Server senden
        //DUMMY! Später dann vom Server holen.
        Benutzer[] benutzer = new Benutzer[3];
        for(int i = 0; i < 3; i++){
            Benutzer tmp = new Benutzer(i,"abc@def.com","Pan" + i,"Peter","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",false);
            benutzer[i]=tmp;
        }

        long endzeit=(System.currentTimeMillis()/1000L)+2700;    //aktuelle Zeit + 45 Minuten
        return new Sitzung(4711,new Raum(4711,"W014",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), benutzer),true,endzeit);
        //TODO: Daten vom Server statt DummyDaten
    }

    public void sitzungDelete(int id){
        //TODO: Daten an Server senden
    }
	
	public ArrayList<Freundschaft> freundschaftGet() {
        ArrayList<Freundschaft> freunde = new ArrayList<Freundschaft>();
        Benutzer[] benutzer = new Benutzer[10];
        for(int i = 0; i < 3; i++){
            Benutzer tmp = new Benutzer(i,"abc@def.com","Peter","Pan","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",false);
            benutzer[i]=tmp;
        }
        Raum room =  new Raum(4711,"W014",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), benutzer);

        freunde.add(new Freundschaft(new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",false), true, room));
        freunde.add(new Freundschaft(new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",false), false, room));
        freunde.add(new Freundschaft(new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",false), true, room));
        freunde.add(new Freundschaft(new Benutzer(3,"abc@def.com","Potter 2","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",false), false, room));
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
}
