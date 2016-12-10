package com.swe.gruppe4.freespace;

import android.Manifest;
import android.content.DialogInterface;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class QRScanActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private Camera mCamera = null;
    private CameraView mCameraView = null;
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

        ImageButton imgClose = (ImageButton)findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

    private void startCamera(){
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

                CameraSource mCameraSource = new CameraSource.Builder(getApplicationContext(), detector)
                        .setFacing(CameraSource.CAMERA_FACING_BACK)
                        .setRequestedFps(15.0f)
                        .build();

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
                detector.setProcessor(new Detector.Processor<Barcode>() {
                    @Override
                    public void release() {
                    }

                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections) {
                        final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                        if (barcodes.size() != 0) {

                            AlertDialog.Builder build = new AlertDialog.Builder(QRScanActivity.this);
                            build.setCancelable(false);

                            build.setTitle(barcodes.valueAt(0).displayValue);

                            build.setPositiveButton("Verstanden", null);
                            //build.setNegativeButton("Nein", null);
                            AlertDialog alert1 = build.create();
                            alert1.show();
                        }
                    }
                });
            }
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
