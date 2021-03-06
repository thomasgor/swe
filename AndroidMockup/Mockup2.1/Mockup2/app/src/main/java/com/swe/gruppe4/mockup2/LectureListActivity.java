package com.swe.gruppe4.mockup2;

import android.content.Context;
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

public class LectureListActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView lectureView;
    private LectureAdapter lectureAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_lecture_list, null, false);
        drawer.addView(contentView, 0);

        lectureView = (ListView) findViewById(R.id.lectureList);
        lectureAdapter = new LectureAdapter(getApplicationContext(), R.layout.lecture_box);
        lectureView.setAdapter(lectureAdapter);
        lectureView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lectureAdapter.add(new Lecture("Vorlesung SWE","Mo.24.10.2016 12:00-13:00","G101"));
        lectureAdapter.add(new Lecture("Vorlesung GIP","Di.25.10.2016 12:00-13:00","G101"));
        lectureAdapter.add(new Lecture("Übung SWE","Mi.26.10.2016 12:00-13:00","G101"));
        lectureAdapter.add(new Lecture("Übung GIP","Do.27.10.2016 12:00-13:00","G101"));
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
        showDialog();

    }

    private void showDialog(){
        AlertDialog.Builder build = new AlertDialog.Builder(LectureListActivity.this);
        build.setCancelable(false);

        build.setMessage("Möchten Sie die Veranstaltung wirklich löschen?");

        build.setPositiveButton("Ja", null);
        build.setNegativeButton("Nein", null);
        AlertDialog alert1 = build.create();
        alert1.show();
    }
}
