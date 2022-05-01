package com.example.petshop;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyPet extends Fragment {

    RecyclerView recPet;
    PetAdapter petAdapter;

    Preferences preferences;
    ProgressDialog progressDialog;
    ArrayList<GetPetPojo.Petlist> petlists;

    public final static String DELETE_URL = Constants.weburl+"/deletepet.php";

    public final static String UPDATE_URL = Constants.weburl+"/updatePet.php";



    public MyPet() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_pet, container, false);

        recPet = view.findViewById(R.id.recMyPet);

        preferences = new Preferences(getActivity());
        progressDialog = new ProgressDialog(getActivity());



        petlists = new ArrayList<GetPetPojo.Petlist>();


        recPet.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        recPet.setAdapter(petAdapter);

        getMyPet();

        return view;
    }

    public void getMyPet(){

        progressDialog.setMessage("Please wait....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = null;

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.weburl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        ApiInterface apiInterface;
        apiInterface = retrofit.create(ApiInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("userid", preferences.getUserID());


        Call<GetPetPojo> call=apiInterface.getMyPet(params);
        call.enqueue(new Callback<GetPetPojo>() {
            @Override
            public void onResponse(Call<GetPetPojo> call, retrofit2.Response<GetPetPojo> response) {

                progressDialog.dismiss();


                if (response.body().getSuccess() == 1) {

                    if (petlists.size() > 0) {
                        petlists.clear();
                        recPet.removeAllViewsInLayout();
                    }

                    petlists = new ArrayList<>();


                    petlists.addAll(response.body().getPetlist());

                    petAdapter = new PetAdapter(getActivity(),petlists);

                    // Set the adapter for RecyclerView
                    recPet.setAdapter(petAdapter);

                    //   clear();
                    petAdapter.notifyDataSetChanged();




                } else if (response.body().getSuccess() == 0) {

                    petlists.clear();
                    recPet.removeAllViewsInLayout();

                    Toast.makeText(getActivity(), "No pet list available", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<GetPetPojo> call, Throwable t) {

                progressDialog.dismiss();

                Log.e(PetList.class.getSimpleName(), t.toString());
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();

            }

        });

    }

    public class PetAdapter extends RecyclerView.Adapter<PetAdapterHolder>  {


        private ArrayList<GetPetPojo.Petlist> petlists;
        private Context context;
        ProgressDialog progressDialog;


        public PetAdapter(Context context, ArrayList<GetPetPojo.Petlist> petlists) {
            this.petlists = petlists;
            this.context = context;
        }





        @NonNull
        @Override
        public PetAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_pet,null );

            PetAdapterHolder petAdapterHolder = new PetAdapterHolder(view);

            progressDialog = new ProgressDialog(context);

            return petAdapterHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PetAdapterHolder holder,
                                     @SuppressLint("RecyclerView") final int position) {

            Glide.with(context).load(Constants.weburl + petlists.get(position)
                    .getPetimage())
                    .into(holder.imagCourse);


            holder.txtPetType.setText(petlists.get(position).getPettype());
            holder.txtPetName.setText(petlists.get(position).getPetname());
            holder.txtCost.setText(petlists.get(position).getPetcost());
            holder.txtUserMobile.setText(petlists.get(position).getUsermobile());

            holder.imgUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
//                   Intent i=new Intent(MyPet.this,UpdateActivity.class);

                }
            });



            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                    // Setting Dialog Title
                    alertDialog.setTitle("Confirm Delete...");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want delete this?");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.ic_action_delete);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

                            // Write your code here to invoke YES event
                            deletePet(position);
                            //   Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            //  Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                }
            });


        }

        @Override
        public int getItemCount() {
            return petlists.size();
        }
    }






























    private class PetAdapterHolder extends RecyclerView.ViewHolder {

        TextView txtPetType,txtPetName,txtCost,txtUserMobile;
        ImageView imagCourse, imgDelete,imgUpdate;
        CardView cardView;

        public PetAdapterHolder(View itemView) {
            super(itemView);

            txtPetType = itemView.findViewById(R.id.txtPetType);
            txtPetName = itemView.findViewById(R.id.txtName);
            txtCost = itemView.findViewById(R.id.txtCost);
            txtUserMobile = itemView.findViewById(R.id.txtUserMobile);
            cardView = itemView.findViewById(R.id.cardview);
            imagCourse = itemView.findViewById(R.id.pet_image);
            imgDelete = itemView.findViewById(R.id.imageDelete);
            imgUpdate= itemView.findViewById(R.id.imageUpdate);


        }
    }

    public void updatePet(final int position)
    {
        progressDialog.setMessage("Please wait....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(100);
        progressDialog.show();


    }


    public void deletePet(final int position)
    {
        progressDialog.setMessage("Please wait....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(100);
        progressDialog.show();


        if (isConnectingToInternet(getContext())) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_URL,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                int success = jsonResponse.getInt("success");
                                progressDialog.dismiss();
                                if (success == 1) {
                                    petlists.remove(position);
                                    petAdapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(),"Item Deleted Sucessfully", Toast.LENGTH_LONG).show();

                                } else if (success == 0) {
                                    Toast.makeText(getActivity(),"Item Not Deleted", Toast.LENGTH_LONG).show();

                                }
                            }catch (JSONException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("petid", petlists.get(position).getId());


                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        }else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Internet Connection is not available ", Toast.LENGTH_LONG).show();
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