package com.fhaachen.swe.freespace.maps.GebaeudeG;

import com.fhaachen.swe.freespace.maps.Kante;
import com.fhaachen.swe.freespace.maps.Karte;
import com.fhaachen.swe.freespace.maps.Knoten;

import java.util.ArrayList;

/**
 * Created by Simon on 08.12.2016.
 */
public class G1 extends Karte{

    public G1(){
        this.setUp();
    }

    private void setUp() {

        //Räume
        Knoten g101 = new Knoten("G101");
        Knoten g102 = new Knoten("G102");
        Knoten g107 = new Knoten("G107");
        Knoten g111 = new Knoten("G111");
        Knoten g112 = new Knoten("G112");
        Knoten g115 = new Knoten("G115");
        Knoten g116 = new Knoten("G116");

        //Ausgänge aus G1 !!! für Etagenwechsel/Ausgang immer Konstanten aus Karte nutzen
        this.AUSGANG = new Knoten("G100");
        Knoten etageHoch = new Knoten(ETAGE_HOCH); //

        //FlurKnoten
        Knoten g131 = new Knoten("G131"); //siehe Knoten 1
        Knoten g132 = new Knoten("G132"); //siehe Knoten 2
        Knoten g133 = new Knoten("G133"); //siehe Knoten 3
        Knoten g134 = new Knoten("G134"); //siehe Knoten 4
        Knoten g135 = new Knoten("G135"); //siehe Knoten 5
        Knoten g136 = new Knoten("G136"); //siehe Knoten 6
        Knoten g137 = new Knoten("G137"); //siehe Knoten 7

        //Kanten, da ungerichtet, jede kante in beide Richtungen
        Kante g161 = new Kante("G161", g131, g101,2);       //siehe Kante 1, Gewicht = grobe Entfernung in Meter
        Kante g162 = new Kante("G162", g131, g102,2);       //siehe Kante 2, Gewicht = grobe Entfernung in Meter
        Kante g163 = new Kante("G163", g131, g107,2);       //siehe Kante 3, Gewicht = grobe Entfernung in Meter
        Kante g164 = new Kante("G164", g132, g131,3);       //siehe Kante 4, Gewicht = grobe Entfernung in Meter
        Kante g165 = new Kante("G165", g132, g107,2);       //siehe Kante 5, Gewicht = grobe Entfernung in Meter
        Kante g166 = new Kante("G166", g133, g132,4);       //siehe Kante 6, Gewicht = grobe Entfernung in Meter
        Kante g167 = new Kante("G167", g133, AUSGANG,5);    //siehe Kante 7
        Kante g168 = new Kante("G168", g133, g134,2);       //siehe Kante 8, Gewicht = grobe Entfernung in Meter
        Kante g169 = new Kante("G169", g134, etageHoch,5);  //siehe Kante 9
        Kante g170 = new Kante("G170", g134, g135,2);       //siehe Kante 10, Gewicht = grobe Entfernung in Meter
        Kante g171 = new Kante("G171", g135, g112,2);       //siehe Kante 11, Gewicht = grobe Entfernung in Meter
        Kante g172 = new Kante("G172", g135, g111,2);       //siehe Kante 12, Gewicht = grobe Entfernung in Meter
        Kante g173 = new Kante("G173", g135, g136,4);       //siehe Kante 13, Gewicht = grobe Entfernung in Meter
        Kante g174 = new Kante("G174", g136, g111,2);       //siehe Kante 14, Gewicht = grobe Entfernung in Meter
        Kante g175 = new Kante("G175", g136, g137,3);       //siehe Kante 15, Gewicht = grobe Entfernung in Meter
        Kante g176 = new Kante("G176", g137, g116,2);       //siehe Kante 16, Gewicht = grobe Entfernung in Meter
        Kante g177 = new Kante("G177", g137, g115,2);       //siehe Kante 17, Gewicht = grobe Entfernung in Meter

        ArrayList<Knoten> knotenListe = new ArrayList<Knoten>();

        knotenListe.add(g101);
        knotenListe.add(g102);
        knotenListe.add(g107);
        knotenListe.add(g111);
        knotenListe.add(g112);
        knotenListe.add(g115);
        knotenListe.add(g116);
        knotenListe.add(AUSGANG);
        knotenListe.add(etageHoch);
        knotenListe.add(g131);
        knotenListe.add(g132);
        knotenListe.add(g133);
        knotenListe.add(g134);
        knotenListe.add(g135);
        knotenListe.add(g136);
        knotenListe.add(g137);

        this.setKnoten(knotenListe);

        ArrayList<Kante> kantenListe = new ArrayList<Kante>();

        kantenListe.add(g161);
        kantenListe.add(g162);
        kantenListe.add(g163);
        kantenListe.add(g164);
        kantenListe.add(g165);
        kantenListe.add(g166);
        kantenListe.add(g167);
        kantenListe.add(g168);
        kantenListe.add(g169);
        kantenListe.add(g170);
        kantenListe.add(g171);
        kantenListe.add(g172);
        kantenListe.add(g173);
        kantenListe.add(g174);
        kantenListe.add(g175);
        kantenListe.add(g176);
        kantenListe.add(g177);

        this.setKanten(kantenListe);
    }
}


/*
*        _______ _____________
*       |       |             |
*       |       |             |
*       | G102  |    G101     |
*       |       |             |
*       |       |_____________|
*       |       |    |        |
*       |       | 1  |        |
*       |_______|    |  G107  |
*       |XXXXXXX|    |        |
*       |XXXXXXX|    |        |
*       |XXXXXXX| 2  |________|
*       |XXXXXXX|    |XXXXXXXX|
*       |       W             |
*       |_______W             |
*       |       A 3           E
*       |_______A             E
*       |       W 4   SSSSSSSS|
*       |_______W     SSSSSSSS|
*       |XXXXXXX|    |XXXXXXXX|
*       |       | 5  |        |
*       | G112  |    |        |
*       |       | 6  |  G111  |
*       |_______|    |        |
*       |       | 7  |        |
*       |       |____|________|
*       | G116  |             |
*       |       |             |
*       |       |    G115     |
*       |       |             |
*       |_______|_____________|
*
*
*   X = nicht betretbar
*   A = Aufzug
*   S = Strufen nach oben
*   E = Eingang/Ausgang
*
*
*   Flurknoten:
*
*   1 = Flurknoten verbunden über Kanten mit G101, G102, G107, 2
*       id = G131
*   2 = Flurknoten verbunden über Kanten mit G107, 1, 3
*       id = G132
*   3 = Flurknoten verbunden über Kanten mit exit, 2, 4
*       id = G133
*   4 = Flurknoten verbunden über Kanten mit etageHoch, 3, 5
*       id = G134
*   5 = Flurknoten verbunden über Kanten mit G111, G112, 4, 6
*       id = G135
*   6 = Flurknoten verbunden über Kanten mit G111, 5, 7
*       id = G136
*   7 = Flurknoten verbunden über Kanten mit G116 u G115 u 6
*       id = G137
*
*
*   Kanten:
*
*            G101
*              |
*              1
*              |
*   G102--2--G131---3---
*              |        |
*              4      G107
*              |        |
*            G132---5---
*              |
*              6
*              |
*            G133---7---E
*              |
*              8
*              |
*            G134---9---S
*              |
*              10
*              |
*   G112-11--G135--12---
*              |        |
*              13     G111
*              |        |
*            G136--14---
*              |
*              15
*              |
*   G116-16--G137
*              |
*              17
*              |
*            G115
*
*   1 = Kanten mit ID G161
*   2 = Kanten mit ID G162
*   3 = Kanten mit ID G163
*   4 = Kanten mit ID G164
*   5 = Kanten mit ID G165
*   6 = Kanten mit ID G166
*   7 = Kanten mit ID G167
*   8 = Kanten mit ID G168
*   9 = Kanten mit ID G169
*   10 = Kanten mit ID G170
*   11 = Kanten mit ID G171
*   12 = Kanten mit ID G172
*   13 = Kanten mit ID G173
*   14 = Kanten mit ID G174
*   15 = Kanten mit ID G175
*   16 = Kanten mit ID G176
*   17 = Kanten mit ID G177
*
*/
