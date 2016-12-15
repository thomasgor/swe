package com.swe.gruppe4.mockup2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;

import com.swe.gruppe4.mockup2.Objektklassen.Veranstaltung;

public class LectureListActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView lectureView;
    private LectureAdapter lectureAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<Veranstaltung> veranstaltungen = new Verbindung().lecturesGet();
        Veranstaltung data;
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_lecture_list, null, false);
        drawer.addView(contentView, 0);

        lectureView = (ListView) findViewById(R.id.lectureList);
        lectureAdapter = new LectureAdapter(getApplicationContext(), R.layout.lecture_box);
        lectureView.setAdapter(lectureAdapter);
        lectureView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);


        for(Veranstaltung v:veranstaltungen){
            lectureAdapter.add(v);
        }
        lectureAdapter.notifyDataSetChanged();

/*
        lectureView.setClickable(true);
        lectureView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),LectureEditActivity.class);

                //ToDo: Passende ID übergeben
                intent.putExtra("ID", l);
                startActivity(intent);
            }
        });
*/



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddLectureActivity.class);
                startActivity(intent);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //showDialog();

    }

    private void showDialog(){
        AlertDialog.Builder build = new AlertDialog.Builder(LectureListActivity.this);
        build.setCancelable(false);

        build.setMessage("Möchten Sie die Veranstaltung wirklich löschen?");

        build.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"Veranstaltung gelöscht", Toast.LENGTH_LONG).show();
                //TODO: ID der angeklickten Veranstaltung auslesen
                //new Verbindung().lectureDelete(id);
            }
        });

        build.setNegativeButton("Nein", null);
        AlertDialog alert1 = build.create();
        alert1.show();
    }
}
