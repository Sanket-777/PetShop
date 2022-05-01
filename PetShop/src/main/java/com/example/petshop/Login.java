package com.example.petshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private static final String LOGIN_URL = Constants.weburl+"login.php";

    private EditText mobile, password;
    private Button btnlogin;
    private TextView txtsignup;
    private CheckBox showpassword;
    ProgressDialog progressDialog;
    String Mobile="", Password="";
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Login");

        progressDialog = new ProgressDialog(Login.this);
        preferences = new Preferences(Login.this);

        showpassword = findViewById(R.id.show_password);
        mobile = (EditText) findViewById(R.id.edt_mobile);
        password = (EditText) findViewById(R.id.edt_password);
        btnlogin = (Button) findViewById(R.id.btn_login);
        txtsignup = (TextView) findViewById(R.id.txtsignup);

        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(Login.this, Registration.class);
                startActivity(i3);

            }
        });
        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (showpassword.isChecked())
                {
                    password.setTransformationMethod(null);
                }
                else
                {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(LoginCustomer.this, OrderType.class);
                startActivity(intent);*/

                int Flag = 0;
                if (TextUtils.isEmpty(mobile.getText().toString())) {
                    mobile.requestFocus();
                    mobile.setError("Please enter mobile");
                    Flag = 1;
                } else if (mobile.getText().toString().trim().length() < 10) {
                    mobile.requestFocus();
                    mobile.setError("Please enter 10 digit mobile number");
                    Flag = 1;
                }
                if (TextUtils.isEmpty(password.getText().toString())) {
                    password.requestFocus();
                    password.setError("Please enter password");
                    Flag = 1;
                }/*else if (password.getText().toString().trim().length() < 8) {
                    password.requestFocus();
                    password.setError("Please enter 8 digit password");
                    Flag = 1;
                }*/

                if (Flag == 0)
                {
                    login();
                }

            }
        });

    }

    public void login(){
        progressDialog.setMessage("Please wait....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(100);
        progressDialog.show();

        Mobile = mobile.getText().toString().trim();
        Password = password.getText().toString().trim();

        if (isConnectingToInternet(getApplicationContext())) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            try {

                                JSONObject jsonResponse = new JSONObject(response);
                                int success = jsonResponse.getInt("success");
                                progressDialog.dismiss();
                                if (success == 0) {
                                    Toast.makeText(Login.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
                                } else if (success == 1) {

                                    String UserId=jsonResponse.getString("id");
                                    String Name=jsonResponse.getString("name");
                                    String Mobile=jsonResponse.getString("mobile");
                                    String Email=jsonResponse.getString("email");
                                    String Address=jsonResponse.getString("address");
                                    String City=jsonResponse.getString("city");
                                    String ProfilePhoto=jsonResponse.getString("profilephoto");
                                    String UserType = jsonResponse.getString("usertype");

                                    preferences.setUserID(UserId);
                                    preferences.setUserName(Name);
                                    preferences.setUserMobile(Mobile);
                                    preferences.setUserType(UserType);
                                    preferences.setUserEmail(Email);
                                    preferences.setAddress(Address);
                                    preferences.setCity(City);
                                    preferences.setProfilePhoto(ProfilePhoto);
                                    preferences.set_is_loggedin("1");


                                    Toast.makeText(Login.this, "You have successfully Login with Pet Shop", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(Login.this, Home.class);
                                    startActivity(intent);
                                    finish();


                                }
                            }catch (JSONException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("mobile",Mobile);
                    params.put("password", Password);


                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        }else {
            progressDialog.dismiss();
            Toast.makeText(Login.this, "Internet Connection is not available ", Toast.LENGTH_LONG).show();
        }

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
        //  overridePendingTransition(R.xml.animation, R.xml.animation2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return true;
    }
}