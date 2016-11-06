package com.swe.gruppe4.mockup2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Freundesliste extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView friendView;
    private FriendListAdapter friendsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freundesliste);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        friendView = (ListView) findViewById(R.id.friendList);
        friendsAdapter = new FriendListAdapter(getApplicationContext(), R.layout.friends_box);
        friendView.setAdapter(friendsAdapter);
        friendView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        for(int i =1; i<10;i++){
            friendsAdapter.add(new PrivateContact("Student"+i,R.drawable.profilepicture,"G10"+i));
        }
        friendsAdapter.notifyDataSetChanged();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Freund hinzufügen", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                showDialogAdd();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        showDialogDelete();
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
        getMenuInflater().inflate(R.menu.freundesliste, menu);
        return true;
    }

    private void showDialogDelete(){
        AlertDialog.Builder build = new AlertDialog.Builder(Freundesliste.this);
        build.setCancelable(false);
        //build.setTitle("Freund wirklich löschen?");
        build.setMessage("Möchten Sie den Freund wirklich löschen?");

        build.setPositiveButton("Ja", null);
        build.setNegativeButton("Nein", null);
        AlertDialog alert1 = build.create();
        alert1.show();
    }

    private void showDialogAdd(){
        AlertDialog.Builder build = new AlertDialog.Builder(Freundesliste.this);
        build.setCancelable(false);

        build.setTitle("Freund hinzufügen");
        build.setMessage("Geben Sie bitte die E-Mail Adresse des Freundes den Sie hinzufügen möchten ein.");
        build.setView(new EditText(Freundesliste.this));

        build.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"Anfrage gesendet", Toast.LENGTH_LONG).show();
            }
        });
        build.setNegativeButton("Abbrechen", null);
        AlertDialog alert1 = build.create();
        alert1.show();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_einstellungen) {
            Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_freundesliste) {
            Intent intent = new Intent(getApplicationContext(),Freundesliste.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_professor_stundenplan) {


        } else if (id == R.id.nav_raeume) {
            Intent intent = new Intent(getApplicationContext(),RoomActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_view) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
