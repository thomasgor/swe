package com.swe.gruppe4.freespace;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.koushikdutta.async.http.body.StringBody;
import com.koushikdutta.ion.Ion;
import com.swe.gruppe4.freespace.Objektklassen.RoomEnterance;

import java.util.ArrayList;

public class NavigationActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener  {
    private MapView mapView;
    private ArrayList<RoomEnterance> roomEnterance;
    private Button qrScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_navigation, null, false);
        drawer.addView(contentView, 0);
        String startPoint = "";
        String endPoint = "";
        int startPointExtra = getIntent().getIntExtra("start",7);
        int endPointExtra = getIntent().getIntExtra("end",1);
        roomEnterance =  new RestConnection(this).raumEingangGet();
        for(int i=0; i< roomEnterance.size();i++){
            if(roomEnterance.get(i).getId() == startPointExtra){
                startPoint = roomEnterance.get(i).getName();
            }
        }
        for(int i=0; i< roomEnterance.size();i++){
            if(roomEnterance.get(i).getId() == endPointExtra){
                endPoint = roomEnterance.get(i).getName();
            }
        }
        mapView = (MapView) findViewById(R.id.navView);
        Ion.with(getApplicationContext())
                .load("http://i.imgur.com/8F3lTML.png")
                //.setHeader("Authorization", auth)
                .withBitmap()
                .placeholder(R.drawable.ic_hourglass_empty_black_24dp)
                .error(R.drawable.ic_hourglass_empty_black_24dp)
                .animateIn(android.R.anim.fade_in)
                .intoImageView(mapView);
        MapView.startNavigation(startPoint,endPoint);
        mapView.invalidate();

        qrScanner = (Button) findViewById(R.id.button);

        qrScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),QRScanActivity.class);
                //ArrayList<Raum> roomListFromConnection = new ArrayList<>(connection.raumGet());
                //intent.putExtra("raumliste",roomListFromConnection);
                startActivity(intent);
                finish();
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
}
