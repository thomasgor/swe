package com.swe.gruppe4.mockup2;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRScanActivity extends AppCompatActivity {

    private Camera mCamera = null;
    private CameraView mCameraView = null;
    BarcodeDetector detector;
    CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        try {
            releaseCameraAndPreview();
            mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
            AlertDialog.Builder build = new AlertDialog.Builder(QRScanActivity.this);
            build.setCancelable(false);

            build.setTitle("Fehlende Berechtigung");
            build.setMessage("Bitte gehen Sie in die Einstellungen und erlauben Sie der App, auf die Kamera zuzugreifen.");

            build.setPositiveButton("Verstanden", null);
            //build.setNegativeButton("Nein", null);
            AlertDialog alert1 = build.create();
            alert1.show();
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
                            //build.setMessage("Probieren Sie bitte, den Scanner noch einmal zu öffnen. Wenn es immer noch nicht klappt, wenden Sie sich bitte an einen Administrator.");

                            build.setPositiveButton("Verstanden", null);
                            //build.setNegativeButton("Nein", null);
                            AlertDialog alert1 = build.create();
                            alert1.show();
                        }
                    }
                });
            }
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
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
