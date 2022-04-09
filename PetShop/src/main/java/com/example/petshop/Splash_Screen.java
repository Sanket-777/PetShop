package com.example.petshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash_Screen extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser fuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mAuth = FirebaseAuth.getInstance();
                fuser = mAuth.getCurrentUser();
                if(mAuth.getCurrentUser()==null)
                {
                    startActivity(new Intent(Splash_Screen.this,Login_Page.class) );
                    finish();
                }
                else
                {
                    if(mAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(Splash_Screen.this,MainActivity.class) );
                        finish();
                    }
                    else if(!(mAuth.getCurrentUser().isEmailVerified()))
                    {
                        startActivity(new Intent(Splash_Screen.this,Login_Page.class) );
                        finish();
                    }
                }
            }
        }, 3000);
    }
}