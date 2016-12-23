package com.swe.gruppe4.freespace;


import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.swe.gruppe4.freespace.Objektklassen.Benutzer;
import com.swe.gruppe4.freespace.Objektklassen.Raum;

/**
 * @author
 * last time modified: 23.12.2016 from Eduard Mantler
 * <p>Zeigt die Raumdetails an</p>
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int id = getIntent().getIntExtra("id",4711);
        raum = new VerbindungDUMMY().raumGet(id);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imgRoom = (ImageView) findViewById(R.id.img_room_photo);

        //Daten holen
        //raum = (Raum) getIntent().getSerializableExtra("raum");

        setData();

        gehezu=(Button) findViewById(R.id.btn_goto);
        gehezu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kein Startpunkt vorhanden: MainActivity.startingPointId = 0!
                Intent intent = new Intent(getApplicationContext(),QRScanActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),"Das ist noch nicht implementiert" ,Toast.LENGTH_SHORT).show();
            }
        });

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

    private boolean listIsAtTop() {
        return listPeopleInRoomView.getChildCount() == 0 || listPeopleInRoomView.getChildAt(0).getTop() == 0;
    }

    private void setData(){
        raumName = (TextView) findViewById(R.id.txt_room_number);
        raumName.setText(getString(R.string.room_number, raum.getRaumname()));
        getSupportActionBar().setTitle(getString(R.string.room_number, raum.getRaumname()));

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
        leute.setText(getString(R.string.people_in_room_cnt,crnt,max,getResources().getQuantityString(R.plurals.people_in_room_cnt_anon,crnt-nichtAnonym,crnt-nichtAnonym)));
    }
}