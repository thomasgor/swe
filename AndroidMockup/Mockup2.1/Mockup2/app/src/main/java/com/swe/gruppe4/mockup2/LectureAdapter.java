package com.swe.gruppe4.mockup2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
        final Veranstaltung lectureObj = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lecture_box, parent, false);
        }
        TextView lectureName = (TextView) convertView.findViewById(R.id.lecture_name);
        lectureName.setText(lectureObj.getName());

        //converting Date from long to readable String
        Date date=new Date(lectureObj.getVon());
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy hh:mm", java.util.Locale.getDefault());
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
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDelete(view, lectureObj.getId());
            }
        });
        editButton.setImageResource(android.R.drawable.ic_menu_edit);
        deleteButton.setImageResource(android.R.drawable.ic_menu_delete);
        return convertView;
    }

    private void showDialogDelete(View v, final long lectureId){
        AlertDialog.Builder build = new AlertDialog.Builder(v.getRootView().getContext());
        build.setCancelable(false);
        Verbindung verb = new Verbindung();

        //build.setTitle("Freund wirklich löschen?");
        build.setMessage("Möchten Sie die Veranstaltung " + verb.lectureGet(lectureId).getName()+ " wirklich löschen?");
        build.setPositiveButton("Ja", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Verbindung verb = new Verbindung();
                verb.lectureDelete(lectureId);
                notifyDataSetChanged();

            }

        });

        build.setNegativeButton("Nein", null);
        AlertDialog alert1 = build.create();
        alert1.show();
    }

}
