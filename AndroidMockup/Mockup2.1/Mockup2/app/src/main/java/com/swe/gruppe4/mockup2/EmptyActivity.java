package com.swe.gruppe4.mockup2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.swe.gruppe4.mockup2.Objektklassen.Sitzung;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDialog();
        setContentView(R.layout.activity_empty);
    }

    private void showDialog(){
        AlertDialog.Builder build = new AlertDialog.Builder(EmptyActivity.this);
        build.setCancelable(false);
        build.setTitle("G101");
        build.setMessage("12/24 Leute\nTag: Gruppenarbeit");

        build.setPositiveButton("Einchecken", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Verbindung connect = new Verbindung();
                Sitzung data = connect.sitzungGet();
                Intent intent = new Intent(getApplicationContext(),ActiveSessionActivity.class);
                intent.putExtra("sitzung",data);
                startActivity(intent);
                //finish();
            }
        });
        build.setNegativeButton("Raum suchen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"Unbekannter QR Code", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("profileName","Max Mustermann");
                intent.putExtra("profileEmail","max@mustermann.de");
                intent.putExtra("profilePicture","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg");
                startActivity(intent);
                //finish();
            }
        });
        AlertDialog alert1 = build.create();
        alert1.show();
    }
}
