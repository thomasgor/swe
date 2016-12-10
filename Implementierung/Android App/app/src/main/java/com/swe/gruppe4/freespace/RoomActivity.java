package com.swe.gruppe4.freespace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.swe.gruppe4.freespace.Objektklassen.Raum;

import java.util.ArrayList;

public class RoomActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView roomView;
    private RoomAdapter roomAdapter;

    //Raum Objekt enthält alle Informationen zu einem Raum
    //Room Objekt dient nur zu Anzeige
    ArrayList<Raum> roomListFromConnection = new ArrayList<>();
    ArrayList<Room> orderedRoomListFull = new ArrayList<>();

    ArrayList<Room> roomListToShow = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_room, null, false);
        drawer.addView(contentView, 0);

        roomView = (ListView) findViewById(R.id.roomList);
        roomAdapter = new RoomAdapter(getApplicationContext(), R.layout.room_box);
        roomView.setAdapter(roomAdapter);
        roomView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        Verbindung connection = new Verbindung();
        roomListFromConnection = connection.raumListeGet();

        //Interne Funktion um Räume zu ordnen
        addRoomsInOrder(roomListFromConnection);
        //Übergebe Liste an Adapter zur Anzeige
        addRoomsInAdapter(orderedRoomListFull);

        //Test nur leer Räume zeigen
        //addRoomsInAdapter(getEmptyRooms(roomListFromConnection));

        roomAdapter.notifyDataSetChanged();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Interne Funktion um Listener zu setzen
        setOnItemClickListenerForView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.filter_tags){
            Intent intent = new Intent(getApplicationContext(), TagsFilterActivity.class);
            startActivity(intent);
        } else if(id == R.id.filter_friends_only) {
            item.setChecked(!item.isChecked());
        } else if(id == R.id.action_filter_rooms) {
            item.setChecked(!item.isChecked());
            if(item.isChecked()) {
                roomAdapter.clearList();
                addRoomsInAdapter(getEmptyRooms(roomListFromConnection));
                roomAdapter.notifyDataSetChanged();
            } else {
                roomAdapter.clearList();
                addRoomsInAdapter(orderedRoomListFull);
                roomAdapter.notifyDataSetChanged();
            }

        }

        /*//noinspection SimplifiableIfStatement
        if (id == R.id.filter_tags) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void addRoomsInOrder(ArrayList<Raum> rawList) {

        ArrayList<Raum> connectionRoomList = rawList;
        ArrayList<Room> redStateRoomList = new ArrayList<>();

        for (int i = 0; i < connectionRoomList.size(); i++) {
            Raum tmpRaum = connectionRoomList.get(i);
            int status = 0;

            //Bestimme Farbe des Raumstatus. Rot > 85%, gelb > 30%, sonst grün
            if (((double) tmpRaum.getTeilnehmer_aktuell()) /
                    ((double) tmpRaum.getTeilnehmer_max()) > 0.85) {
                status = R.drawable.circle_red;
            } else if (((double) tmpRaum.getTeilnehmer_aktuell()) /
                    ((double) tmpRaum.getTeilnehmer_max()) > 0.30) {
                status = R.drawable.circle_yellow;
            } else {
                status = R.drawable.circle_green;
            }

            String roomInfo = "Belegung: " + tmpRaum.getTeilnehmer_aktuell() + "/"
                    + tmpRaum.getTeilnehmer_max();

            if(status == R.drawable.circle_red) {
                redStateRoomList.add(redStateRoomList.size(),new Room(tmpRaum.getRaumname(), status, roomInfo, false));
            } else {
                orderedRoomListFull.add(new Room(tmpRaum.getRaumname(), status, roomInfo, false));
            }
        }

        //Schiebe volle Räume ans Ende
        for(int i = 0; i < redStateRoomList.size(); i++) {
            orderedRoomListFull.add(redStateRoomList.get(i));
        }
    }

    private Room raumRoomAdapter(Raum raum) {
        int status = 0;

        //Bestimme Farbe des Raumstatus. Rot > 85%, gelb > 30%, sonst grün
        if (((double) raum.getTeilnehmer_aktuell()) /
                ((double) raum.getTeilnehmer_max()) > 0.85) {
            status = R.drawable.circle_red;
        } else if (((double) raum.getTeilnehmer_aktuell()) /
                ((double) raum.getTeilnehmer_max()) > 0.30) {
            status = R.drawable.circle_yellow;
        } else {
            status = R.drawable.circle_green;
        }

        String roomInfo = "Belegung: " + raum.getTeilnehmer_aktuell() + "/"
                + raum.getTeilnehmer_max();

        return new Room(raum.getRaumname(), status, roomInfo, false);
    }

    //Übergebe Liste an Adapter zur Anzeige
    private void addRoomsInAdapter(ArrayList<Room> list) {
        for(int i = 0; i < list.size(); i++) {
            roomAdapter.add(list.get(i));
        }
    }

    //Filtert die leeren Räume aus einer Raumliste
    private ArrayList<Room> getEmptyRooms(ArrayList<Raum> fullRoomList) {
        ArrayList<Room> emptyRoomList = new ArrayList<>();

        for(int i = 0; i < fullRoomList.size(); i++) {
            if(fullRoomList.get(i).getTeilnehmer_aktuell() == 0) {
                emptyRoomList.add(raumRoomAdapter(fullRoomList.get(i)));
            }
        }

        return emptyRoomList;
    }

    public void setOnItemClickListenerForView() {
        roomView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(),RoomDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

}