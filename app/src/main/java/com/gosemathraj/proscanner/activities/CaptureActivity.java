package com.gosemathraj.proscanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by iamsparsh on 14/12/17.
 */

public class CaptureActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private static final String TAG = "Pro Scanner";
    private int CAMERA_ID = 0;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        checkIntent();
    }

    private void checkIntent() {
        if(getIntent() != null){
            CAMERA_ID = getIntent().getIntExtra("cameraId",0);
        }
        mScannerView.startCamera(CAMERA_ID);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.v(TAG, rawResult.getText());
        Log.v(TAG, rawResult.getBarcodeFormat().toString());

        mScannerView.stopCamera();

        Intent intent=new Intent();
        intent.putExtra("data",rawResult.getText().toString());
        setResult(0,intent);
        finish();
    }
}
