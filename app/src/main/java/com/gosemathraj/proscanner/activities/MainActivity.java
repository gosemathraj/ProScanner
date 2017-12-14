package com.gosemathraj.proscanner.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gosemathraj.proscanner.R;
import com.gosemathraj.proscanner.utility.Util;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private SharedPreferences permissionStatus;

    private TextView resultText;
    private View clickedView;

    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            init();
            if(savedInstanceState != null){
                resultText.setText(savedInstanceState.getString("savedData"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init() {
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
        findViewById();
    }

    public void scan(View view) {

        clickedView = view;
        managePermission();
    }

    private void findViewById() {
        resultText = (TextView) findViewById(R.id.scanned_text);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("savedData", result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            if(requestCode == 0){
                if(data != null){
                    result = data.getStringExtra("data");
                    resultText.setText(result);
                    Log.e("Result text",result);
                }
            }

            if (requestCode == REQUEST_PERMISSION_SETTING) {
                if (checkForPermissions()) {
                    proceedAfterPermission();
                }
            }
        }
    }

    /*-----------------------------------------Permissions------------------------------------------------------------*/
    private boolean checkForPermissions() {

        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        return  camera  == PackageManager.PERMISSION_GRANTED ;
    }

    private void checkRequestRationalePermissions() {

        if(requestRequestRationalePermission() == true){
            requestPermissions();
        }else{
            if(Util.getInstance().getBoolPref(this,"isFirstTimeForPermissions")){
                requestPermissions();
            }else{
                Util.getInstance().showToast(this,getString(R.string.permission_denied_permanently));
            }
        }
    }

    public void requestPermissions(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_CONSTANT);
    }

    private boolean requestRequestRationalePermission() {
        return ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA);
    }
    /*-----------------------------------------Permissions------------------------------------------------------------*/



    private void managePermission(){
        if (!checkForPermissions()) {
            if (requestRequestRationalePermission()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Camera Permission needed");
                builder.setMessage("This app needs camera permission to scan Qr Codes");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        requestPermissions();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.CAMERA,false)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Camera Permission needed");
                builder.setMessage("This app needs camera permission to scan Qr Codes");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                requestPermissions();
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.CAMERA,true);
            editor.commit();

        } else {
            proceedAfterPermission();
        }
    }

    private void proceedAfterPermission() {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        if(clickedView.getId() == R.id.front_scan){
            intent.putExtra("cameraId",1);              // front camera
        }else if(clickedView.getId() == R.id.rear_scan){
            intent.putExtra("cameraId",0);              // rear camera
        }
        startActivityForResult(intent,0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                proceedAfterPermission();
            } else {
                if (requestRequestRationalePermission()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Camera Permission needed");
                    builder.setMessage("This app needs camera permission to scan Qr Codes");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            requestPermissions();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
