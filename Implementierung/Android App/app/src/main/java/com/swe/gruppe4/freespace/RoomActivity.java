package com.swe.gruppe4.freespace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.swe.gruppe4.freespace.Objektklassen.Freundschaft;
import com.swe.gruppe4.freespace.Objektklassen.Raum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Die Activity dient zur Darstellung der Räume in Form einer Liste. Die Liste kann
 * gefiltert werden in dem entsprechende Checkboxen gesetzt oder über die TagFilterActivity
 * Tags ausgewählt werden. Die Klasse enthält die Methoden, die zur Filterung nötig sind.
 * Die Filter werden persistent gesetzt.
 *
 * @author Marco Linnartz
 * @version 1.0
 */

public class RoomActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView roomView;
    private RoomAdapter roomAdapter;

    //Zustand der Checkboxen "Räume mit Freunden" und "Nur leere Räume"
    private boolean roomsWithFriendsIsSelected;
    private boolean emtyRoomsIsSelected;

    //Enthält alle Raume und wird bei onCeate durch Verbindungsklasse gefüllt
    ArrayList<Raum> roomListFromConnection = new ArrayList<>();

    //Listen für die Anzeige

    //enthält nur noch die leeren Räume
    ArrayList<Raum> emptyRoomsList = new ArrayList<>();
    //enthält nur noch die Räume mit Freunden
    ArrayList<Raum> roomsWithFriendsList = new ArrayList<>();

    //entält die gesetzten Filter
    HashSet<String> filterTags = new HashSet<>();

    //Freundschaftsliste des aktuellen Benutzers
    ArrayList<Freundschaft> friendListUser = new ArrayList<>();

    //speichert die gesetzten Filter und Tags
    SharedPreferences sharedPref;

    /**
     * Beim Aufbau der Activitiy wird zunächst der Status der Checkboxen wiederhergestellt (wenn schon vorhanden).
     * Diese wurden in einer SharedPreferences Datei gespeichert. Wenn Tags gesetzt wurden befinden, diese sich ebenfalls
     * in dieser Datei. Über ein Verbindungsobjekt wird zunächst die aktuelle Raumliste geladen.
     * Außerdem werden die leeren und Räume mit Freunden erstellt (Nicht teuer).
     * Anzeige der Räume erfolgt anschließen mit Hilfe der Methode updateRooms.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        sharedPref = this.getSharedPreferences("com.swe.gruppe4.freespace.roomfilter", Context.MODE_PRIVATE);
        final boolean defaultValue = false;
        if(sharedPref.contains("emtyRoomsIsSelected")) {
            emtyRoomsIsSelected = sharedPref.getBoolean("emtyRoomsIsSelected", defaultValue);
        } else {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("emtyRoomsIsSelected", defaultValue);
            editor.commit();
        }

        if(sharedPref.contains("roomsWithFriendsIsSelected")) {
            roomsWithFriendsIsSelected = sharedPref.getBoolean("roomsWithFriendsIsSelected", defaultValue);
        } else {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("roomsWithFriendsIsSelected", defaultValue);
            editor.commit();
        }

        if(sharedPref.contains("filterTags")) {
            filterTags = (HashSet<String>) sharedPref.getStringSet("filterTags", new HashSet<String>());
        } else {
            filterTags = new HashSet<>();
        }


        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_room, null, false);
        drawer.addView(contentView, 0);

        roomView = (ListView) findViewById(R.id.roomList);
        roomAdapter = new RoomAdapter(getApplicationContext(), R.layout.room_box);
        roomView.setAdapter(roomAdapter);
        roomView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        VerbindungDUMMY connection = new VerbindungDUMMY();
        roomListFromConnection = connection.raumGet();
        roomsWithFriendsList = getRoomsWithFriends(roomListFromConnection);
        emptyRoomsList = getEmptyRooms(roomListFromConnection);

        //Erstellt Raumliste für Anzeige nach Filterkriterien und zeigt sie an
        updateRooms();

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

    /**
     * Checkboxen im Optionsmenu werden entsprechend der Einstellungen in den SharedPreferenzes gesetzt
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.room, menu);
        MenuItem empty = menu.findItem(R.id.action_filter_rooms);
        empty.setChecked(emtyRoomsIsSelected);
        MenuItem friends = menu.findItem(R.id.filter_friends_only);
        friends.setChecked(roomsWithFriendsIsSelected);
        return true;
    }

    /**
     * Wertet die Auswahl im Optionsmenu aus.
     * Wenn Tags berüht wird startet die TagsFilterActivity, um Tags zu setzten
     * Bei auf Checkboxen gedrückt wird wird der Status getoggelt und die Datei mit den SharedPreferences
     * wird aktualisiert
     * Am Ende wird die Anzeige der Räume aktualisiert
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Tags
        if(id == R.id.filter_tags){
            Intent intent = new Intent(getApplicationContext(), TagsFilterActivity.class);
            startActivity(intent);
        }
        //Räume mit Freunden
        else if(id == R.id.filter_friends_only) {
            item.setChecked(!item.isChecked());
            if(item.isChecked()) {
                roomsWithFriendsList = getRoomsWithFriends(roomListFromConnection);
                roomsWithFriendsIsSelected = true;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("roomsWithFriendsIsSelected",true);
                editor.commit();
            }
            else {
                roomsWithFriendsIsSelected = false;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("roomsWithFriendsIsSelected",false);
                editor.commit();
            }

        }
        //Nur leere Räume
        else if(id == R.id.action_filter_rooms) {
            item.setChecked(!item.isChecked());
            if(item.isChecked()) {
                emptyRoomsList = getEmptyRooms(roomListFromConnection);
                emtyRoomsIsSelected = true;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("emtyRoomsIsSelected",true);
                editor.commit();
            } else {
                emtyRoomsIsSelected = false;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("emtyRoomsIsSelected",false);
                editor.commit();
            }

        }

        updateRooms();

        return super.onOptionsItemSelected(item);
    }

    /**
     *Übergibt die gefilterte Raumliste an dem Raumadapter
     * Hier erfolgt auch die Filterung anhand der gesetzten Filter
     * Am Ende erfolgt eine Alphanumerische Sortierung und das Schieben
     * der vollen Räume an das Ende der Liste
     */
    private void updateRooms() {
        ArrayList<Raum> result = new ArrayList<>();
        roomAdapter.clearList();
        if(roomsWithFriendsIsSelected) {
            result.addAll(roomsWithFriendsList);
            result = getRoomsWithTags(result);
        }
        if(emtyRoomsIsSelected) {
            result.addAll(emptyRoomsList);
            result = getRoomsWithTags(result);
        }

        if(!roomsWithFriendsIsSelected && !emtyRoomsIsSelected) {
            result.addAll(roomListFromConnection);
            result = getRoomsWithTags(result);
        }

        result = getRoomsInAlphanumericOrder(result);
        result = orderFullRoomsToBottomOfList(result);

        addRoomsInAdapter(result);
        roomAdapter.notifyDataSetChanged();

    }

    /**
     * Übergebe Liste an Adapter zur Anzeige
     */
    private void addRoomsInAdapter(ArrayList<Raum> list) {
        for(int i = 0; i < list.size(); i++) {
            roomAdapter.add(list.get(i));
        }
    }

    /**
     * //Filtert die leeren Räume aus einer Raumliste
     * @param fullRoomList Liste die gefiltert werden soll
     * @return gefilterte Liste
     */
    private ArrayList<Raum> getEmptyRooms(ArrayList<Raum> fullRoomList) {
        ArrayList<Raum> emptyRoomList = new ArrayList<>();

        for(int i = 0; i < fullRoomList.size(); i++) {
            if(fullRoomList.get(i).getTeilnehmer_aktuell() == 0) {
                emptyRoomList.add(fullRoomList.get(i));
            }
        }

        return emptyRoomList;
    }

    /**
     * Filtert die Liste mit Freunden aus einer Raumliste
     * @param fullRoomList Liste die gefiltert werden soll
     * @return gefilterte Liste
     */
    private ArrayList<Raum> getRoomsWithFriends(ArrayList<Raum> fullRoomList) {
        friendListUser = new VerbindungDUMMY().freundschaftGet();
        ArrayList<Raum> friendListRoom = new ArrayList<>();
        boolean found = false;

        for(int i = 0; i < fullRoomList.size(); i++) {
            Raum tmpRoom = fullRoomList.get(i);
            found = false;
            for(int j = 0; j < friendListUser.size(); j++) {
                if(friendListUser.get(j).getRaum().getId() == tmpRoom.getId()) {
                    found = true;
                }
            }
            if(found) {
                friendListRoom.add(tmpRoom);
            }

        }
        return friendListRoom;
    }

    private ArrayList<Raum> getRoomsInAlphanumericOrder(ArrayList<Raum> raumList) {
        ArrayList<Raum> tmpList = raumList;
        Collections.sort(tmpList, new RaumComparator());
        return tmpList;
    }

    private ArrayList<Raum> getRoomsWithTags(ArrayList<Raum> raumList) {
        ArrayList<Raum> tmpList = new ArrayList<>();
        if(filterTags == null || filterTags.size() == 0) {
            return  raumList;
        }
        for(Raum room : raumList) {
            for (String filterTagString : filterTags) {
                if (room.getTag().getName().equals(filterTagString)) {
                    tmpList.add(room);
                    break;
                }
            }
        }
        return tmpList;
    }

    /**
     * Wenn auf ein Raumelement gedrückt wird, wird die RaumdetailsActivity mit einem Intent
     * gestartet, das die ID des Raumes enthält
     */
    public void setOnItemClickListenerForView() {
        roomView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(),RoomDetailsActivity.class);
                intent.putExtra("id",roomAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });
    }

    /**
     * Sortiert die vollen Räume an das Ende der Liste
     * @param roomList ursprüngeliche Liste
     * @return Liste mit vollen Räume am Ende, wenn vorhanden
     */
    public ArrayList<Raum> orderFullRoomsToBottomOfList(ArrayList<Raum> roomList) {
        ArrayList<Raum> tmpList = new ArrayList<>();
        ArrayList<Raum> fullRoomsList = new ArrayList<>();
        for(int i = 0; i < roomList.size(); i++) {
            if(roomList.get(i).getTeilnehmer_aktuell() ==
                    roomList.get(i).getTeilnehmer_max()) {
                fullRoomsList.add(fullRoomsList.size(),roomList.get(i));
            }
            else {
                tmpList.add(roomList.get(i));
            }
        }

        for (int i = 0; i < fullRoomsList.size(); i++) {
            tmpList.add(fullRoomsList.get(i));
        }

        return tmpList;
    }

    //Nicht mehr nötig, da das Room Objekt durch Raum abgelöst wurde
    /*
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
    */

    /*
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
    */

}