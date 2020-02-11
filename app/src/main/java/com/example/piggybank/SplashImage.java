package com.example.piggybank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {

                Intent intent = new Intent ( SplashImage.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        };

        Timer timer = new Timer();
        timer.schedule(timerTask,3000);

    }

}
