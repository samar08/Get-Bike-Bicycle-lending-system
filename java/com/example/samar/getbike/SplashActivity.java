package com.example.samar.getbike;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.parse.ParseUser;

public class SplashActivity extends AppCompatActivity {
ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo=findViewById(R.id.loaderlogo);
        logo.animate().rotationBy(720).setDuration(1500);
        Handler handler=new Handler();
       if( ParseUser.getCurrentUser()==null){


           handler.postDelayed(new Runnable() {
               @Override
               public void run() {
                   Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                   startActivity(intent);
                   finish();
               }
           },1500);
       }
       else{

           handler.postDelayed(new Runnable() {
               @Override
               public void run() {
                   Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                   startActivity(intent);
                   finish();
               }
           },1500);
       }



    }
}
