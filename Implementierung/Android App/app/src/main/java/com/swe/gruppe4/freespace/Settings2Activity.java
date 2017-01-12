package com.swe.gruppe4.freespace;

import android.content.Context;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.swe.gruppe4.freespace.Objektklassen.*;
import com.swe.gruppe4.freespace.RestConnection;

public class Settings2Activity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    int anonFlag, pushFlag;
    CheckBox anonymChkB;
    CheckBox pushChkB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AktuellerBenutzer.getAktuellerBenutzer().istProfessor()){
            super.setTheme(R.style.AppThemeProf);
        }
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_settings2, null, false);
        drawer.addView(contentView, 0);

        Button masterpw = (Button) findViewById(R.id.setMasterPW);
        final EditText passwordEtxt = (EditText) findViewById(R.id.editText);

        if(AktuellerBenutzer.getAktuellerBenutzer().istProfessor()) {
            masterpw.setVisibility(View.GONE);
            passwordEtxt.setVisibility(View.GONE);
        } else {
            masterpw.setVisibility(View.VISIBLE);
            passwordEtxt.setVisibility(View.VISIBLE);
        }

        anonymChkB = (CheckBox) findViewById(R.id.checkBox);
        pushChkB = (CheckBox) findViewById(R.id.checkBox2);
        Benutzer ben = AktuellerBenutzer.getAktuellerBenutzer();
        anonymChkB.setChecked(ben.isAnonymous());
        pushChkB.setChecked(ben.isPush());


        if(ben.isAnonymous()){
            anonFlag = 1;
        }else{
            anonFlag = 0;
        }

        if(ben.isPush()){
            pushFlag = 1;
        }else{
            pushFlag = 0;
        }

        final RestConnection verb = new RestConnection(this);
        //final RestConnection verb = new RestConnection(this);

        anonymChkB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(anonymChkB.isChecked()){
                    Benutzer ben = verb.benutzerPut("",1,pushFlag);
                    AktuellerBenutzer.setAktuellerBenutzer(ben);
                    anonFlag = 1;

                }else{
                    Benutzer ben = verb.benutzerPut("",0,pushFlag);
                    AktuellerBenutzer.setAktuellerBenutzer(ben);
                    anonFlag = 0;
                }
            }
        });
        pushChkB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(pushChkB.isChecked()){
                    Benutzer ben = verb.benutzerPut("",anonFlag,1);
                    AktuellerBenutzer.setAktuellerBenutzer(ben);
                    pushFlag = 1;
                }else{
                    Benutzer ben = verb.benutzerPut("",anonFlag,0);
                    AktuellerBenutzer.setAktuellerBenutzer(ben);
                    pushFlag = 0;
                }
            }
        });

        masterpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Benutzer ben = verb.benutzerPut(passwordEtxt.getText().toString(),anonFlag,pushFlag);
                if(ben.istProfessor() == false) {
                    Toast.makeText(getApplicationContext(),"Falsches Passwort", Toast.LENGTH_LONG).show();
                }else{
                    AktuellerBenutzer.setAktuellerBenutzer(ben);
                    anonymChkB.setChecked(ben.isAnonymous());
                    pushChkB.setChecked(ben.isPush());

                    //Settings2Activity.this.finish();
                    //final Intent intent = Settings2Activity.this.getIntent();
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //Settings2Activity.this.startActivity(intent);

                    Intent intent = new Intent(Settings2Activity.this.getApplicationContext(), Settings2Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Settings2Activity.this.startActivity(intent);

                    //Intent intent = getIntent();
                    //finish();
                    //startActivity(intent);

                }
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }
}
