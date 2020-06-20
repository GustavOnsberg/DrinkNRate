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
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
    private String scaned = "";
    private View root;


    private static final int REQUEST_CAMERA_PERMISSION = 13;


    private ScanViewModel scanViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scanViewModel =
                ViewModelProviders.of(this).get(ScanViewModel.class);
        root = inflater.inflate(R.layout.fragment_scan, container, false);
        scanViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });


        surfaceView = root.findViewById(R.id.surface_view);
        progressBar = root.findViewById(R.id.progress_bar);
        progressBar.setMax(scanTarget);

        initDetsAndSources();
        return root;
    }
/*
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //surfaceView = getActivity().findViewById(R.id.surface_view);
        //barcodeText = getActivity().findViewById(R.id.barcode_text);

        surfaceView = getView().findViewById(R.id.surface_view);
        barcodeText = getView().findViewById(R.id.barcode_text);

        initDetsAndSources();

    }*/

    private void initDetsAndSources() {  // initialises detectors and sources

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
                // insures that the proper permissions have benn granted.
                try {
                    if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(surfaceView.getHolder());

                    }else{
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

                    }

                }catch (IOException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcode = detections.getDetectedItems();
                if (barcode.size() !=0){

                    barcodeData = barcode.valueAt(0).displayValue;
                    if(barcodeData.equals(scaned)){
                        scanCounter++;
                        if(scanCounter == scanTarget){
                            ((MainActivity)getActivity()).barcodeNumber = scaned;
                            ((MainActivity)getActivity()).findDrink(root);
                        }
                    }
                    else{
                        scaned = barcodeData;
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