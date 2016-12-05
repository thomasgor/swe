package com.swe.gruppe4.mockup2;

import com.swe.gruppe4.mockup2.Objektklassen.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Merlin on 04.12.2016.
 */

public class Verbindung {





    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Raum benutzt
     */
    public ArrayList<Raum> raumGet() {
        ArrayList<Raum> raeume = new ArrayList<Raum>();
        Benutzer[] benutzer = new Benutzer[3];
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",false);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",false);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",false);
        Raum room =  new Raum(4711,"W009",6,1,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), Arrays.copyOfRange(benutzer,0,0));
        raeume.add(room);
        room =  new Raum(4712,"W010",6,2,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), new Benutzer[0]);
        raeume.add(room);
        room =  new Raum(4713,"W011",6,3,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), benutzer);
        raeume.add(room);
        room =  new Raum(4714,"W012",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), Arrays.copyOfRange(benutzer,0,1));
        raeume.add(room);
        room =  new Raum(4715,"W013",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), Arrays.copyOfRange(benutzer,1,2));
        raeume.add(room);
        room =  new Raum(4716,"W014",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), benutzer);
        raeume.add(room);
        room =  new Raum(4717,"W015",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), benutzer);
        raeume.add(room);


        return raeume;

        //TODO: Daten vom Server statt DummyDaten
    }

    public Raum raumGet(int id){
        //TODO: Daten an Server senden
        //DUMMY! Später dann vom Server holen.
        Benutzer[] benutzer = new Benutzer[3];
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",false);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",false);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",false);

        return new Raum(id,"W014",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), benutzer);

        //TODO: Daten vom Server statt DummyDaten
    }

    public Raum raumPut(int tagID){
        //TODO: Daten an Server senden
        //DUMMY! Später dann vom Server holen.
        Benutzer[] benutzer = new Benutzer[3];
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",false);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",false);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",false);
        return new Raum(4711,"W014",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(tagID,"Ruhe"), benutzer);

        //TODO: Daten vom Server statt DummyDaten
    }



    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Sitzung benutzt
     */
    public Sitzung sitzungGet(){
        //DUMMY! Später dann vom Server holen.
        Benutzer[] benutzer = new Benutzer[3];
        /*for(int i = 0; i < 3; i++){
            Benutzer tmp = new Benutzer(i,"abc@def.com","Pan" + i,"Peter","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",false);
            benutzer[i]=tmp;
        }*/
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",false);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",false);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",false);


        long endzeit=(System.currentTimeMillis()/1000L)+2700;    //aktuelle Zeit + 45 Minuten
        return new Sitzung(4711,new Raum(4711,"W014",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Lernen"), benutzer),false,endzeit);

        //TODO: Daten vom Server statt DummyDaten

    }
    public Sitzung sitzungPost(int raumID){
        //TODO: Daten an Server senden
        //DUMMY! Später dann vom Server holen.
        Benutzer[] benutzer = new Benutzer[3];
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",false);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",false);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",false);

        long endzeit=(System.currentTimeMillis()/1000L)+2700;    //aktuelle Zeit + 45 Minuten
        return new Sitzung(4711,new Raum(raumID,"W014",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Lernen"), benutzer),false,endzeit);

        //TODO: Daten vom Server statt DummyDaten
    }

    public Sitzung sitzungPut(int id){
        //TODO: Daten an Server senden
        //DUMMY! Später dann vom Server holen.
        Benutzer[] benutzer = new Benutzer[6];
        /*for(int i = 0; i < 3; i++){
            Benutzer tmp = new Benutzer(i,"abc@def.com","Pan" + i,"Peter","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",false);
            benutzer[i]=tmp;
        }*/
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","https://pbs.twimg.com/profile_images/775210778/peter_400x400.JPG", "",false);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",false);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",false);
        benutzer[3] = new Benutzer(4,"abc@def.com","Yoda","Meister","http://starwars.gamona.de/wp-content/gallery/yoda-bilder/13_Yoda.jpg", "",false);
        benutzer[4] = new Benutzer(5,"abc@def.com","Spock","Captain","https://pbs.twimg.com/profile_images/424886311078469632/8sKG_p8v_400x400.jpeg", "",false);
        benutzer[5] = new Benutzer(6,"abc@def.com","Schnee","Jon","http://static.giantbomb.com/uploads/original/3/39164/2865551-reasons-people-love-game-thrones-jon-snow-video.jpg", "",false);

        long endzeit=(System.currentTimeMillis()/1000L)+2700;    //aktuelle Zeit + 45 Minuten
        return new Sitzung(4711,new Raum(4711,"W014",6,6,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), benutzer),true,endzeit);
        //TODO: Daten vom Server statt DummyDaten
    }

    public void sitzungDelete(int id){
        //TODO: Daten an Server senden
    }




    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Freundschaft benutzt
     */
	public ArrayList<Freundschaft> freundschaftGet() {
        ArrayList<Freundschaft> freunde = new ArrayList<Freundschaft>();
        Benutzer[] benutzer = new Benutzer[3];
        for(int i = 0; i < 3; i++){
            Benutzer tmp = new Benutzer(i,"abc@def.com","Peter","Pan","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",false);
            benutzer[i]=tmp;
        }
        Raum room =  new Raum(4711,"W014",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), benutzer);

        freunde.add(new Freundschaft(new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",false), true, room));
        freunde.add(new Freundschaft(new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",false), true, room));
        freunde.add(new Freundschaft(new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",false), true, room));
        return freunde;
    }
    //TODO: Daten vom Server statt DummyDaten
}
