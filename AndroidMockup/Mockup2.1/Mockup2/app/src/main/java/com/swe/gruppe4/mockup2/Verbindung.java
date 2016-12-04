package com.swe.gruppe4.mockup2;

import com.swe.gruppe4.mockup2.Objektklassen.*;

/**
 * Created by Merlin on 04.12.2016.
 */

public class Verbindung {



    public Sitzung getSitzung(){
        //DUMMY! Später dann vom Server holen.
        Benutzer[] benutzer = new Benutzer[10];
        for(int i = 0; i < 3; i++){
            Benutzer tmp = new Benutzer(i,"abc@def.com","Peter","Pan","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",false);
            benutzer[i]=tmp;
        }

        long endzeit=(System.currentTimeMillis()/1000L)+2700;    //aktuelle Zeit + 45 Minuten
        return new Sitzung(4711,new Raum(4711,"W014",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), benutzer),false,endzeit);

        //TODO: Daten vom Server statt DummyDaten

    }
}
