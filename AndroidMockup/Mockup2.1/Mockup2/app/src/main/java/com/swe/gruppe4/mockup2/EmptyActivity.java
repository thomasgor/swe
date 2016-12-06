package com.swe.gruppe4.mockup2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.swe.gruppe4.mockup2.Objektklassen.Raum;
import com.swe.gruppe4.mockup2.Objektklassen.Sitzung;

public class EmptyActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * Die Methode entkommentieren um QR Scanner zu benutzen
         * */
        /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);*/


        super.onCreate(savedInstanceState);

        /**
         * ShowDialog auskommentieren wenn QR Scanner benutzt wird
         * */
        showDialog("abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz");
        setContentView(R.layout.activity_empty);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            BarcodeDetector detector =
                    new BarcodeDetector.Builder(getApplicationContext())
                            .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                            .build();

            Frame frame = new Frame.Builder().setBitmap(photo).build();
            SparseArray<Barcode> barcodes = detector.detect(frame);

            if(barcodes.size()>0) {
                Barcode thisCode = barcodes.valueAt(0);
                showDialog(thisCode.rawValue);
            } else {
                Toast.makeText(getApplicationContext(),"Kein QR-Code gefunden",Toast.LENGTH_SHORT).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }

        }
    }

    private void showDialog(String title){
        if(title.equals("abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz")){
            AlertDialog.Builder build = new AlertDialog.Builder(EmptyActivity.this);
            build.setCancelable(false);
            build.setTitle("W014");
            build.setMessage("5/8 Leute\nTag: Lernen");

            build.setPositiveButton("Einchecken", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Verbindung connect = new Verbindung();
                    Sitzung data = connect.sitzungGet();
                    Intent intent = new Intent(getApplicationContext(),ActiveSessionActivity.class);
                    intent.putExtra("sitzung",data);

                    /*Raum raum = connect.raumGet(4711);
                    Intent intent = new Intent(getApplicationContext(),RoomDetailsActivity.class);
                    intent.putExtra("raum",raum);*/
                    startActivity(intent);
                    finish();
                }
            });
            build.setNegativeButton("Raum suchen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Toast.makeText(getApplicationContext(),"Unbekannter QR Code", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(),RoomActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            AlertDialog alert1 = build.create();
            alert1.show();
        } else {
            Toast.makeText(getApplicationContext(),"Unbekannter QR Code", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
