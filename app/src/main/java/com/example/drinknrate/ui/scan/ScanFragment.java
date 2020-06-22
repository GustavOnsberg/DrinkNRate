package com.example.drinknrate.ui.scan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.drinknrate.MainActivity;
import com.example.drinknrate.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanFragment extends Fragment {

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceView surfaceView;
    private ProgressBar progressBar;
    private String barcodeData;

    private int scanCounter = 0;
    private int scanTarget = 10;
    private String scanned = "";
    private View root;

    private static final int REQUEST_CAMERA_PERMISSION = 13;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //sets the view as the fragment
        root = inflater.inflate(R.layout.fragment_scan, container, false);

        //find views
        surfaceView = root.findViewById(R.id.surface_view);
        progressBar = root.findViewById(R.id.progress_bar);
        progressBar.setMax(scanTarget);

        //Initialises detectors
        initDetsAndSources();
        return root;
    }

    private void initDetsAndSources() {

        barcodeDetector = new BarcodeDetector.Builder(getActivity())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(getActivity(), barcodeDetector)
                .setRequestedPreviewSize(1024, 1024)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                //Check for permissions
                try {
                    if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(surfaceView.getHolder());
                    }else{
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }
            //Happens when scanner found something
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcode = detections.getDetectedItems();
                if (barcode.size() !=0){
                    //Get scan result
                    barcodeData = barcode.valueAt(0).displayValue;
                    //Count equal scans in a row
                    if(barcodeData.equals(scanned)){
                        scanCounter++;
                        if(scanCounter == scanTarget){
                            //Go to drink
                            ((MainActivity)getActivity()).barcode = scanned;
                            ((MainActivity)getActivity()).findDrink(root);
                        }
                    }
                    else{
                        scanned = barcodeData;
                        scanCounter = 0;
                    }

                    progressBar.setProgress(scanCounter);
                }
                else{
                    scanCounter = 0;
                }
            }
        });

    }

}