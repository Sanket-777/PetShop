package com.example.petshop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;


public class SplashScreen extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Preferences preferences = new Preferences(SplashScreen.this);

                if (preferences.get_is_loggedin().equals("1")){
                    Intent intent = new Intent(SplashScreen.this, Home.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                }
                finish();
//                Intent intent=new Intent(SplashScreen.this,MainActivity.class);
//                startActivity(intent);
//                finish();

            }
        },SPLASH_TIME_OUT);
        try{
            getSupportActionBar().hide();
        }
        catch (Exception E)
        {
            Log.d("Exception",E.toString());
        }

    }

}
