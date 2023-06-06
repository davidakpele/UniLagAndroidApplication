package com.example.schoolmanagement;
import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import at.favre.lib.crypto.bcrypt.BCrypt;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    private TextInputLayout useremail, psw;
    private FloatingActionButton floatingActionButton;
    private AppCompatButton LoginButtonSubmit;
    //    Firebase auth
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase ;
    private DatabaseReference reference;
    private TextView forgotOptionLink, CreateAccountButton;
    //    Progress dialog
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Assigning matric Number
        useremail =  findViewById(R.id.UserEmail);
        //Assigning Password
        psw = findViewById(R.id.password);
        //Create Account Assigning
        CreateAccountButton = findViewById(R.id.CreateAccountButton);
        //Forgot Password Link assigning
        forgotOptionLink = findViewById(R.id.forgotOptionLink);
        //Previous Button Assigning
        floatingActionButton = findViewById(R.id.floatingActionButton);
        //Login assigning
        LoginButtonSubmit = findViewById(R.id.LoginButtonSubmit);
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            // user not logged in yet
            Log.d(TAG,  "Already Login");
        }
        progressDialog.setCanceledOnTouchOutside(false);
        //Onclick on login button action
        LoginButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateUserInput();
            }
        });
        //onclick on forget password button
        forgotOptionLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, RetrievePassword.class);
                startActivity(i);
            }
        });
        //onclick on create button inside login activity
       CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
    }

    private void ValidateUserInput() {
        //let do validations now
        if (TextUtils.isEmpty(useremail.getEditText().getText().toString().trim())){
            useremail.setError("Required*");
            useremail.requestFocus();
            useremail.requestFocus();
            return;
        } else if  (!Patterns.EMAIL_ADDRESS.matcher(useremail.getEditText().getText().toString()).matches()) {
            useremail.setError("Invalid Email Address.!");
            useremail.requestFocus();
            useremail.requestFocus();
            return;
        } else {
            useremail.setError(null);
        }
        if (TextUtils.isEmpty(psw.getEditText().getText().toString().trim())) {
            psw.setError("Enter Your Password");
            psw.requestFocus();
            psw.requestFocus();
            return;
        }else {
            //authenticate user with email/password by adding complete listener
            LoginUser();
        }
    }

    private void LoginUser() {
        //show progressDialog
        progressDialog.setMessage("Logging In..");
        progressDialog.show();
        final String email = useremail.getEditText().getText().toString().trim();
        final String password = psw.getEditText().getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("email").equalTo(email);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot !=null){
//                    String fees=dataSnapshot.child("surname").getValue().toString();
//                    Log.d("message", fees);
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        String DatabaseUserPassword=data.child("password").getValue().toString();
                        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), DatabaseUserPassword);
                        if (result.verified){
                            progressDialog.dismiss();
                            new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Login").setContentText("User Account Verified!").show();
                        }else{
                            progressDialog.dismiss();
                            psw.setError("Wrong Password.!");
                            psw.requestFocus();
                            psw.requestFocus();
                            return;
                        }
                    }
                }else {
                    progressDialog.dismiss();
                    useremail.setError("Email Address Not Found.!");
                    useremail.requestFocus();
                    useremail.requestFocus();
                    psw.setError("Wrong Password.!");
                    psw.requestFocus();
                    psw.requestFocus();
                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Error").setContentText("Wrong Credentials.!").show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("error", error.toString());
            }
        });
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
    }
    }
