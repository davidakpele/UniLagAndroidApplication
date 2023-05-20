package com.example.schoolmanagement;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.schoolmanagement.databinding.ActivityLoginBinding;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Onclick on login button action
        binding.LoginButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(Login.this).setTitleText("Here's a message!").show();
                new SweetAlertDialog(Login.this).setTitleText("Here's a message!").setContentText("It's pretty, isn't it?").show();
                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Are you sure?").setContentText("Won't be able to recover this file!").setConfirmText("Yes,delete it!").show();
                new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Good job!").setContentText("You clicked the button!").show();
                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Are you sure?").setContentText("Won't be able to recover this file!").setConfirmText("Yes,delete it!").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {@Override
                public void onClick(SweetAlertDialog sDialog) {
                    // Showing simple toast message to user
                    Toast.makeText(Login.this, " You Clicked me ", Toast.LENGTH_SHORT).show();

                    sDialog.dismissWithAnimation();
                }
                }).show();
                //Toast.makeText(getApplicationContext(), "Login Button Click", Toast.LENGTH_SHORT).show();
                // Create the object of AlertDialog Builder class
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                // Set the message show for the Alert time
                builder.setMessage("Do you want to exit ?");
                // Set Alert Title
                builder.setTitle("Alert !");

                // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                builder.setCancelable(false);

                // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // When the user click yes button then app will close
                    finish();
                });
                // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });

                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();
                // Show the Alert Dialog box
                alertDialog.show();

            }
        });

        //onclick on forget password button
        binding.forgotOptionLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, RetrievePassword.class);
                startActivity(i);
            }
        });

        //onclick on create button inside login activity
        binding.CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });
    }
}