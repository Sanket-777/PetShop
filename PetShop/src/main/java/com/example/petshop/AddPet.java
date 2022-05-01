package com.example.petshop;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class AddPet extends Fragment {

    ProgressDialog progressDialog;
    Preferences preferences;
    EditText edtPetName, edtPedAge, edtPetCost;
    CircleImageView circleImageView;
    Button btnAddPet, btnDate;
    TextView txtDate;
    String postPath = "", imagePath = "";

    ArrayList<String> arrayListPetType;
    ArrayList<String> arrayListPetGender;

    Spinner spinnerPetType, spinnerPetGender;

    String selectedPetType, selectedPetGender;
    Calendar myCalendar;
    SimpleDateFormat dateFormatter;
    int mYear,mMonth,mDay;
    DatePickerDialog picker;






    public AddPet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_pet, container, false);

        preferences = new Preferences(getActivity());
        progressDialog = new ProgressDialog(getActivity());

        btnDate = view.findViewById(R.id.btnDate);
        txtDate = view.findViewById(R.id.txtDate);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();

            }
        });

        arrayListPetType = new ArrayList<>();
        arrayListPetGender = new ArrayList<>();

        arrayListPetType = new ArrayList<>();
        arrayListPetType.add("Dog");
        arrayListPetType.add("Cat");
        arrayListPetType.add("Parrot");
        arrayListPetType.add("LoveBirds");

        arrayListPetGender = new ArrayList<>();
        arrayListPetGender.add("Male");
        arrayListPetGender.add("Female");


        spinnerPetType = view.findViewById(R.id.spinnerPetType);
        spinnerPetGender = view.findViewById(R.id.spinnerPetGender);

        ArrayAdapter<String> adapterPetType = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                arrayListPetType);

        spinnerPetType.setAdapter(adapterPetType);

        spinnerPetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
                //We are here that means an item was selected
                //Now we retrieve the user selection
                //Get the selected item text
                selectedPetType = parent.getItemAtPosition(pos).toString();
                Toast toastSpinnerSelection = Toast.makeText(getActivity(), selectedPetType, Toast.LENGTH_SHORT);
                //display the toast notification on user interface
                //set the toast display location
                toastSpinnerSelection.setGravity(Gravity.CENTER|Gravity.BOTTOM,0,150);
                toastSpinnerSelection.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
                //Another interface callback
            }

        });

        ArrayAdapter<String> adapterPetGender = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                arrayListPetGender);

        spinnerPetGender.setAdapter(adapterPetGender);

        spinnerPetGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
                //We are here that means an item was selected
                //Now we retrieve the user selection
                //Get the selected item text
                selectedPetGender = parent.getItemAtPosition(pos).toString();
                Toast toastSpinnerSelection = Toast.makeText(getActivity(), selectedPetGender, Toast.LENGTH_SHORT);
                //display the toast notification on user interface
                //set the toast display location
                toastSpinnerSelection.setGravity(Gravity.CENTER|Gravity.BOTTOM,0,150);
                toastSpinnerSelection.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
                //Another interface callback
            }

        });

        edtPetName = view.findViewById(R.id.edtPetName);
        edtPedAge = view.findViewById(R.id.edtPetAge);
        edtPetCost = view.findViewById(R.id.edtPetCost);



        circleImageView = view.findViewById(R.id.pet_image);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity()
                        .setAspectRatio(16,9)
                        .getIntent(getContext());

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });


        btnAddPet = view.findViewById(R.id.btnAddPet);

        btnAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                savePet();

            }
        });


        return view;
    }

    public void savePet()
    {
        progressDialog.setMessage("Please wait....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);

        String expDate = txtDate.getText().toString().trim();


        String PetType = selectedPetType;
        String PetName = edtPetName.getText().toString();
        String PetAge = edtPedAge.getText().toString();
        String PetCost = edtPetCost.getText().toString();
        String PetGender = selectedPetGender;

        Retrofit retrofit = null;

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.weburl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        MultipartBody.Part body = null, body1 = null;
        if(!postPath.equalsIgnoreCase("")) {
            File file = new File(postPath);

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("petimage", file.getName(), requestBody);
        }


        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), PetType);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), PetName);
        RequestBody age = RequestBody.create(MediaType.parse("text/plain"), PetAge);
        RequestBody cost = RequestBody.create(MediaType.parse("text/plain"),PetCost);
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"),PetGender);
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), preferences.getUserID());
        RequestBody usermobile = RequestBody.create(MediaType.parse("text/plain"),preferences.getUserMobile());
        RequestBody datee = RequestBody.create(MediaType.parse("text/plain"),thisDate);
        RequestBody expdatee = RequestBody.create(MediaType.parse("text/plain"),expDate);

        ApiInterface apiInterface;
        apiInterface = retrofit.create(ApiInterface.class);
        Map<String, RequestBody> params = new HashMap<>();

        params.put("userid",userid);
        params.put("pettype",type);
        params.put("petname",name);
        params.put("petage",age);
        params.put("petcost",cost);
        params.put("petgender",gender);
        params.put("usermobile",usermobile);
        params.put("datee",datee);
        params.put("expdate",expdatee);



        Call<AddPetPojo> call=apiInterface.addPet(params,body);
        call.enqueue(new Callback<AddPetPojo>() {
            @Override
            public void onResponse(Call<AddPetPojo> call, retrofit2.Response<AddPetPojo> response) {

                progressDialog.dismiss();
                if (response.body().getSuccess() == 1) {

                    Toast.makeText(getActivity(), "Pet added Sucessfully", Toast.LENGTH_SHORT).show();
                   /* Intent intent = new Intent(AddCourse.this,Course.class);
                    startActivity(intent);
                    finish();*/

                } else if (response.body().getSuccess() == 0) {

                    Toast.makeText(getActivity(), "Unsucessfull....Try again", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<AddPetPojo> call, Throwable t) {
                progressDialog.dismiss();

                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imagePath = getRealPathFromURI(resultUri);
                Glide.with(AddPet.this)
                        .load(imagePath)
                        .into(circleImageView);

                postPath = imagePath;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

            }
        }
    }



    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
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
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }


}