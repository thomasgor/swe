package com.swe.gruppe4.freespace;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.swe.gruppe4.freespace.Objektklassen.*;
import java.util.ArrayList;

/**
 * @author Eduard Mantler
 * last time modified: 23.12.2016 from Eduard Mantler
 * <p>QR-Scanner von der Startseite</p>
 */
public class QRScanActivity extends AppCompatActivity {
    private Button scan_btn;
    //private int context = getIntent().getIntExtra("RoomId", 0);
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        id = getIntent().getIntExtra("id", 0);
        final Activity activity = this;

        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(false);
        integrator.setTimeout(20000);
        integrator.initiateScan();



    }

    /**
     * onActivityResult: verwaltet die Eingaben des QR-Scanner
     * @param requestCode Anfrage Id
     * @param resultCode  Ergebnis Id
     * @param data        Ergebnis Daten
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null){
                cancelledScan();
            }
            else {
                if ( isInteger(result.getContents())) {
                    RestConnection verbindung = new RestConnection(this);
                    Raum meinRaum = verbindung.raumGet(Integer.parseInt(result.getContents()));

                    if(meinRaum == null) {
                        invalidQRCode();
                    }
                    else {
                        Log.d("edu", "QRScan: ");
                        if(id == 0) { //!= NULL?? 0 Id in DB? // id == 0, wenn QR scanner NICHT von den Raumdetails "Gehe Zu" aufgerufen wurde
                            Log.d("edu", "QRScan: id = 0");
                            showDialog(meinRaum);
                        } else {
                            Log.d("edu", "QRScan: id != 0");
                            Intent intent = new Intent(getApplicationContext(),NavigationActivity.class);
                            intent.putExtra("end", meinRaum.getId());
                            intent.putExtra("start", id);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                else {
                    invalidQRCode();
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * showDialog: zeigt die Auswahl: "Raum suchen", "Einchecken" nach erfolgreichen Scan
     * @param meinRaum Raum Objekt des gescannten Raumes
     */
    private void showDialog(final Raum meinRaum){
        MainActivity.startingPointId = meinRaum.getId();
        AlertDialog.Builder build = new AlertDialog.Builder(QRScanActivity.this);
        build.setCancelable(false);
        build.setTitle(meinRaum.getRaumname());

        String tagMessage = "";
        if(meinRaum.getTag() == null) {
            tagMessage = "Tag: Kein Tag gesetzt";
        } else {
            tagMessage = "Tag: " + meinRaum.getTag().getName();
        }
        build.setMessage( meinRaum.getTeilnehmer_aktuell()  + "/" + meinRaum.getTeilnehmer_max()
                                                            + " Leute\n" + tagMessage );


        build.setPositiveButton("Einchecken", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RestConnection connect = new RestConnection(QRScanActivity.this);
                Sitzung data = connect.sitzungPost(meinRaum.getId());
                Intent intent = new Intent(getApplicationContext(),ActiveSessionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        build.setNegativeButton("Raum suchen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(),RoomActivity.class);
                //intent.putExtra("raumliste",(ArrayList<Raum>) getIntent().getSerializableExtra("raumliste"));
                startActivity(intent);
                finish();
            }
        });

        AlertDialog alert1 = build.create();

        alert1.show();

    }

    /**
     * invalidQRCode: Meldung falls der QR-Code nicht erkannt wurde
     */
    private void invalidQRCode() {
        Toast.makeText(this, "Unbekannter QR Code" ,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * cancelledScan: Meldung falls der Scanvorgang abgebrochen wurde
     */
    private void cancelledScan() {
        Toast.makeText(this, "Scanvorgang abgebrochen" ,Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * isInteger: Prüft ob ein String in ein Integer convertiert werden kann
     * @param str der zu prüfende String
     * @return True, falls der String in einen Integer convertiert werden kann
     */
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

}

