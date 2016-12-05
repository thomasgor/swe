package com.swe.gruppe4.mockup2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.swe.gruppe4.mockup2.Objektklassen.Benutzer;
import com.swe.gruppe4.mockup2.Objektklassen.Raum;
import com.swe.gruppe4.mockup2.Objektklassen.Sitzung;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RoomDetailsActivity extends AppCompatActivity {

    ImageView imgRoom;
    Raum raum;
    TextView raumName;
    TextView tag;
    ListView listPeopleInRoomView;
    RaumdetailsLeuteAdapter raumLeuteAdapter;
    TextView leute;
    Button gehezu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imgRoom = (ImageView) findViewById(R.id.img_room_photo);

        //Daten holen
        raum = (Raum) getIntent().getSerializableExtra("raum");

        setData();

        gehezu=(Button) findViewById(R.id.btn_goto);
        gehezu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Das ist noch nicht implementiert",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setData(){
        raumName = (TextView) findViewById(R.id.txt_room_number);
        raumName.setText(getString(R.string.room_number, raum.getRaumname()));

        tag = (TextView) findViewById(R.id.txt_tag);
        tag.setText(raum.getTag().getName());
        tag.setText(getString(R.string.tag, raum.getTag().getName()));

        Ion.with(getApplicationContext())
                .load(raum.getFotoURL())
                .withBitmap()
                .placeholder(R.drawable.ic_hourglass_empty_black_24dp)
                .error(R.drawable.ic_hourglass_empty_black_24dp)
                .animateIn(android.R.anim.fade_in)
                .intoImageView(imgRoom);



        listPeopleInRoomView = (ListView) findViewById(R.id.list_people_in_room);
        raumLeuteAdapter = new RaumdetailsLeuteAdapter(getApplicationContext(), R.layout.friends_box);
        listPeopleInRoomView.setAdapter(raumLeuteAdapter);
        listPeopleInRoomView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        int nichtAnonym = 0;
        for(Benutzer ben : raum.getBenutzer()){
            nichtAnonym++;
            raumLeuteAdapter.add(ben);
        }
        raumLeuteAdapter.notifyDataSetChanged();

        leute = (TextView) findViewById(R.id.txt_people_in_room_cnt);
        int max = raum.getTeilnehmer_max();
        int crnt = raum.getTeilnehmer_aktuell();
        leute.setText(getString(R.string.people_in_room_cnt,crnt,max,crnt-nichtAnonym));
    }
}
