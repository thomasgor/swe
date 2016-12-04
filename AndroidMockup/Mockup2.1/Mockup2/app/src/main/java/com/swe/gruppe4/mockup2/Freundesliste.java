package com.swe.gruppe4.mockup2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.swe.gruppe4.mockup2.Objektklassen.*;
import java.util.ArrayList;

public class Freundesliste extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_freundesliste, null, false);
        drawer.addView(contentView, 0);

        ListView friendView = (ListView) findViewById(R.id.friendList);
        FriendListAdapter friendsAdapter = new FriendListAdapter(getApplicationContext(), R.layout.friends_box);
        friendView.setAdapter(friendsAdapter);
        friendView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        Verbindung verbindung = new Verbindung();
        for(Freundschaft f:freunde){
            friendsAdapter.add(f);
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


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        showDialogDelete();
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

}
