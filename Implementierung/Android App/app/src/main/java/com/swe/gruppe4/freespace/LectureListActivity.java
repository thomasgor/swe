package com.swe.gruppe4.freespace;

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

import com.swe.gruppe4.freespace.Objektklassen.AktuellerBenutzer;
import com.swe.gruppe4.freespace.Objektklassen.Veranstaltung;

public class LectureListActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView lectureView;
    private LectureAdapter lectureAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        ArrayList<Veranstaltung> veranstaltungen = new RestConnection(this).lecturesGet();


        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_lecture_list, null, false);
        drawer.addView(contentView, 0);

        lectureView = (ListView) findViewById(R.id.lectureList);
        lectureAdapter = new LectureAdapter(getApplicationContext(), R.layout.lecture_box, this);
        lectureView.setAdapter(lectureAdapter);
        lectureView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);


        for(Veranstaltung v:veranstaltungen){

                lectureAdapter.add(v);

        }
        lectureAdapter.notifyDataSetChanged();





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


    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
