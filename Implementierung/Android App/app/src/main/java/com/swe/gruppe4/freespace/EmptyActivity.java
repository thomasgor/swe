package com.swe.gruppe4.freespace;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.swe.gruppe4.freespace.Objektklassen.Sitzung;

/**
 * @author
 * last time modified: 23.12.2016 from Eduard Mantler
 * <p>Dummy Activity für die Navigation</p>
 */
public class EmptyActivity extends AppCompatActivity {

    private int destinationId;
    private int sourceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        destinationId = getIntent().getIntExtra("destinationId", 0);
        sourceId = getIntent().getIntExtra("sourceId", 0);
        showDialog();
        setContentView(R.layout.activity_empty);
    }

    private void showDialog(){
        AlertDialog.Builder build = new AlertDialog.Builder(EmptyActivity.this);
        build.setCancelable(false);
        build.setTitle("Navigation");
        build.setMessage("Bald ist hier eine Karte von Raum " + sourceId + " nach " + destinationId + ".");

        build.setPositiveButton("Ich freu mich drauf!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog alert1 = build.create();
        alert1.show();

        /*
        AlertDialog.Builder build = new AlertDialog.Builder(EmptyActivity.this);
        build.setCancelable(false);
        build.setTitle("G101");
        build.setMessage("12/24 Leute\nTag: Gruppenarbeit");

        build.setPositiveButton("Einchecken", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                VerbindungDUMMY connect = new VerbindungDUMMY();
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
        */
    }
}
