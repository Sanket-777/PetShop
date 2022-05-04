package com.example.petshop;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment {

    private Button btnaddprofile,btnLogout, btnPetCare;
    private TextView  name, mobile, email;
    private CircleImageView circleImageView;
    Preferences preferences;


    public Profile() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        preferences = new Preferences(getContext());

       // btnPetCare =  view.findViewById(R.id.btnPetCare);
        btnaddprofile = (Button) view.findViewById(R.id.btn_addprofile);
        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        name = (TextView) view.findViewById(R.id.txt_name);
        mobile = (TextView) view.findViewById(R.id.txt_mobile);
        email = (TextView) view.findViewById(R.id.txt_email);
        circleImageView = (CircleImageView) view.findViewById(R.id.profile_image);

        name.setText(preferences.getUserName());
        mobile.setText(preferences.getUserMobile());
        email.setText(preferences.getUserEmail());

        Glide.with(getActivity()).load(Constants.weburl + preferences.getProfilePhoto())
                .into(circleImageView);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Login.class);
                preferences.clearPreferences();
                startActivity(intent);
                getActivity().finish();
//                    getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            }
        });

        btnaddprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddProfile.class);
                startActivity(intent);


            }
        });



        return view;
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