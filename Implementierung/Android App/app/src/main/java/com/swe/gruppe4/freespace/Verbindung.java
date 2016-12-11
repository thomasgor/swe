package com.swe.gruppe4.freespace;

import com.swe.gruppe4.freespace.Objektklassen.*;
import com.swe.gruppe4.freespace.Objektklassen.Tag;
import java.util.ArrayList;

/**
 * Created by Merlin on 04.12.2016.
 */


public class Verbindung {




    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Sitzung benutzt
     */
    public Sitzung sitzungGet(){
        //DUMMY! Später dann vom Server holen.
        Benutzer[] benutzer = new Benutzer[5];
        /*for(int i = 0; i < 3; i++){
            Benutzer tmp = new Benutzer(i,"abc@def.com","Pan" + i,"Peter","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg", "",false);
            benutzer[i]=tmp;
        }*/
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","https://pbs.twimg.com/profile_images/775210778/peter_400x400.JPG", "",false);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",false);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",false);
        benutzer[3] = new Benutzer(4,"abc@def.com","Yoda","Meister","http://starwars.gamona.de/wp-content/gallery/yoda-bilder/13_Yoda.jpg", "",false);
        benutzer[4] = new Benutzer(5,"abc@def.com","Spock","Kommandant","https://pbs.twimg.com/profile_images/424886311078469632/8sKG_p8v_400x400.jpeg", "",false);
        //benutzer[5] = new Benutzer(6,"abc@def.com","Schnee","Jon","http://static.giantbomb.com/uploads/original/3/39164/2865551-reasons-people-love-game-thrones-jon-snow-video.jpg", "",false);

        long endzeit=(System.currentTimeMillis()/1000L)+2700;    //aktuelle Zeit + 45 Minuten
        return new Sitzung(4711,new Raum(4711,"W014",8,6,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.freespace.Objektklassen.Tag(1,"Präsentation"), benutzer),false,endzeit);
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
        return new Sitzung(4711,new Raum(raumID,"W014",8,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.freespace.Objektklassen.Tag(1,"Lernen"), benutzer),false,endzeit);

        //TODO: Daten vom Server statt DummyDaten
    }

    public Sitzung sitzungPut(int id){

        ArrayList<Raum> raume = new ArrayList<>(this.raumGet());

        for(int i = 0; i < raume.size(); i++) {
            if(id == raume.get(i).getId()) {
                Raum meinRaum = new Raum(raume.get(i).getId(),
                        raume.get(i).getRaumname(),
                        raume.get(i).getTeilnehmer_max(),
                        raume.get(i).getTeilnehmer_aktuell(),
                        raume.get(i).getFotoURL(),
                        raume.get(i).getTag(),
                        raume.get(i).getBenutzer());
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
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","https://pbs.twimg.com/profile_images/775210778/peter_400x400.JPG", "",false);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",false);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",false);
        benutzer[3] = new Benutzer(4,"abc@def.com","Yoda","Meister","http://starwars.gamona.de/wp-content/gallery/yoda-bilder/13_Yoda.jpg", "",false);
        benutzer[4] = new Benutzer(5,"abc@def.com","Spock","Kommandant","https://pbs.twimg.com/profile_images/424886311078469632/8sKG_p8v_400x400.jpeg", "",false);
        benutzer[5] = new Benutzer(6,"abc@def.com","Schnee","Jon","http://static.giantbomb.com/uploads/original/3/39164/2865551-reasons-people-love-game-thrones-jon-snow-video.jpg", "",false);

        long endzeit=(System.currentTimeMillis()/1000L)+2700;    //aktuelle Zeit + 45 Minuten
        return new Sitzung(4711,new Raum(4711,"W014",8,7,"http://i.imgur.com/LyzIuVj.jpg", new Tag(1,"Präsentation"), benutzer),true,endzeit);
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
        Raum room =  new Raum(4711,"W014",6,5,"http://i.imgur.com/LyzIuVj.jpg", new com.swe.gruppe4.freespace.Objektklassen.Tag(1,"Hallo Welt"), benutzer);

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

    /**
     * Die folgenden Methoden werden für die REST-Ressourcen Tag benutzt
     */

    public ArrayList<Tag> tagGet() {
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

    public ArrayList<Raum> raumGet() {
        ArrayList<Raum> raumListe = new ArrayList<>();
        Benutzer[] benutzer = new Benutzer[6];
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","https://pbs.twimg.com/profile_images/775210778/peter_400x400.JPG", "",false);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",false);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",false);
        benutzer[3] = new Benutzer(4,"abc@def.com","Yoda","Meister","http://starwars.gamona.de/wp-content/gallery/yoda-bilder/13_Yoda.jpg", "",false);
        benutzer[4] = new Benutzer(5,"abc@def.com","Spock","Kommandant","https://pbs.twimg.com/profile_images/424886311078469632/8sKG_p8v_400x400.jpeg", "",false);
        benutzer[5] = new Benutzer(6,"abc@def.com","Schnee","Jon","http://static.giantbomb.com/uploads/original/3/39164/2865551-reasons-people-love-game-thrones-jon-snow-video.jpg", "",false);


        //Mokupdaten, später über GET vom Server
        raumListe.add(new Raum(100,"G100",22,6,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Präsentation"),benutzer));
        raumListe.add(new Raum(101,"G101",22,3,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Präsentation"),benutzer));
        raumListe.add(new Raum(4711,"G102",22,15,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Ruhe"),benutzer));
        raumListe.add(new Raum(103,"G103",22,0,"http://i.imgur.com/LyzIuVj.jpg",new Tag(0,""),new Benutzer[0]));
        raumListe.add(new Raum(104,"G104",22,0,"http://i.imgur.com/LyzIuVj.jpg",new Tag(0,""),new Benutzer[0]));
        raumListe.add(new Raum(4711,"G105",22,22,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Präsentation"),benutzer));
        raumListe.add(new Raum(106,"G106",22,22,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Präsentation"),benutzer));
        raumListe.add(new Raum(107,"G107",22,15,"http://i.imgur.com/LyzIuVj.jpg",new Tag(4711,"Präsentation"),benutzer));
        raumListe.add(new Raum(108,"G108",22,0,"http://i.imgur.com/LyzIuVj.jpg",new Tag(0,""),new Benutzer[0]));

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
                        raume.get(i).getBenutzer());
                return (Raum)meinRaum;
                //TODO: Daten vom Server statt DummyDaten
            }

        }
        //return (Raum)meinRaum;
        return null;
    }
    public Raum raumPut(int tagID){
        //TODO: Daten an Server senden
        //DUMMY! Später dann vom Server holen.
        Benutzer[] benutzer = new Benutzer[3];
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",false);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",false);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",false);
        return new Raum(4711,"W014",8,5,"http://i.imgur.com/LyzIuVj.jpg", new Tag(tagID,"Ruhe"), benutzer);

        //TODO: Daten vom Server statt DummyDaten
    }
}
