package com.example.petshop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AddProfile extends AppCompatActivity {

    private EditText name, email, mobile, address, city;
    private Button btnProfile;
    ProgressDialog progressDialog;
    private ImageView profileImage;
    private String imagePath = "";
    private String postPath = "";
    Preferences preferences;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile);

        progressDialog = new ProgressDialog(this);

        name = (EditText) findViewById(R.id.edt_name);
        email = (EditText) findViewById(R.id.edt_email);
        mobile = (EditText) findViewById(R.id.edt_mobile);
        address = (EditText) findViewById(R.id.edt_address);
        city = (EditText) findViewById(R.id.edt_city);


        btnProfile = (Button) findViewById(R.id.btn_profile);

        circleImageView = (CircleImageView) findViewById(R.id.profile_image);


        preferences = new Preferences(AddProfile.this);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Flag = 0;
                if (TextUtils.isEmpty(name.getText().toString())) {
                    name.requestFocus();
                    name.setError("Please enter name");
                    name.requestFocus();
                    Flag = 1;
                }
                if (TextUtils.isEmpty(email.getText().toString())) {
                    email.requestFocus();
                    email.setError("Please enter email");
                    name.requestFocus();
                    Flag = 1;
                } else if (!isValidEmail(email.getText().toString())) {
                    email.requestFocus();
                    email.setError("Please enter valid email");
                    name.requestFocus();
                    Flag = 1;
                }
                if (TextUtils.isEmpty(mobile.getText().toString())) {
                    mobile.requestFocus();
                    mobile.setError("Please enter mobile");
                    name.requestFocus();
                    Flag = 1;
                } else if (mobile.getText().toString().trim().length() < 10) {
                    mobile.requestFocus();
                    mobile.setError("Please enter 10 digit mobile number");
                    name.requestFocus();
                    Flag = 1;
                }
                if (TextUtils.isEmpty(address.getText().toString())) {
                    address.requestFocus();
                    address.setError("Please enter address");
                    name.requestFocus();
                    Flag = 1;
                }
                if (TextUtils.isEmpty(city.getText().toString())) {
                    city.requestFocus();
                    city.setError("Please enter city");
                    name.requestFocus();
                    Flag = 1;
                }
                if (Flag == 0) {
                    if (isConnectingToInternet(getApplicationContext())) {
                        saveProfile();
                    } else {
                        Toast.makeText(AddProfile.this, "Internet Connection is not available", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setFixAspectRatio(true)
                        .setAllowFlipping(false)
                        .setAllowRotation(true)
                        .start(AddProfile.this);

            }
        });
    }

    public void saveProfile() {
        progressDialog.setMessage("Please wait....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        String Name = name.getText().toString();
        String Email = email.getText().toString();
        String Mobile = mobile.getText().toString();
        String Address = address.getText().toString();
        String City = city.getText().toString();



        Retrofit retrofit = null;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {


            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.weburl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        MultipartBody.Part body = null;
        MultipartBody.Part body1 = null;
        if (!postPath.equalsIgnoreCase("")) {
            File file = new File(postPath);

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("profilephoto", file.getName(), requestBody);
        }



        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), Name);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), Email);
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), Mobile);
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), Address);
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), City);
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), preferences.getUserID());


        ApiInterface apiInterface;
        apiInterface = retrofit.create(ApiInterface.class);
        Map<String, RequestBody> params = new HashMap<>();

        params.put("name", name);
        params.put("email", email);
        params.put("mobile", mobile);
        params.put("address", address);
        params.put("city", city);
        params.put("userid", userid);

        Call<ProfilePojo> call = apiInterface.profile(params, body);
        call.enqueue(new Callback<ProfilePojo>() {
            @Override
            public void onResponse(Call<ProfilePojo> call, Response<ProfilePojo> response) {

                progressDialog.dismiss();
                if (response.body().getSuccess() == 1) {

                    preferences.setUserName(response.body().getName());
                    preferences.setUserEmail(response.body().getEmail());
                    preferences.setUserMobile(response.body().getMobile());
                    preferences.setAddress(response.body().getAddress());
                    preferences.setCity(response.body().getCity());

                    preferences.setProfilePhoto(response.body().getProfilephoto());

                    Toast.makeText(AddProfile.this, "Profile save Sucessfully", Toast.LENGTH_SHORT).show();
                   /* Intent intent = new Intent(LecturerProfile.this, Home.class);
                    startActivity(intent);*/
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.frame_container,new Profile())
                            .commit();

                } else if (response.body().getSuccess() == 0) {

                    Toast.makeText(AddProfile.this, "Unsucessfull....Try again", Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onFailure(Call<ProfilePojo> call, Throwable t) {
                progressDialog.dismiss();

                System.out.println("*****s"+t.toString());
                Toast.makeText(AddProfile.this, t.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    imagePath = getRealPathFromURI(resultUri);
                    Glide.with(AddProfile.this)
                            .load(imagePath)
                            .into(circleImageView);

                    postPath = imagePath;

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, "sorry, there was an error", Toast.LENGTH_LONG).show();
        }

    }


    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    //email validations
    private boolean isValidEmail(String email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    public boolean isConnectingToInternet(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info[] = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++)

                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
