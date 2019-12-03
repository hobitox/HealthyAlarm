package com.example.hellalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    private static int timeout=3000;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        img=findViewById(R.id.img);

        Animation animation= AnimationUtils.loadAnimation(HomeActivity.this,R.anim.myanim);
        img.startAnimation(animation);



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(HomeActivity.this ,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },timeout);
    }
}
