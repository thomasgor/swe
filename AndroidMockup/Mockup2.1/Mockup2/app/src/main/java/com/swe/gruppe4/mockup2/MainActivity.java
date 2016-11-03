package com.swe.gruppe4.mockup2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuItemView;
import android.support.v7.app.ActionBar;
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
import android.content.Intent;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button qrScanner;
    private ListView roomView;
    private RoomAdapter roomAdapter;
    private TextView profileName;
    private DrawerLayout mDrawerLayout;
    private TextView profileEmail;
    private ImageView profileImage;
    private String profileImageURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        qrScanner = (Button) findViewById(R.id.button);
        qrScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EmptyActivity.class);
                startActivity(intent);
                finish();
            }
        });
        setSupportActionBar(toolbar);

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        TextView profileName = (TextView)header.findViewById(R.id.profileName);
        TextView profileEmail = (TextView)header.findViewById(R.id.profileEmail);
        ImageView profileImage = (ImageView) header.findViewById(R.id.profileImage);

        Bundle profileInfo = getIntent().getExtras();
        profileName.setText(profileInfo.get("profileName").toString());
        profileEmail.setText(profileInfo.get("profileEmail").toString());
        String profileImageURL = profileInfo.get("profilePicture").toString();

        new LoadProfileImage(profileImage).execute(profileImageURL);

        roomView = (ListView) findViewById(R.id.roomList);
        roomAdapter = new RoomAdapter(getApplicationContext(), R.layout.room_box);
        roomView.setAdapter(roomAdapter);
        roomView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        roomAdapter.add(new Room("G101",R.drawable.circle_green,"12/40 belegt",false));
        roomAdapter.add(new Room("G103",R.drawable.circle_green,"8/23 belegt",false));
        roomAdapter.add(new Room("G102",R.drawable.circle_yellow,"25/30 belegt",false));
        roomAdapter.add(new Room("G111",R.drawable.circle_yellow,"17/32 belegt",false));
        roomAdapter.notifyDataSetChanged();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
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
            Intent intent = new Intent(getApplicationContext(),RoomActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_view) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}


