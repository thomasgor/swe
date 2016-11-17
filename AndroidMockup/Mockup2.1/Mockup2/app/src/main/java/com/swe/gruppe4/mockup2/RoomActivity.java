package com.swe.gruppe4.mockup2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.AbsListView;
import android.widget.ListView;

public class RoomActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView roomView;
    private RoomAdapter roomAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_room, null, false);
        drawer.addView(contentView, 0);

        roomView = (ListView) findViewById(R.id.roomList);
        roomAdapter = new RoomAdapter(getApplicationContext(), R.layout.room_box);
        roomView.setAdapter(roomAdapter);
        roomView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        for(int i=0; i< 3;i++){
            roomAdapter.add(new Room("G10"+i,R.drawable.circle_green,"Belegung: 5/22",false));
        }
        for(int i=3; i< 6;i++){
            roomAdapter.add(new Room("G10"+i,R.drawable.circle_yellow,"Belegung: 15/28",false));
        }
        for(int i=6; i< 9;i++){
            roomAdapter.add(new Room("G10"+i,R.drawable.circle_red,"Belegung: 25/30",false));
        }
        roomAdapter.notifyDataSetChanged();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.filter_tags){
            Intent intent = new Intent(getApplicationContext(), TagsFilterActivity.class);
            startActivity(intent);
        }

        /*//noinspection SimplifiableIfStatement
        if (id == R.id.filter_tags) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
