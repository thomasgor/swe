package com.swe.gruppe4.mockup2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;
import com.swe.gruppe4.mockup2.Objektklassen.*;

/**
 * Created by Kiesa on 29.10.2016.
 */

class LectureAdapter extends ArrayAdapter<Veranstaltung> {

    private ArrayList<Veranstaltung> lectureList = new ArrayList<>();
    /**
     *  add Room object  to the adapter
     */
    public void add(Veranstaltung veranstaltung) {
        super.add(veranstaltung);
        lectureList.add(veranstaltung);

    }



    public Veranstaltung getItem(int index) {
        return this.lectureList.get(index);
    }

    public LectureAdapter(Context context , int textViewResourceId){
        super(context,textViewResourceId);
    }

    /**
     *  this method is used to populate the Lecturelist in the LecturelistActivity
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Veranstaltung lectureObj = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lecture_box, parent, false);
        }
        TextView lectureName = (TextView) convertView.findViewById(R.id.lecture_name);
        lectureName.setText(lectureObj.getName());

        //converting Date from long to readable String
        Date date=new Date(lectureObj.getVon());
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy hh:mm");
        String dateTextVon = df2.format(date);
        TextView lectureDateVon = (TextView) convertView.findViewById(R.id.lecture_date_von);
        lectureDateVon.setText(dateTextVon);


        date=new Date(lectureObj.getBis());
        String dateTextBis = df2.format(date);
        TextView lectureDateBis = (TextView) convertView.findViewById(R.id.lecture_date_bis);
        lectureDateBis.setText(dateTextBis);

        TextView lectureRoom = (TextView) convertView.findViewById(R.id.lecture_room);
        lectureRoom.setText(lectureObj.getRaum().getRaumname());

        ImageView editButton = (ImageView) convertView.findViewById(R.id.imageView2);
        ImageView deleteButton = (ImageView) convertView.findViewById(R.id.imageView3);
        editButton.setImageResource(android.R.drawable.ic_menu_edit);
        deleteButton.setImageResource(android.R.drawable.ic_menu_delete);
        return convertView;
    }

}
