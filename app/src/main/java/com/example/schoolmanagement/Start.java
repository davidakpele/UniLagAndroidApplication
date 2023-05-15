package com.example.schoolmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.schoolmanagement.databinding.ActivityStartBinding;

public class Start extends AppCompatActivity {
    private ActivityStartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.StartNewRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Start.this, Register.class);
                startActivity(i);
            }
        });
        binding.StudentLoginPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Start.this, Login.class);
                startActivity(i);
            }
        });

        binding.MakePaymentOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Start.this, OnlinePayment.class);
                startActivity(i);
            }
        });


        binding.ReviewRequirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Start.this, RequirementView.class);
                startActivity(i);
            }
        });

    }
}