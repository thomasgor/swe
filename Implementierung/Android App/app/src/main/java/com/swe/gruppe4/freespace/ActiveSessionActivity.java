package com.swe.gruppe4.freespace;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.swe.gruppe4.freespace.Objektklassen.Benutzer;
import com.swe.gruppe4.freespace.Objektklassen.Raum;
import com.swe.gruppe4.freespace.Objektklassen.Sitzung;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

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
    boolean hasShadow = false;

    public final static int BACK_FROM_TAG = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Daten holen
        //data = new VerbindungDUMMY().sitzungGet();

        data = new RestConnection(this).sitzungGet();

        if(data == null){
            Toast.makeText(this, "Leider ist ein Fehler aufgetreten, bitte starten Sie die App neu." , Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            //startActivity(intent);
            finish();
        }

        raum = data.getRaum();
        //raum = new RestConnection(this).raumGet(data.getRaum().getId());


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
                data = new RestConnection(ActiveSessionActivity.this).sitzungPut(data.getId());
                raum = data.getRaum();
                setData();
            }
        });

        beenden = (Button) findViewById(R.id.btn_goto);
        beenden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RestConnection(ActiveSessionActivity.this).sitzungDelete(data.getId());
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            rlTop=(RelativeLayout) findViewById(R.id.content_active_session2);
            listPeopleInRoomView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                    if(i != SCROLL_STATE_IDLE){
                        if(listIsAtTop()){
                            rlTop.animate().z(0).setStartDelay(0).setDuration(130);
                        } else {
                            rlTop.animate().z(7).setStartDelay(0).setDuration(130);
                        }
                    }
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onScroll (AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if(listIsAtTop()){
                        rlTop.animate().z(0).setStartDelay(0).setDuration(130);
                    } else {
                        rlTop.animate().z(7).setStartDelay(0).setDuration(130);
                    }
                }
            });
        }

    }

    private boolean listIsAtTop() {
        return listPeopleInRoomView.getChildCount() == 0 || listPeopleInRoomView.getChildAt(0).getTop() == 0;
    }

    private void setData(){
        raumName = (TextView) findViewById(R.id.txt_room_number);
        raumName.setText(getString(R.string.room_number, raum.getRaumname()));

        getSupportActionBar().setTitle("Aktive Sitzung in " + raum.getRaumname());

        tag = (TextView) findViewById(R.id.txt_tag);

        if(raum.getTag() != null){
            tag.setText(getString(R.string.tag, raum.getTag().getName()));
        }
        else{
            tag.setText(getString(R.string.tag, "Kein Tag vorhanden"));
        }


        String userPass = RestConnection.id + ":" + RestConnection.token;
        String encoding = Base64.encodeToString(userPass.getBytes(), Base64.DEFAULT);

        String auth = "Basic " + encoding;

        Log.d("edu",auth);

        auth = auth.replace("\n", "").replace("\r", "");

        Ion.with(getApplicationContext())
                .load(raum.getFotoURL())
                .setHeader("Authorization", auth)
                .withBitmap()
                .placeholder(R.drawable.ic_hourglass_empty_black_24dp)
                .error(R.drawable.ic_hourglass_empty_black_24dp)
                .animateIn(android.R.anim.fade_in)
                .intoImageView(imgRoom);



        setTag = (Button) findViewById(R.id.btn_set_tag);
        if(data.getRaum().getTag() == null){
            setTag.setEnabled(true);
        } else {
            setTag.setEnabled(data.isMyTag());
        }


        setTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TagSetActivity.class);
                startActivityForResult(intent, BACK_FROM_TAG);
            }
        });

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

        //long delay = data.getEndzeit()*1000 - System.currentTimeMillis();


        leute = (TextView) findViewById(R.id.txt_people_in_room_cnt);
        int max = raum.getTeilnehmer_max();
        int crnt = raum.getTeilnehmer_aktuell();
        leute.setText(getString(R.string.people_in_room_cnt,crnt,max,getResources().getQuantityString(R.plurals.people_in_room_cnt_anon,crnt-nichtAnonym,crnt-nichtAnonym)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case BACK_FROM_TAG:
                int tagID = data.getIntExtra("id",0);
                new RestConnection(ActiveSessionActivity.this).raumPut(tagID, raum.getId());
                raum = new RestConnection(ActiveSessionActivity.this).raumGet(raum.getId());
                setData();
        }
    }



}

