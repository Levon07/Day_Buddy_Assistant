package com.example.daybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView day = findViewById(R.id.day);
        ImageView buddy = findViewById(R.id.buddy);

        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_Screen.this, LogIn.class);
                startActivity(intent);
            }
        };

        // Post the Runnable with a delay


        visible(day, 500);
        visible(buddy, 750);
        handler.postDelayed(runnable, 3500);






    }

    void visible(ImageView x, int y){

        Handler handler = new Handler();

        Runnable vis = new Runnable() {
            @Override
            public void run() {
                x.setVisibility(View.VISIBLE);
            }
        };

        Runnable invis = new Runnable() {
            @Override
            public void run() {
                x.setVisibility(View.INVISIBLE);
            }
        };

        Runnable all = new Runnable() {
            @Override
            public void run() {



                handler.postDelayed(vis, 100);
                handler.postDelayed(invis, 200);
                handler.postDelayed(vis, 300);
                handler.postDelayed(invis, 400);
                handler.postDelayed(vis, 500);
                handler.postDelayed(invis, 600);
                handler.postDelayed(vis, 700);
                handler.postDelayed(invis, 800);
                handler.postDelayed(vis, 900);
                handler.postDelayed(invis, 1000);
                handler.postDelayed(vis, 1100);


            }
        };

        handler.postDelayed(all, y);

    }

}
