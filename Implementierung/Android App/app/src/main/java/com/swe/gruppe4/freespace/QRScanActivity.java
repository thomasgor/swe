package com.swe.gruppe4.freespace;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

//import com.google.android.gms.vision.CameraSource;
//import com.google.android.gms.vision.Detector;
//import com.google.android.gms.vision.barcode.Barcode;
//import com.google.android.gms.vision.barcode.BarcodeDetector;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.swe.gruppe4.freespace.Objektklassen.*;
import java.util.ArrayList;

public class QRScanActivity extends AppCompatActivity {
    private Button scan_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        final Activity activity = this;

        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "Scanvorgang abgebrochen", Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                //Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
                Verbindung verbindung = new Verbindung();
                Raum meinRaum = verbindung.raumGet(Integer.parseInt(result.getContents()));

                if(meinRaum == null) {
                    Toast.makeText(this, "Unbekannter QR Code" ,Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    showDialog(meinRaum);
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showDialog(final Raum meinRaum){
        AlertDialog.Builder build = new AlertDialog.Builder(QRScanActivity.this);
        build.setCancelable(false);
        build.setTitle(meinRaum.getRaumname());
        build.setMessage( meinRaum.getTeilnehmer_aktuell() + "/" + meinRaum.getTeilnehmer_max() + " Leute\nTag: " + meinRaum.getTag().getName() );

        build.setPositiveButton("Einchecken", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Verbindung connect = new Verbindung();
                Sitzung data = connect.sitzungGet();
                Intent intent = new Intent(getApplicationContext(),ActiveSessionActivity.class);
                intent.putExtra("sitzung",new Sitzung(4711,meinRaum,false,(System.currentTimeMillis()/1000L)+2700));
                startActivity(intent);
                //finish();
            }
        });
        build.setNegativeButton("Raum suchen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(getApplicationContext(),"Unbekannter QR Code", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(),RoomActivity.class);
                //intent.putExtra("profileName","Max Mustermann");
                //intent.putExtra("profileEmail","max@mustermann.de");
                //intent.putExtra("profilePicture","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg");
                intent.putExtra("raumliste",(ArrayList<Raum>) getIntent().getSerializableExtra("raumliste"));
                startActivity(intent);
                //finish();
            }
        });
        AlertDialog alert1 = build.create();
        alert1.show();
    }

}
