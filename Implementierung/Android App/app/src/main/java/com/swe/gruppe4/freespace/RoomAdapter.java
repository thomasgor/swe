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
 * Created by Kiesa on 28.10.2016.
 */

public class RoomAdapter extends ArrayAdapter<Raum> {

    private ArrayList<Raum> roomInfoList = new ArrayList<>();
    /**
     *  add Room object  to the adapter
     */
    public void add(Raum room) {
        super.add(room);
        roomInfoList.add(room);

    }


    @Override
    public int getCount() {
        return roomInfoList.size();
    }

    @Override
    public Raum getItem(int index) {
        return this.roomInfoList.get(index);
    }

    public RoomAdapter(Context context , int textViewResourceId){
        super(context,textViewResourceId);
    }

    public void clearList() {
        roomInfoList = new ArrayList<>();
    }

    /**
     *  this method is used to populate the Roomlist in the RoomActivity
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Raum roomObj = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.room_box, parent, false);
        }
        /*if(roomObj.flag){
            ImageView leaveButton = (ImageView) convertView.findViewById(R.id.leaveRoom);
            ImageView refreshButton = (ImageView) convertView.findViewById(R.id.refreshSession);
            leaveButton.setImageResource(R.drawable.ic_menu_send);
            refreshButton.setImageResource(android.R.drawable.ic_menu_rotate);
        }*/
        TextView roomName = (TextView) convertView.findViewById(R.id.roomName);
        assert roomObj != null;
        roomName.setText(roomObj.getRaumname());
        TextView roomInfo = (TextView) convertView.findViewById(R.id.roomInfo);
        roomInfo.setText("Belegung: " + roomObj.getTeilnehmer_aktuell() + "/" + roomObj.getTeilnehmer_max());
        ImageView statusIcon = (ImageView) convertView.findViewById(R.id.statusImg);

        //TODO: MUSS noch geändert werden wenn klar ist wie das Raumobjekt den Status übergibt
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
