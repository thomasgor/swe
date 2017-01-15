package com.swe.gruppe4.freespace;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.swe.gruppe4.freespace.Objektklassen.*;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemClickListener;

public class Freundesliste extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static ArrayList<Freundschaft> freunde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("edu", "In der Freundesliste!");
        freunde = new RestConnection(this).freundschaftGet();
        Freundschaft data;
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_freundesliste, null, false);
        drawer.addView(contentView, 0);

        ListView requestView = (ListView) findViewById(R.id.friendRequestList);
        ListView friendView = (ListView) findViewById(R.id.friendList);

        FriendRequestAdapter requestAdapter = new FriendRequestAdapter(this, R.layout.friends_request_box);
        final FriendListAdapter friendsAdapter = new FriendListAdapter(this, R.layout.friends_box);
        requestView.setAdapter(requestAdapter);
        friendView.setAdapter(friendsAdapter);
        requestView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        friendView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        for(Freundschaft f:freunde){
            if(f.isStatus() == true)
                friendsAdapter.add(f);
            else
                requestAdapter.add(f);
        }

        requestAdapter.notifyDataSetChanged();
        friendsAdapter.notifyDataSetChanged();
        if(!requestAdapter.isEmpty()) {
            TextView tw = (TextView) findViewById(R.id.freundschaftsanfragen);
            tw.setVisibility(View.VISIBLE);
        }

        friendView.setClickable(true);
        friendView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),RoomDetailsActivity.class);
                int id = friendsAdapter.getFreund(i).getRaum().getId();
                intent.putExtra("id",friendsAdapter.getFreund(i).getRaum().getId());

                startActivity(intent);
            }
        });
        //Benutzer hinzufügen Button
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

    }


    /**
     * Diese Methode wird dazu verwendet, um einen neuen Dialog für das hinzufügen eines neuen Freundes anzuzeigen
     */
    private void showDialogAdd(){
        AlertDialog.Builder build = new AlertDialog.Builder(Freundesliste.this);
        build.setCancelable(false);

        build.setTitle("Freund hinzufügen");
        build.setMessage("Geben Sie bitte die E-Mail Adresse des Freundes den Sie hinzufügen möchten ein.");
        final EditText email = new EditText(Freundesliste.this);
        build.setView(email);


        build.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"Anfrage gesendet", Toast.LENGTH_LONG).show();
                new RestConnection(Freundesliste.this).freundschaftPost(email.getText().toString());
            }
        });
        build.setNegativeButton("Abbrechen", null);
        AlertDialog alert1 = build.create();
        alert1.show();
    }




}
