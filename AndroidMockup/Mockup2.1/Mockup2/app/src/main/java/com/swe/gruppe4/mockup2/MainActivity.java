package com.swe.gruppe4.mockup2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuItemView;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button qrScanner;
    private ListView roomView;
    private RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawer.addView(contentView, 0);
        qrScanner = (Button) findViewById(R.id.button);
        qrScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),QRScanActivity.class);
                startActivity(intent);
            }
        });

        TabHost host = (TabHost) findViewById(R.id.roomTab);
        host.setup();
        TabHost.TabSpec spec = host.newTabSpec("Liste");
        spec.setContent(R.id.listTab);
        spec.setIndicator("Liste");
        host.addTab(spec);

        spec = host.newTabSpec("Karte");
        spec.setContent(R.id.mapTab);
        spec.setIndicator("Karte");
        host.addTab(spec);

        roomView = (ListView) findViewById(R.id.roomList);
        roomAdapter = new RoomAdapter(getApplicationContext(), R.layout.room_box);
        roomView.setAdapter(roomAdapter);
        roomView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        roomAdapter.add(new Room("G101",R.drawable.circle_green,"12/40 belegt",false));
        roomAdapter.add(new Room("G103",R.drawable.circle_green,"8/23 belegt",false));
        roomAdapter.add(new Room("G102",R.drawable.circle_yellow,"25/30 belegt",false));
        roomAdapter.add(new Room("G111",R.drawable.circle_yellow,"17/32 belegt",false));
        roomAdapter.notifyDataSetChanged();
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

    }

}


