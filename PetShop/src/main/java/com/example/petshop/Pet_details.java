package com.example.petshop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Pet_details extends AppCompatActivity implements  RecyclerViewInterface {
    private TextView petname;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.id.pet_details);

        petname = findViewById(R.id.pet_name);
    }

    @Override
    public void onItemClick(int position) {

    }
}
