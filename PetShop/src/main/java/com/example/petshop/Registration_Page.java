package com.example.petshop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration_Page extends AppCompatActivity {
    EditText p_number, uname, pass, conf_pass,email;
    Button nxtbtn;
    String Phone_number, Name, Password, Confirm_pass,s_email;
    DatabaseReference myref;
    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userId;
    ProgressDialog pg;
    private static final String TAG = "EmailPassword";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_registration_page);

        uname = findViewById(R.id.Username);
        pass = findViewById(R.id.password);
        conf_pass = findViewById(R.id.conf_password);
        nxtbtn = findViewById(R.id.nextbtn);
        email = findViewById(R.id.Email);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myref = database.getReference("User Details");


        nxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
                //calls the signup method for the form validation`
            }
        });

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Registration_Page.this, Login_Page.class);
        startActivity(i);
    }
    public void signup () {
        if (!validateform()) {
            return;
        }

        Name = uname.getText().toString();
        SharedPreferences mySharedPreferences = this.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("USERNAME",Name);
        editor.apply();
        s_email = email.getText().toString();
        Password = pass.getText().toString();
        Confirm_pass = conf_pass.getText().toString();
        mAuth.createUserWithEmailAndPassword(s_email,Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){

                            fStore = FirebaseFirestore.getInstance();
                            userId = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userId);

                            Map<String,Object> user = new HashMap<>();
                            user.put("fullName", Name);
                            user.put("Email", s_email);
                            user.put("Password",Password);
                            Intent i =new Intent(Registration_Page.this,Login_Page.class);
                            startActivity(i);
                            finish();

                            documentReference.collection("users").document("Info").set(user);
                            FirebaseUser fUser = mAuth.getCurrentUser();
                            fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Registration_Page.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });
                            finish();
                        }
                        else
                        {
                            Log.w(TAG,"Failed to Sign-up",task.getException());
                            Toast.makeText(Registration_Page.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    private boolean validateform () {
        boolean valid = true;

        String us = uname.getText().toString();
        if (TextUtils.isEmpty(us)) {
            uname.setError("Field Empty");
            valid = false;
        } else {
            uname.setError(null);
        }
        String em = email.getText().toString();
        if(TextUtils.isEmpty(em))
        {
            email.setError("Field Empty");
            valid= false;
        }
        else  if(!Patterns.EMAIL_ADDRESS.matcher(em).matches())
        {
            email.setError("Invalid Email");
            valid = false;
        }
        else {
            email.setError(null);
        }
// onClick of button perform this simplest code.
        String pw = pass.getText().toString();
        if (TextUtils.isEmpty(pw)) {
            pass.setError("Field Empty");
            valid = false;
        } else {
            pass.setError(null);
        }
        String cpw = conf_pass.getText().toString();
        if (TextUtils.isEmpty(cpw)) {
            conf_pass.setError("Field Empty");
            valid = false;
        } else {
            conf_pass.setError(null);
        }
        if (!(cpw.equals(pw))) {
            conf_pass.setError("Password Not Match");
        } else {
            conf_pass.setError(null);
        }
        return valid;
    }
}