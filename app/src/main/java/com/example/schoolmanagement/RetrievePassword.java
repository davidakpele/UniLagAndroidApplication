package com.example.schoolmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.schoolmanagement.databinding.ActivityRetrievePasswordBinding;

public class RetrievePassword extends AppCompatActivity {
    private ActivityRetrievePasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRetrievePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Going Backing Arrow Action
        binding.itemTouchHelperPreviousElevation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RetrievePassword.this, Login.class));
                finish();
            }
        });
    }
}