package com.swe.gruppe4.freespace;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import java.io.InputStream;
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
                .error(R.drawable.ic_hourglass_empty_black_24dp)
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_einstellungen) {
            Intent intent = new Intent(getApplicationContext(),Settings2Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_freundesliste) {
            Intent intent = new Intent(getApplicationContext(),Freundesliste.class);
            startActivity(intent);
        } else if (id == R.id.nav_professor_stundenplan) {
            Intent intent = new Intent(getApplicationContext(), LectureListActivity.class);
            startActivity(intent);

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

}