package com.example.dailydone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MakeItEasy();
                startHomePage();
            }
        });
        thread.start();
    }

    public void MakeItEasy(){
        try {
            Thread.sleep(1500);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startHomePage(){
        Intent it = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(it);
        finish();
    }
}
