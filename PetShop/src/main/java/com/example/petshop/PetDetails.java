package com.example.petshop;

import static android.app.PendingIntent.getActivity;
import static com.example.petshop.PetList.CALL;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PetDetails extends AppCompatActivity {
    TextView petname,petage,petcost,petype,petexpdate,petgender;
    ImageView petimage;
    GetPetPojo.Petlist pets;
    Button callowner,buypet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);
        petname = findViewById(R.id.PET_NAME);
        petage = findViewById(R.id.PET_AGE);
        petcost = findViewById(R.id.PET_COST);
        petgender = findViewById(R.id.PET_GENDER);
        petimage = findViewById(R.id.PET_IMAGE);
        callowner =findViewById(R.id.call_owner);

        Bundle extras;
        String name,age,gender,cost,type,expdate;
        if (savedInstanceState == null)

        {

            /*fetching extra data passed with intents in a Bundle type variable*/



            extras = getIntent().getExtras();

            if(extras == null)

            {

                name = null;
                age = null;
                gender = null;
                cost = null;
                type = null;
                expdate = null;

            }

            else

            {

                /* fetching the string passed with intent using ‘extras’*/
                String phno;
                name = extras.getString("PET_NAME");
                cost= extras.getString("PET_COST");
                gender= extras.getString("PET_GENDER");
                age= extras.getString("PET_AGE");
                expdate= extras.getString("PET_AVAILIBILTY");
                phno = extras.getString("PET_OWNER_NUMBER");
                Log.d("Phone", "onClick: "+phno);
                callowner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (checkPermission(Manifest.permission.CALL_PHONE)) {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" +phno));
                            startActivity(callIntent);

                        } else {
                            askForPermission(Manifest.permission.CALL_PHONE, CALL);
                        }
                    }
                });

                petname.setText(name);

                Log.d("EXP DATE", "onCreate: "+expdate);
                petcost.setText(cost+" RS");
                petgender.setText(gender);
                petage.setText(age);


            }

        }


    }
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{permission}, requestCode);
        } else {
            Toast.makeText(getApplicationContext(), "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission(String permission){
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            if (result == PackageManager.PERMISSION_GRANTED){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {

                //Call
                case 001:
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + "9975375723"));
                    startActivity(callIntent);

                    break;

            }

            Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }





}

