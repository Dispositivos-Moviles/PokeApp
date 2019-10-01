package com.nickgonzalezs.todolist;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.security.Permission;

public class QRReaderActivity extends AppCompatActivity implements Detector.Processor<Barcode> {

    //Variables custom
    private final String TAG = getClass().getSimpleName();
    private BarcodeDetector barcodeDetector;
    private SurfaceView svCamera;
    private CameraSource cameraSource;

    private static final int CAMERA_PERMISSION_REQUEST = 1024;
    private AppCompatActivity appCompatActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_qrreader);


        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        barcodeDetector.setProcessor(this);

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1024, 720)
                .setAutoFocusEnabled(true)
                .build();

        appCompatActivity = this;

        svCamera = findViewById(R.id.sv_camara);

        svCamera.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(appCompatActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(appCompatActivity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                            return;
                        }

                        cameraSource.start(svCamera.getHolder());

                    }
                } catch (IOException e) {
                    Log.e(TAG, "CAMARA ERROR " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

    }

    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        SparseArray<Barcode> barcodes = detections.getDetectedItems();

        if (barcodes.size() > 0) {

            final String readed = barcodes.valueAt(0).rawValue;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Vibrator vibrator = (Vibrator) appCompatActivity.getSystemService(VIBRATOR_SERVICE);
//                    Toast.makeText(appCompatActivity, readed, Toast.LENGTH_LONG).show();

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        vibrator.vibrate(VibrationEffect.createOneShot(2, VibrationEffect.DEFAULT_AMPLITUDE));
//                    } else {
//                        vibrator.vibrate(2);
//                    }

                    Intent i = new Intent(appCompatActivity, MainActivity.class);
                    i.putExtra("qrcode", readed);
                    appCompatActivity.setResult(RESULT_OK, i);
                    appCompatActivity.finish();
                }
            });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        cameraSource.stop();
    }
}
