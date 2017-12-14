package com.gosemathraj.proscanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gosemathraj.proscanner.R;

/**
 * Created by iamsparsh on 14/12/17.
 */

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView loadingTxt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        try{
            init();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init() {

        goFullScreen();
        findViewById();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                loadingTxt.setVisibility(View.VISIBLE);
            }
        },1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }

    private void goFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void findViewById() {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        loadingTxt = (TextView) findViewById(R.id.loading_txt);
    }
}
