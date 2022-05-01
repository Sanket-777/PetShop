package com.example.petshop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PetList extends Fragment {

    RecyclerView recPet;
    PetAdapter petAdapter;

    Preferences preferences;
    ProgressDialog progressDialog;
    ArrayList<GetPetPojo.Petlist> petlists;

    static final Integer CALL = 001;

    public PetList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        container.removeAllViews();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet_list, container, false);
        setHasOptionsMenu(true);


        recPet = view.findViewById(R.id.recPetList);

        preferences = new Preferences(getActivity());
        progressDialog = new ProgressDialog(getActivity());



        petlists = new ArrayList<GetPetPojo.Petlist>();


        recPet.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        recPet.setAdapter(petAdapter);

        getPet();

        return view;
    }

    public void getPet(){

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


        Call<GetPetPojo> call=apiInterface.getPet(params);
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

                    petAdapter = new PetAdapter(getActivity(),petlists, petAdapter.recyclerViewInterface);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

       /* getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;*/

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                if (petAdapter != null) petAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    public class PetAdapter extends RecyclerView.Adapter<PetAdapterHolder> implements Filterable {

        private  final RecyclerViewInterface recyclerViewInterface;
        private ArrayList<GetPetPojo.Petlist> petlists;
        private ArrayList<GetPetPojo.Petlist> mFilteredList;
        private ArrayList<GetPetPojo.Petlist> mList;
        private Context context;
        ProgressDialog progressDialog;


        public PetAdapter(Context context, ArrayList<GetPetPojo.Petlist> petlists,RecyclerViewInterface recyclerViewInterface) {
            this.petlists = petlists;
            this.mFilteredList = petlists;
            this.mList = petlists;
            this.context = context;
            this.recyclerViewInterface = recyclerViewInterface;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                String charString = "";

                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults filterResults = new FilterResults();
                    charString = charSequence.toString();

                    if (charString.isEmpty()) {
                        filterResults.values = 0;
                        mFilteredList = petlists;

                    } else {

                        ArrayList<GetPetPojo.Petlist> filteredList = new ArrayList<>();

                        for (GetPetPojo.Petlist androidVersion : petlists) {

                            if (androidVersion.getPetType().toLowerCase().contains(charString) || androidVersion.getPetcost().toLowerCase().contains(charString) || androidVersion.getPetage().toLowerCase().contains(charString)) {

                                filteredList.add(androidVersion);
                            }
                        }

                        mFilteredList = filteredList;
                    }


                    filterResults.values = mFilteredList;

                    return filterResults;
                }


                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    //  mFilteredList = (ArrayList<Dataprovider_customer>) filterResults.values;
                    //  mFilteredList = albumList;

                    if (charString.isEmpty()) {
                        petlists = mList;
                        notifyDataSetChanged();
                    } else {
                        int bal = petlists.size();
                        petlists = (ArrayList<GetPetPojo.Petlist>) filterResults.values;
                        notifyDataSetChanged();

//                Log.v(TAG+" getFilter", "publishResults called");
//                mainCatDAOList = (ArrayList<MainCategoryDAO>) results.values;
//                Log.v(TAG+" getFilter", "publishResults arrayList size "+arrayList.size());
                        for (int i = 0; i < petlists.size(); i++) {
                            petlists.get(i).getPetname();
                        }//for
                        notifyDataSetChanged();
                    }
                }
            };
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
        public void onBindViewHolder(@NonNull PetAdapterHolder holder, @SuppressLint("RecyclerView") final int  position) {

            Glide.with(context).load(Constants.weburl + petlists.get(position)
                    .getPetimage())
                    .into(holder.imagCourse);


            holder.txtPetType.setText(petlists.get(position).getPettype());
            holder.txtPetName.setText(petlists.get(position).getPetname());
            holder.txtCost.setText(petlists.get(position).getPetcost());
            holder.txtUserMobile.setText(petlists.get(position).getUsermobile());
            holder.imagUpdate.setVisibility(View.GONE);
            holder.imageDelete.setVisibility(View.GONE);
            holder.txtUserMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (checkPermission(Manifest.permission.CALL_PHONE)) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + petlists.get(position).getUsermobile()));
                        startActivity(callIntent);

                    } else {
                        askForPermission(Manifest.permission.CALL_PHONE, CALL);
                    }

                }
            });

           /* holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), StudentCourseDetails.class);
                    intent.putExtra("courseid", courselists.get(position).getCourseid());
                    intent.putExtra("fees", courselists.get(position).getFees());
                    intent.putExtra("by", courselists.get(position).getName());
                    intent.putExtra("coursename", courselists.get(position).getCoursename());

                    startActivity(intent);
                }
            });*/


        }

        @Override
        public int getItemCount() {
            return petlists.size();
        }

    }

    public class PetAdapterHolder extends RecyclerView.ViewHolder {

        TextView txtPetType,txtPetName,txtCost,txtUserMobile;
        ImageView imagCourse,imagUpdate,imageCart,imageDelete;
        CardView cardView;

        public PetAdapterHolder(View itemView) {
            super(itemView);

            txtPetType = itemView.findViewById(R.id.txtPetType);
            txtPetName = itemView.findViewById(R.id.txtName);
            txtCost = itemView.findViewById(R.id.txtCost);
            txtUserMobile = itemView.findViewById(R.id.txtUserMobile);
            cardView = itemView.findViewById(R.id.cardview);
            imagCourse = itemView.findViewById(R.id.pet_image);
            imageCart=itemView.findViewById(R.id.imageCart);
            imageDelete=itemView.findViewById(R.id.imageDelete);


            imagUpdate=itemView.findViewById(R.id.imageUpdate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                //    if(recyclerViewInterface != null)
                }
            });


        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(getActivity(), "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission(String permission){
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(getActivity(), permission);
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
        if(ActivityCompat.checkSelfPermission(getActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {

                //Call
                case 001:
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + "9975375723"));
                    startActivity(callIntent);

                    break;

            }

            Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

}
