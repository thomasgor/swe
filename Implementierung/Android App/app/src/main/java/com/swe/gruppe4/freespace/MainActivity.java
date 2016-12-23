package com.swe.gruppe4.freespace;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.content.Intent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;

import com.google.firebase.iid.FirebaseInstanceId;
import com.swe.gruppe4.freespace.Objektklassen.Raum;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    // statische Variable um den eingescannten Startpunkt festzuhalten
    // Id 0 darf nicht in der Tabelle Räume der DB vorhanden sein
    public static int startingPointId = 0;
    private Button qrScanner;
    private ListView roomView;
    private RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.d("FirebaseToken", FirebaseInstanceId.getInstance().getToken());

        final VerbindungDUMMY connection = new VerbindungDUMMY();

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawer.addView(contentView, 0);
        qrScanner = (Button) findViewById(R.id.button);

        qrScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),QRScanActivity.class);
                //ArrayList<Raum> roomListFromConnection = new ArrayList<>(connection.raumGet());
                //intent.putExtra("raumliste",roomListFromConnection);
                intent.putExtra("id", 0);
                startActivity(intent);
            }
        });

        TabHost host = (TabHost) findViewById(R.id.roomTab);
        host.setup();
        TabHost.TabSpec spec = host.newTabSpec("Liste");
        spec.setContent(R.id.listTab);
        spec.setIndicator("Liste");
        host.addTab(spec);

        spec = host.newTabSpec("Karte");
        spec.setContent(R.id.mapTab);
        spec.setIndicator("Karte");
        host.addTab(spec);

        //ArrayList<Raum> roomListFromConnection = new ArrayList<>(connection.raumListeGet());

        roomView = (ListView) findViewById(R.id.roomList);
        roomAdapter = new RoomAdapter(getApplicationContext(), R.layout.room_box);
        roomView.setAdapter(roomAdapter);
        roomView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        /*
        for(int i = 0; i < roomListFromConnection.size(); i++) {
            roomAdapter.add(roomListFromConnection.get(i));
        }
        */

        //Mockup Daten
        /*Benutzer[] benutzer = new Benutzer[3];
        benutzer[0] = new Benutzer(1,"abc@def.com","Pan","Peter","http://img.lum.dolimg.com/v1/images/open-uri20150422-20810-r3neg5_4c4b3ee3.jpeg", "",false);
        benutzer[1] = new Benutzer(2,"abc@def.com","Beutlin","Frodo","http://thewallmachine.com/files/1376423116.jpg", "",false);
        benutzer[2] = new Benutzer(3,"abc@def.com","Potter","Harry","http://intouch.wunderweib.de/assets/styles/600x600/public/intouch/media/redaktionell/wunderweib/intouch_2/1news/2014_10/juli_33/woche2_22/thilo_7/harrypotter_3/harry-potter-h.jpg?itok=xOtudiW3", "",false);
        roomAdapter.add(new Raum(100,"G100",22,5,"",new Tag(4711,"Präsentation"),benutzer));
        roomAdapter.add(new Raum(102,"G102",22,15,"",new Tag(4711,"Präsentation"),benutzer));
        roomAdapter.add(new Raum(104,"G104",22,0,"",new Tag(0,""),new Benutzer[0]));
        roomAdapter.add(new Raum(107,"G107",22,15,"",new Tag(4711,"Präsentation"),benutzer)); */

        ArrayList<Raum> räume = connection.raumGet();
        for (Raum r:räume) {
            if(r.getTeilnehmer_aktuell() < r.getTeilnehmer_max())
                roomAdapter.add(r);

        }
        roomAdapter.notifyDataSetChanged();
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/


        roomView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(),RoomDetailsActivity.class);
                intent.putExtra("id",roomAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });
    }



}


