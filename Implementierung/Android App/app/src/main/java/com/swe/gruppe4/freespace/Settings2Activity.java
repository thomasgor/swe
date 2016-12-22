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

import com.swe.gruppe4.freespace.Objektklassen.*;
import com.swe.gruppe4.freespace.RestConnection;

public class Settings2Activity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_settings2, null, false);
        drawer.addView(contentView, 0);

        Button masterpw = (Button) findViewById(R.id.setMasterPW);
        //ToDo: Checkboxen auf Rückgabewerte von isAnonym() und isPush() des aktuellen Benutzers setzen.
        final EditText passwordEtxt = (EditText) findViewById(R.id.editText);
        final CheckBox anonymChkB = (CheckBox) findViewById(R.id.checkBox);
        final CheckBox pushChkB = (CheckBox) findViewById(R.id.checkBox2);
        final RestConnection verb = new RestConnection(this);
        anonymChkB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(anonymChkB.isChecked()){

                    Benutzer ben = verb.benutzerPut("",1,2);


                }else{
                    Benutzer ben = verb.benutzerPut("",0,2);

                }
            }
        });
        pushChkB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(pushChkB.isChecked()){

                    Benutzer ben = verb.benutzerPut("",2,1);


                }else{
                    Benutzer ben = verb.benutzerPut("",2,0);

                }
            }
        });

        masterpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Benutzer ben = verb.benutzerPut(passwordEtxt.getText().toString(),2,2);
                if(ben.istProfessor() == false) {
                    Toast.makeText(getApplicationContext(),"Falsches Passwort", Toast.LENGTH_LONG).show();
                }else{

                    //ToDo: Eingeloggten Benutzer aktualisieren, im Prof Features verfügbar zu machen
                }
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
}
