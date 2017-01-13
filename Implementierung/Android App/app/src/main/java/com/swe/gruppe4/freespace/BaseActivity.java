package com.swe.gruppe4.freespace;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.swe.gruppe4.freespace.Objektklassen.*;
import com.koushikdutta.ion.Ion;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * TODO: Alle Seiten auf diesen Drawer verweisen um Redundanz zu verhindern
     * */

    protected DrawerLayout drawer;
    static String profileName = "", profileEmail = "", profileImageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Benutzer ben = AktuellerBenutzer.getAktuellerBenutzer();
        if(ben.istProfessor()) {
            super.setTheme(R.style.AppThemeProf_NoActionBar);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(!ben.istProfessor()) {
            navigationView.getMenu().findItem(R.id.nav_professor).setVisible(false);

        }



        View header=navigationView.getHeaderView(0);

        TextView profileName = (TextView)header.findViewById(R.id.profileName);
        TextView profileEmail = (TextView)header.findViewById(R.id.profileEmail);
        ImageView profileImage = (ImageView) header.findViewById(R.id.profileImage);

        Bundle profileInfo = getIntent().getExtras();
        if(BaseActivity.profileName.equals("")){
            BaseActivity.profileName=(profileInfo.get("profileName").toString());
        }
        if(BaseActivity.profileEmail.equals("")){
            BaseActivity.profileEmail=(profileInfo.get("profileEmail").toString());
        }
        if(BaseActivity.profileImageUrl.equals("")){
            BaseActivity.profileImageUrl=(profileInfo.get("profilePicture").toString());
        }
        profileName.setText(BaseActivity.profileName);
        profileEmail.setText(BaseActivity.profileEmail);

        Ion.with(getApplicationContext())
                .load(BaseActivity.profileImageUrl)
                .withBitmap()
                .placeholder(R.drawable.ic_hourglass_empty_black_24dp)
                .error(R.drawable.nopp)
                .animateIn(android.R.anim.fade_in)
                .intoImageView(profileImage);

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

    public void onBackPressedNoDrawer() {
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_einstellungen) {
            Intent intent = new Intent(getApplicationContext(),Settings2Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_freundesliste) {
            MainActivity.startingPointId=0;
            Intent intent = new Intent(getApplicationContext(),Freundesliste.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_professor_stundenplan) {
            Intent intent = new Intent(getApplicationContext(), LectureListActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_raeume) {
            MainActivity.startingPointId = 0;
            Intent intent = new Intent(getApplicationContext(),RoomActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_view) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setTheme(int resid){
        super.setTheme(resid);
    }

    public static void showEndDialog(Context context){
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.errormsgTitle, RestConnection.lastStatusCode))
                .setMessage("Leider ist ein Fehler aufgetreten.\nBitte starten Sie die App neu.")
                .setPositiveButton("Verstanden", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                })
                .create()
                .show();
    }

}