package com.swe.gruppe4.freespace;
import android.app.Activity;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import com.swe.gruppe4.freespace.Objektklassen.*;

/**
 * Created by Kiesa on 29.10.2016.
 */

class LectureAdapter extends ArrayAdapter<Veranstaltung> {

    private ArrayList<Veranstaltung> lectureList = new ArrayList<>();
    private Activity aufgerufenVon;
    private Context context;
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

    public LectureAdapter(Context context , int textViewResourceId, Activity aufgerufenVon){
        super(context,textViewResourceId);
        this.aufgerufenVon = aufgerufenVon;
        this.context = context;
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
        SimpleDateFormat df2 = new SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault());
        String dateTextVon = df2.format(date);



        date=new Date(lectureObj.getBis());
        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
        String dateTextBis = df3.format(date);
        TextView lectureDateBis = (TextView) convertView.findViewById(R.id.lecture_date);
        lectureDateBis.setText(dateTextVon + " - " + dateTextBis);

        TextView lectureRoom = (TextView) convertView.findViewById(R.id.lecture_room);
        lectureRoom.setText(lectureObj.getRaum().getRaumname());


        ImageView deleteButton = (ImageView) convertView.findViewById(R.id.imageView3);
        deleteButton.setImageResource(android.R.drawable.ic_menu_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDelete(view, lectureObj.getId(), context);
            }
        });



        ImageView editButton = (ImageView) convertView.findViewById(R.id.imageView2);
        editButton.setImageResource(android.R.drawable.ic_menu_edit);

        //editButton.setClickable(true);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), LectureEditActivity.class);
                i.putExtra("Id", lectureObj.getId());
                view.getContext().startActivity(i);
            }
        });




        return convertView;
    }

    private void showDialogDelete(View v, final int lectureId, final Context context){
        AlertDialog.Builder build = new AlertDialog.Builder(v.getRootView().getContext());
        build.setCancelable(false);

        //VerbindungDUMMY verb = new VerbindungDUMMY();
        RestConnection verb = new RestConnection(this.aufgerufenVon);

        //build.setTitle("Freund wirklich löschen?");
        build.setMessage("Möchten Sie die Veranstaltung " + verb.lectureGet(lectureId).getName()+ " wirklich löschen?");
        build.setPositiveButton("Ja", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //VerbindungDUMMY verb = new VerbindungDUMMY();
                RestConnection verb = new RestConnection(getContext());

                verb.lectureDelete(lectureId);
                notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(),"Veranstaltung gelöscht", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, LectureListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);



            }

        });

        build.setNegativeButton("Nein", null);
        AlertDialog alert1 = build.create();
        alert1.show();
    }

}
