package com.swe.gruppe4.freespace;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.swe.gruppe4.freespace.Objektklassen.AktuellerBenutzer;
import com.swe.gruppe4.freespace.Objektklassen.Benutzer;
import com.swe.gruppe4.freespace.Objektklassen.Raum;
import com.swe.gruppe4.freespace.Objektklassen.Tag;

/**
 * <p>Überschrift: Struktur von RoomDetailsActivity</p>
 * <p>Beschreibung: Diese Activity dient dazu, die Raumdetails anzuzeigen.
 * </p>
 * <p>Organisation: FH Aachen, FB05, SWE Gruppe 4 </p>
 *
 * @author Merlin
 * @version 1.0
 *
 */
public class RoomDetailsActivity extends AppCompatActivity {

    ImageView imgRoom;
    Raum raum;
    TextView raumName;
    TextView tag;
    ListView listPeopleInRoomView;
    RaumdetailsLeuteAdapter raumLeuteAdapter;
    TextView leute;
    Button gehezu;
    RelativeLayout rlTop;
    ActionBar actionBar;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Benutzer ben = AktuellerBenutzer.getAktuellerBenutzer();
        if(ben.istProfessor()) {
            super.setTheme(R.style.AppThemeProf);
        }

        id = getIntent().getIntExtra("id",4711);
        //raum = new VerbindungDUMMY().raumGet(id);
        raum = new RestConnection(this).raumGet(id);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);
        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }


        imgRoom = (ImageView) findViewById(R.id.img_room_photo);

        //Daten holen
        //raum = (Raum) getIntent().getSerializableExtra("raum");
        setData();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            rlTop=(RelativeLayout) findViewById(R.id.activity_room_details2);
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

    /**
     * Wird benutzt, um zu gucken ob die Liste an der obersten Position steht. Einsatz ist Schattenwurf ab API 21
     *
     * @return Boolean ob die Liste an der obersten Position steht*/
    private boolean listIsAtTop() {
        return listPeopleInRoomView.getChildCount() == 0 || listPeopleInRoomView.getChildAt(0).getTop() == 0;
    }

    /**
     * Verarbeitet die Daten und setzt Strings an die dafür vorgesehenen Stellen
     * */
    private void setData(){
        raumName = (TextView) findViewById(R.id.txt_room_number);

        raumName.setText(getString(R.string.room_number, raum.getRaumname()));
        if(actionBar != null){
            actionBar.setTitle(getString(R.string.room_number, raum.getRaumname()));
        }

        tag = (TextView) findViewById(R.id.txt_tag);
        Tag tag2 = raum.getTag();
        if(raum.getTag() != null) {
            tag.setText(raum.getTag().getName());
        } else {
            tag.setText(R.string.noTag); // TODO eventuell diese Nachricht entfernen
        }

        //tag.setText(getString(R.string.tag, raum.getTag().getName()));

        String userPass = RestConnection.id + ":" + RestConnection.token;
        String encoding = Base64.encodeToString(userPass.getBytes(), Base64.DEFAULT);

        String auth = "Basic " + encoding;

        Log.d("edu",auth);

        auth = auth.replace("\n", "").replace("\r", "");


        Ion.with(getApplicationContext())
                .load(raum.getFotoURL())
                .setHeader("Authorization", auth)
                .withBitmap()
                .placeholder(R.drawable.loadingpic)
                .error(R.drawable.error_loading)
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
        leute.setText(getString(R.string.people_in_room_cnt,crnt,max,getResources().getQuantityString(R.plurals.people_in_room_cnt_anon,crnt-nichtAnonym,crnt-nichtAnonym)));

        gehezu=(Button) findViewById(R.id.btn_goto);
        if(MainActivity.startingPointId != 0){
            gehezu.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_directions_white_24dp, 0,0,0);
            gehezu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),NavigationActivity.class);
                    intent.putExtra("start",MainActivity.startingPointId);
                    intent.putExtra("end",id);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            gehezu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Kein Startpunkt vorhanden: MainActivity.startingPointId = 0!
                    Intent intent = new Intent(getApplicationContext(),QRScanActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(getApplicationContext(),"Das ist noch nicht implementiert" ,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    /**
     * Funktion für den Zurückbutton in der oberen Ecke.
     *
     * @param menuItem gedrückter Knopf
     * @return super.onOptionsItemSelected(menuItem)*/
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}