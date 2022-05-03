package com.example.petshop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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

public class Registration extends AppCompatActivity {

    private static final String REGISTER_URL = Constants.weburl+"registration.php";


    private EditText email, name, mobile, password, conformpassword;
    private Button btnsignup;
    private TextView txtlogin;
    private ProgressDialog progressDialog;
    String Email="", Name="", Mobile="", Password="", ConformPassword="";
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog = new ProgressDialog(this);
        preferences = new Preferences(this);

        email = (EditText) findViewById(R.id.edt_email);
        name = (EditText) findViewById(R.id.edt_name);
        mobile = (EditText) findViewById(R.id.edt_mobile);
        password = (EditText) findViewById(R.id.password);
        conformpassword = (EditText) findViewById(R.id.conf_password);

        btnsignup = (Button) findViewById(R.id.btn_register);
//        txtlogin = (TextView) findViewById(R.id.txt_login);
//
//        txtlogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i1 = new Intent(Registration.this, Login.class);
//                startActivity(i1);
//
//            }
//        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Flag = 0;
                if (TextUtils.isEmpty(email.getText().toString())) {
                    email.requestFocus();
                    email.setError("Please enter email");
                    Flag = 1;
                } else if (!isValidEmail(email.getText().toString())) {
                    email.requestFocus();
                    email.setError("Please enter valid email");
                    Flag = 1;
                }
                if (TextUtils.isEmpty(name.getText().toString())) {
                    name.requestFocus();
                    name.setError("Please enter password");
                    Flag = 1;
                }

                if (TextUtils.isEmpty(password.getText().toString())) {
                    password.requestFocus();
                    password.setError("Please enter password");
                    Flag = 1;
                }
                if (password.getText().toString().trim().length() < 8) {
                    password.requestFocus();
                    password.setError("password should be 8 length long");
                    Flag = 1;
                }
                if(!password.getText().toString().trim().matches("(.*[0-9].*)"))
                {
                    password.requestFocus();
                    password.setError("password should contain at least one digit");
                    Flag = 1;

                }
                if(!password.getText().toString().trim().matches("(.*[A-Z].*)"))
                {
                    password.requestFocus();
                    password.setError("password should contain at least one upper alphabet");
                    Flag = 1;

                }

                if(!password.getText().toString().trim().matches("(.*[a-z].*)"))
                {
                    password.requestFocus();
                    password.setError("password should contain at least one lower alphabet");
                    Flag = 1;

                }
                if(!password.getText().toString().trim().matches("^(?=.*[_.()$&@]).*$"))
                {
                    password.requestFocus();
                    password.setError("password should contains at least one special symbol ");
                    Flag = 1;

                }
                if (TextUtils.isEmpty(conformpassword.getText().toString())) {
                    conformpassword.requestFocus();
                    conformpassword.setError("Please enter conformpassword");
                    Flag = 1;
                }/*else if (conformpassword.getText().toString().trim().length() < 8) {
                    conformpassword.requestFocus();
                    conformpassword.setError("Please enter 8 conform password");
                    Flag = 1;
                }*/
                if (TextUtils.isEmpty(mobile.getText().toString())) {
                    mobile.requestFocus();
                    mobile.setError("Please enter mobile");
                    Flag = 1;
                } else if (mobile.getText().toString().trim().length() < 10) {
                    mobile.requestFocus();
                    mobile.setError("Please enter 10 digit mobile number");
                    Flag = 1;
                }

                if (Flag == 0) {
                    signup();
                }
            }
        });
    }

    public void signup(){
        progressDialog.setMessage("Please wait....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(100);
        progressDialog.show();

        Email = email.getText().toString().trim();
        Name = name.getText().toString().trim();
        Password = password.getText().toString().trim();
        ConformPassword = conformpassword.getText().toString().trim();
        Mobile = mobile.getText().toString().trim();

        if (isConnectingToInternet(getApplicationContext())) {
            if (Password.equals(ConformPassword)) {



                StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();

                                try {

                                    JSONObject jsonResponse = new JSONObject(response);
                                    int success = jsonResponse.getInt("success");
                                    progressDialog.dismiss();
                                    if (success == 0) {
                                        Toast.makeText(Registration.this, "Registration failed. Please try again.", Toast.LENGTH_LONG).show();
                                    } else if (success == 1) {
                                        Toast.makeText(Registration.this, "You have successfully registered with Pet shop ", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(Registration.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                        //   pref.setFirstTimeLoggedIn("true");

                                    } else if (success == 2) {
                                        Toast.makeText(Registration.this, "This user is already registered with Pet shop", Toast.LENGTH_LONG).show();
                                    }
                                }catch (JSONException e) {
                                    progressDialog.dismiss();
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("email", Email);
                        params.put("name", Name);
                        params.put("mobile",Mobile);
                        params.put("password", Password);
                        params.put("usertype",preferences.getUserType());

                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            }else {
                progressDialog.dismiss();
                Toast.makeText(Registration.this, "Passwords are not matching", Toast.LENGTH_LONG).show();
            }
        }else {
            progressDialog.dismiss();
            Toast.makeText(Registration.this, "Internet Connection is not available ", Toast.LENGTH_LONG).show();
        }

    }

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
