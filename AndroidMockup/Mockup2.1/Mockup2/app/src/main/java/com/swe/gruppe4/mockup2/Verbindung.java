package com.swe.gruppe4.mockup2;

import com.swe.gruppe4.mockup2.Objektklassen.*;
import com.swe.gruppe4.mockup2.Objektklassen.Tag;
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
            Benutzer tmp = new Benutzer(i,"abc@def.com","Pan" + i,"Peter","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",0,0);
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
            Benutzer tmp = new Benutzer(i,"abc@def.com","Pan" + i,"Peter","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",0,0);
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
            Benutzer tmp = new Benutzer(i,"abc@def.com","Pan" + i,"Peter","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",0,0);
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
        ArrayList<Freundschaft> freunde = new ArrayList<>();
        Benutzer[] benutzer = new Benutzer[10];
        for(int i = 0; i < 3; i++){
            Benutzer tmp = new Benutzer(i,"abc@def.com","Peter","Pan","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",0,0);
            benutzer[i]=tmp;
        }
        Raum room =  new Raum(4711,"W014",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), benutzer);

        freunde.add(new Freundschaft(new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",0,0), true, room));
        freunde.add(new Freundschaft(new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",0,0), false, room));
        freunde.add(new Freundschaft(new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",0,0), true, room));
        freunde.add(new Freundschaft(new Benutzer(3,"abc@def.com","Potter 2","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",0,0), false, room));
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

    public ArrayList<com.swe.gruppe4.mockup2.Objektklassen.Tag> tagGet() {
        ArrayList<Tag> tagList = new ArrayList<>();

        //Mokupdaten, später über GET vom Server
        tagList.add(new Tag(4711,"Präsentation"));
        tagList.add(new Tag(4712,"Lernen"));
        tagList.add(new Tag(4713,"Ruhe"));

        return tagList;
    }

    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Raum benutzt
     */

    public ArrayList<Raum> raumListeGet() {
        ArrayList<Raum> raumListe = new ArrayList<>();
        Benutzer[] benutzer = new Benutzer[3];
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",0,0);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",0,0);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",0,0);


        //Mokupdaten, später über GET vom Server
        raumListe.add(new Raum(100,"G100",22,5,"",new Tag(4711,"Präsentation"),benutzer));
        raumListe.add(new Raum(101,"G101",22,3,"",new Tag(4711,"Präsentation"),benutzer));
        raumListe.add(new Raum(102,"G102",22,15,"",new Tag(4711,"Präsentation"),benutzer));
        raumListe.add(new Raum(103,"G103",22,0,"",new Tag(0,""),new Benutzer[0]));
        raumListe.add(new Raum(104,"G104",22,0,"",new Tag(0,""),new Benutzer[0]));
        raumListe.add(new Raum(105,"G105",22,22,"",new Tag(4711,"Präsentation"),benutzer));
        raumListe.add(new Raum(106,"G106",22,22,"",new Tag(4711,"Präsentation"),benutzer));
        raumListe.add(new Raum(107,"G107",22,15,"",new Tag(4711,"Präsentation"),benutzer));
        raumListe.add(new Raum(108,"G108",22,0,"",new Tag(0,""),new Benutzer[0]));

        return raumListe;
    }



    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Veranstaltung benutzt
     */
    public ArrayList<Veranstaltung> lecturesGet() {
        ArrayList<Veranstaltung> lectures = new ArrayList<Veranstaltung>();
        Veranstaltung[] veranstaltungen = new Veranstaltung[3];
        Benutzer[] benutzer = new Benutzer[5];
        Raum room =  new Raum(4711,"W014",8,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), benutzer);
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
        Raum room =  new Raum(4711,"W014",8,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.mockup2.Objektklassen.Tag(1,"Hallo Welt"), benutzer);
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