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

import com.swe.gruppe4.freespace.Objektklassen.Benutzer;

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
        //ToDo
        final EditText passwordEtxt = (EditText) findViewById(R.id.editText);
        final CheckBox anonymChkB = (CheckBox) findViewById(R.id.checkBox);
        anonymChkB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(anonymChkB.isChecked()){

                    //v.putAnonymerStatus();
                    //ToDo: Funktion um anonymen Status zu setzen (in RestConnection.java)

                }else{
                    System.out.println("Un-Checked");
                }
            }
        });
        masterpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RestConnection v = new RestConnection(getApplicationContext());
                Benutzer ben = v.benutzerPut(passwordEtxt.getText().toString());
                if(ben.istProfessor() == false) {
                    Toast.makeText(getApplicationContext(),"Falsches Passwort", Toast.LENGTH_LONG).show();
                }else{

                    //ToDo: "refresh" current user
                }
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
}
