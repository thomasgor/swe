package com.swe.gruppe4.mockup2;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.swe.gruppe4.mockup2.Objektklassen.Sitzung;

import java.io.IOException;

public class QRScanActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int CAMERA_REQUEST = 1337;
    private Camera mCamera = null;
    private CameraView mCameraView = null;
    private ImageButton snapBtn;
    BarcodeDetector detector;
    CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            startCamera();
        }

        ImageButton imgClose = (ImageButton) findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        snapBtn = (ImageButton) findViewById(R.id.button_snap_picture);
        snapBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    snapBtn.setBackgroundResource(R.drawable.camera_snap_circle_clicked);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    qrAnalyze();
                    snapBtn.setBackgroundResource(R.drawable.camera_snap_circle);
                }
                return true;
            }
        });
    }

    private void capture() {
        mCamera.takePicture(null, null, null, new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                /*Toast.makeText(getApplicationContext(), "Picture Taken",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();*/

                Bitmap photo = BitmapFactory.decodeByteArray(data, 0,
                        data.length);

                /*intent.putExtra("image", bmp);
                setResult(RESULT_OK, intent);
                camera.stopPreview();
                if (camera != null) {
                    camera.release();
                    mCamera = null;
                }
                finish();*/

                BarcodeDetector detector =
                        new BarcodeDetector.Builder(getApplicationContext())
                                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                                .build();

                Frame frame = new Frame.Builder().setBitmap(photo).build();
                SparseArray<Barcode> barcodes = detector.detect(frame);

                if (barcodes.size() > 0) {
                    Barcode thisCode = barcodes.valueAt(0);
                    showDialog(thisCode.rawValue);
                } else {
                    Toast.makeText(getApplicationContext(), "Kein QR-Code gefunden", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void releaseCameraAndPreview() {
        ///mCameraView.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void startCamera() {
        try {
            releaseCameraAndPreview();
            mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());


        }

        if (mCamera != null) {
            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout
        } else {
            detector =
                    new BarcodeDetector.Builder(getApplicationContext())
                            .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                            .build();

            if (!detector.isOperational()) {

                AlertDialog.Builder build = new AlertDialog.Builder(QRScanActivity.this);
                build.setCancelable(false);

                build.setTitle("Fehler beim öffnen des Scanners");
                build.setMessage("Probieren Sie bitte, den Scanner noch einmal zu öffnen. Wenn es immer noch nicht klappt, wenden Sie sich bitte an einen Administrator.");

                build.setPositiveButton("Verstanden", null);
                //build.setNegativeButton("Nein", null);
                AlertDialog alert1 = build.create();
                alert1.show();

                finish();

            } else {

                final CameraSource mCameraSource = new CameraSource.Builder(getApplicationContext(), detector)
                        .setFacing(CameraSource.CAMERA_FACING_BACK)
                        .setRequestedFps(15.0f)
                        .build();

                mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder surfaceHolder) {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        try {
                            mCameraSource.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                        mCameraSource.stop();

                    }
                });


                detector.setProcessor(new Detector.Processor<Barcode>() {
                    @Override
                    public void release() {
                    }

                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections) {
                        final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                        if (barcodes.size() != 0) {

                            showDialog(barcodes.valueAt(0).rawValue);
                        }
                    }
                });
            }
        }
    }

    private void qrAnalyze(){
        capture();
        /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("image");

        }
    }

    private void showDialog(String title){
        if(title.equals("W014")){
            AlertDialog.Builder build = new AlertDialog.Builder(QRScanActivity.this);
            build.setCancelable(false);
            build.setTitle(title);
            build.setMessage("5/6 Leute\nTag: Lernen");

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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startCamera();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission


                    AlertDialog.Builder build = new AlertDialog.Builder(QRScanActivity.this);
                    build.setCancelable(false);

                    build.setTitle("Fehlende Berechtigung");
                    build.setMessage("Bitte erlauben Sie den Zugriff auf die Kamera, um den QR Scanner nutzen zu können.");

                    build.setPositiveButton("Verstanden", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    //build.setNegativeButton("Nein", null);
                    AlertDialog alert1 = build.create();
                    alert1.show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
