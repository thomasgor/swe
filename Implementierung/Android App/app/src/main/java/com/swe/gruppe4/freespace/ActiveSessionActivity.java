package com.swe.gruppe4.freespace;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.view.ViewPropertyAnimator;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;
import com.swe.gruppe4.freespace.Objektklassen.Benutzer;
import com.swe.gruppe4.freespace.Objektklassen.Raum;
import com.swe.gruppe4.freespace.Objektklassen.Sitzung;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ActiveSessionActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView imgRoom;
    Sitzung data;
    Raum raum;
    TextView raumName;
    TextView tag;
    Button setTag;
    ListView listPeopleInRoomView;
    RaumdetailsLeuteAdapter raumLeuteAdapter;
    TextView activeUntil;
    Date untilTime;
    TextView leute;
    Button erneuern;
    Button beenden;
    RelativeLayout rlTop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Daten holen
        data = (Sitzung) getIntent().getSerializableExtra("sitzung");
        raum = data.getRaum();


        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_active_session, null, false);
        drawer.addView(contentView, 0);

        imgRoom = (ImageView) findViewById(R.id.img_room_photo);
        setData();

        erneuern = (Button) findViewById(R.id.btn_session_renew);
        erneuern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = new Verbindung().sitzungPut(data.getId());
                raum = data.getRaum();
                setData();
            }
        });

        beenden = (Button) findViewById(R.id.btn_goto);
        beenden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Verbindung().sitzungDelete(data.getId());
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void setData(){
        raumName = (TextView) findViewById(R.id.txt_room_number);
        raumName.setText(getString(R.string.room_number, raum.getRaumname()));

        getSupportActionBar().setTitle(raum.getRaumname());

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



        setTag = (Button) findViewById(R.id.btn_set_tag);
        setTag.setEnabled(data.isMyTag());

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

        activeUntil = (TextView) findViewById(R.id.txt_session_active_until);
        untilTime = new Date(data.getEndzeit()*1000);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(untilTime);
        activeUntil.setText(getString(R.string.session_active_until_clock, DateFormat.getTimeInstance(DateFormat.SHORT).format(untilTime)));

        leute = (TextView) findViewById(R.id.txt_people_in_room_cnt);
        int max = raum.getTeilnehmer_max();
        int crnt = raum.getTeilnehmer_aktuell();
        leute.setText(getString(R.string.people_in_room_cnt,crnt,max,getResources().getQuantityString(R.plurals.people_in_room_cnt_anon,crnt-nichtAnonym,crnt-nichtAnonym)));
    }

}

