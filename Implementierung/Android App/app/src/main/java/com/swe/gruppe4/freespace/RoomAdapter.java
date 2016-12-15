package com.swe.gruppe4.freespace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.swe.gruppe4.freespace.Objektklassen.Raum;

import java.util.ArrayList;

/**
 * Dient zum erstellen der Raumliste. Hier werden die Raumobjekt in die Listview
 * eingefügt
 *
 * @author Marco Linnartz
 * @version 1.1
 */

public class RoomAdapter extends ArrayAdapter<Raum> {

    /**
     * Liste der angezeigten Räume
     */
    private ArrayList<Raum> roomInfoList = new ArrayList<>();

    /**
     *  Fügt Raum zum Adapter hinzu
     */
    public void add(Raum room) {
        super.add(room);
        roomInfoList.add(room);
    }


    /**
     * Anzahl der angezeigten Räume
     * @return Anzahl der angezeigten Räume
     */
    @Override
    public int getCount() {
        return roomInfoList.size();
    }

    /**
     * Rückgabe des Raums anhand des Indexes
     * @param index
     * @return
     */
    @Override
    public Raum getItem(int index) {
        return this.roomInfoList.get(index);
    }

    public RoomAdapter(Context context , int textViewResourceId){
        super(context,textViewResourceId);
    }

    /**
     * Leert die Liste der angezeigten Räume
     */
    public void clearList() {
        roomInfoList = new ArrayList<>();
    }

    /**
     *  Wird genutzt um die Raumobjekte nacheinander in die Raumliste hinzuzufügen
     *  Informationen die dargestellt werden stammen aus dem Raumobjekt
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Raum roomObj = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.room_box, parent, false);
        }

        TextView roomName = (TextView) convertView.findViewById(R.id.roomName);
        assert roomObj != null;
        roomName.setText(roomObj.getRaumname());
        TextView roomInfo = (TextView) convertView.findViewById(R.id.roomInfo);
        roomInfo.setText("Belegung: " + roomObj.getTeilnehmer_aktuell() + "/" + roomObj.getTeilnehmer_max());
        ImageView statusIcon = (ImageView) convertView.findViewById(R.id.statusImg);

        if(roomObj.getStatus().equals("grün")) {
            statusIcon.setImageResource(R.drawable.circle_green);
        }
        else if(roomObj.getStatus().equals("gelb")) {
            statusIcon.setImageResource(R.drawable.circle_yellow);
        }
        else if(roomObj.getStatus().equals("rot")) {
            statusIcon.setImageResource(R.drawable.circle_red);
        }
        else {
            statusIcon.setImageResource(R.drawable.circle_gray);
        }

        return convertView;
    }
}
