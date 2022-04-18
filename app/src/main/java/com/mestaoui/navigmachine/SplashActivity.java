package com.mestaoui.navigmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    private ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);

        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(500);
        animation1.setStartOffset(1000);
        animation1.setFillAfter(true);
        logo.startAnimation(animation1);

        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
}