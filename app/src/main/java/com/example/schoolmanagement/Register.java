package com.example.schoolmanagement;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.schoolmanagement.databinding.ActivityRegisterBinding;
import com.fredporciuncula.phonemoji.PhonemojiTextInputLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import at.favre.lib.crypto.bcrypt.BCrypt;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Register extends AppCompatActivity {
    //Firebase auth
    private FirebaseAuth firebaseAuth;
    //Progress dialog
    private ProgressDialog progressDialog;
    private String Application= "", Faculty="", Department="", Program = "", NiN="", StudentSurname ="", StudentOthername ="", StudentDateofBirth = "", StuntGender= "", StudentEmail ="", StudentRelationStatus, StudentPhoneNumber;
    private String selectedFaculty, selectedDepartment, SelectedApplication, SelectedProgram, SelectGender, SelectRelationshipStatus;                 //vars to hold the values of selected State and District
    //declaring TextView to show the errors
    private Spinner  App_type, FacultySpinner, DepartmentSpinner, ProgramSpinner, GenderSpinner, RelationshipStatusSpinner;                //Spinners
    // on below line creating a variable.
    private DatePickerDialog datePickerDialog;
    private TextInputEditText etJoiningDate;
    // Set fake TextView to be in error so that the error message appears
    private TextView tvInvisibleError;
    private TextInputLayout National_Identification_Number, Surname, OtherName, Email, RelationShip_Status;
    private PhonemojiTextInputLayout Mobile;
    private AppCompatButton PayButtonSubmit;
    private EditText mEmail, mSubject, mMessage;
    private ArrayAdapter<CharSequence> AppAdapter, facultyAdapter, DepartmentAdapter, ProgramAdapter, GenderAdapter, RelationshipStatusAdapter;   //declare adapters for the spinners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        //Button hooks
        PayButtonSubmit=(AppCompatButton)findViewById(R.id.PayButtonSubmit);
        etJoiningDate=(TextInputEditText)findViewById(R.id.idEdtDate);
        tvInvisibleError = (TextView)findViewById(R.id.tvInvisibleError);
        etJoiningDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        datePickerDialog = new DatePickerDialog(Register.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        etJoiningDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                }, year, month, day);
                        datePickerDialog.show();
                        break;
                }
                return false;
            }
        });
        //State Spinner Initialisation
        App_type = findViewById(R.id.application_type);
        AppAdapter = ArrayAdapter.createFromResource(this, R.array.School_Application, R.layout.spinner_layout);
        AppAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        App_type.setAdapter(AppAdapter);

        FacultySpinner= findViewById(R.id.Faculty);
        facultyAdapter = ArrayAdapter.createFromResource(this, R.array.array_default_faculty, R.layout.spinner_layout);
        facultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FacultySpinner.setAdapter(facultyAdapter);

        DepartmentSpinner = findViewById(R.id.spinner_department);
        DepartmentAdapter = ArrayAdapter.createFromResource(this, R.array.array_default_faculty, R.layout.spinner_layout);
        DepartmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DepartmentSpinner.setAdapter(DepartmentAdapter);

        ProgramSpinner = findViewById(R.id.spinner_program);
        ProgramAdapter = ArrayAdapter.createFromResource(this, R.array.array_default_faculty, R.layout.spinner_layout);
        ProgramAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ProgramSpinner.setAdapter(ProgramAdapter);

        GenderSpinner = findViewById(R.id.spinner_gender);
        GenderAdapter = ArrayAdapter.createFromResource(this, R.array.Gender_spinner, R.layout.spinner_layout);
        GenderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GenderSpinner.setAdapter(GenderAdapter);

        RelationshipStatusSpinner = findViewById(R.id.spinner_relationship);
        RelationshipStatusAdapter = ArrayAdapter.createFromResource(this, R.array.Relation_status_spinner, R.layout.spinner_layout);
        RelationshipStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RelationshipStatusSpinner.setAdapter(RelationshipStatusAdapter);
        App_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FacultySpinner = findViewById(R.id.Faculty);
                SelectedApplication =App_type.getSelectedItem().toString();
                int parentID = parent.getId();
                if (parentID == R.id.application_type){
                    switch (SelectedApplication){
                        case "--Select--": facultyAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.array_default_faculty, R.layout.spinner_layout);
                            DepartmentAdapter =ArrayAdapter.createFromResource(parent.getContext(), R.array.array_default_faculty, R.layout.spinner_layout);
                            ProgramAdapter =ArrayAdapter.createFromResource(parent.getContext(), R.array.array_default_faculty, R.layout.spinner_layout);
                            break;
                        case "Distance Learning Institute": facultyAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.DCL_Faculty_Array_List, R.layout.spinner_layout);
                            DepartmentAdapter =ArrayAdapter.createFromResource(parent.getContext(), R.array.select_any_faculty_first, R.layout.spinner_layout);
                            ProgramAdapter =ArrayAdapter.createFromResource(parent.getContext(), R.array.program_type_a, R.layout.spinner_layout);
                            break;
                        case "Postgraduate": facultyAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.POST_Faculty_Array_List, R.layout.spinner_layout);
                            DepartmentAdapter =ArrayAdapter.createFromResource(parent.getContext(), R.array.select_any_faculty_first, R.layout.spinner_layout);
                            ProgramAdapter =ArrayAdapter.createFromResource(parent.getContext(), R.array.program_type_b, R.layout.spinner_layout);
                            break;
                        case "Undergraduate": facultyAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.UNDER_Faculty_Array_List, R.layout.spinner_layout);
                            DepartmentAdapter =ArrayAdapter.createFromResource(parent.getContext(), R.array.select_any_faculty_first, R.layout.spinner_layout);
                            ProgramAdapter =ArrayAdapter.createFromResource(parent.getContext(), R.array.program_type_a, R.layout.spinner_layout);
                            break;
                        default:  break;
                    }
                    facultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     // Specify the layout to use when the list of choices appears
                    FacultySpinner.setAdapter(facultyAdapter);

                    DepartmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     // Specify the layout to use when the list of choices appears
                    DepartmentSpinner.setAdapter(DepartmentAdapter);

                    ProgramAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     // Specify the layout to use when the list of choices appears
                    ProgramSpinner.setAdapter(ProgramAdapter);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //Action ::When faculty is selected
        FacultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DepartmentSpinner = findViewById(R.id.spinner_department);
                selectedFaculty = FacultySpinner.getSelectedItem().toString();
                int parentID = parent.getId();
                if (parentID == R.id.Faculty){
                    if (selectedFaculty == "Faculty of Basic Medical Science (BMS)"){
                        Toast.makeText(Register.this, " You Clicked me ", Toast.LENGTH_SHORT).show();
                    }
                    switch (selectedFaculty){
                        case "--Select--": DepartmentAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.array_default_faculty, R.layout.spinner_layout);
                            break;
                        case "Faculty of Basic Medical Science (BMS)": DepartmentAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.Department_Under_Basic_Medical_Science_Under_DLI, R.layout.spinner_layout);
                            break;
                        case "Faculty of Public Health": DepartmentAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.Department_Under_Public_Health_Under_DLI, R.layout.spinner_layout);
                            break;
                        case "Faculty of Social Science": DepartmentAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.Department_Under_Social_Science_Under_DLI, R.layout.spinner_layout);
                            break;
                        case "Faculty of Engineering/Env": DepartmentAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.Department_Under_Engineering_env_Under_DLI, R.layout.spinner_layout);
                            break;
                        case "Faculty of Education": DepartmentAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.Department_Under_Education_Under_DLI, R.layout.spinner_layout);
                            break;
                        case "Faculty of Science": DepartmentAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.Department_Under_Science_Under_DLI, R.layout.spinner_layout);
                            break;
                        case "Faculty of Art": DepartmentAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.Department_Under_Art_Under_DLI, R.layout.spinner_layout);
                            break;
                        case "Faculty of Technology": DepartmentAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.Department_Under_Technology_Under_DLI, R.layout.spinner_layout);
                            break;
                        case "Faculty of Administrator": DepartmentAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.Department_Under_Admin_Under_DLI, R.layout.spinner_layout);
                            break;
                        case "Faculty of Veterinary Medicine": DepartmentAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.Department_Under_vite_Under_DLI, R.layout.spinner_layout);
                            break;
                        case "Faculty of Pharmacy": DepartmentAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.Department_Under_Pharmacy_Under_DLI, R.layout.spinner_layout);
                            break;
                        case "Faculty of Agriculture": DepartmentAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.Department_Under_Agric_Under_DLI, R.layout.spinner_layout);
                            break;
                        case "Faculty of Law": DepartmentAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.Department_Under_Law_Under_DLI, R.layout.spinner_layout);
                            break;
                        default:  break;
                    }
                    DepartmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     // Specify the layout to use when the list of choices appears
                    DepartmentSpinner.setAdapter(DepartmentAdapter);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        PayButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App_type !=null && App_type.getSelectedItem() !=null){
                    View Aview = App_type.getSelectedView();
                    TextView AppListItem = (TextView)Aview;
                    View Fview = FacultySpinner.getSelectedView();
                    TextView FacListItem = (TextView)Fview;
                    View Dview = DepartmentSpinner.getSelectedView();
                    TextView DepListItem = (TextView)Dview;
                    View Pview = ProgramSpinner.getSelectedView();
                    TextView ProListItem = (TextView)Pview;
                    Surname = findViewById(R.id.Surname);
                    OtherName = findViewById(R.id.OtherName);
                    National_Identification_Number = findViewById(R.id.nin);
                    Email = findViewById(R.id.UserEmail);
                    View view = App_type.getSelectedView();
                    TextView tvListItem = (TextView)view;
                    View GenderView = GenderSpinner.getSelectedView();
                    TextView GenderListItem = (TextView)GenderView;
                    View RelationshipView = RelationshipStatusSpinner.getSelectedView();
                    TextView RelationshipListItem = (TextView)RelationshipView;
                    Mobile = findViewById(R.id.Usermobile);
                    if (App_type.getSelectedItem().toString().equals("--Select--")) {
                        tvListItem.setError("");
                        tvListItem.setTextColor(Color.RED);
                        tvListItem.setText("Required*");
                        tvListItem.requestFocus();
                        tvInvisibleError.requestFocus();
                        tvInvisibleError.setError("Required*");
                        return;
                    }else{
                        AppListItem.setError(null);
                        tvInvisibleError.setError(null);
                    }
                    if (FacultySpinner.getSelectedItem().toString().equals("--Empty--") || FacultySpinner.getSelectedItem().toString().equals("--Select--")) {
                        FacListItem.setError("");
                        FacListItem.setTextColor(Color.RED);
                        FacListItem.setText("Required*");
                        FacListItem.requestFocus();
                        return;
                    }
                    if (DepartmentSpinner.getSelectedItem().toString().equals("--Empty--") || DepartmentSpinner.getSelectedItem().toString().equals("-Choose Department-")) {
                        DepListItem.setError("");
                        DepListItem.setTextColor(Color.RED);
                        DepListItem.setText("Required*");
                        DepListItem.requestFocus();
                        return;
                    }
                    if (ProgramSpinner.getSelectedItem().toString().equals("--Empty--")) {
                        ProListItem.setError("");
                        ProListItem.setTextColor(Color.RED);
                        ProListItem.setText("Required*");
                        ProListItem.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(National_Identification_Number.getEditText().getText().toString())){
                        National_Identification_Number.setError("Required*");
                        National_Identification_Number.requestFocus();
                        return;
                    }else {
                        National_Identification_Number.setError(null);
                    }
                    if (TextUtils.isEmpty(Surname.getEditText().getText().toString())){
                        Surname.setError("Required*");
                        Surname.requestFocus();
                        return;
                    }else {
                        Surname.setError(null);
                    }
                    if (TextUtils.isEmpty(OtherName.getEditText().getText().toString())){
                        OtherName.setError("Required*");
                        OtherName.requestFocus();
                        OtherName.requestFocus();
                        return;
                    }else {
                        OtherName.setError(null);
                    }
                    if (TextUtils.isEmpty(etJoiningDate.getText().toString())){
                        etJoiningDate.setError("Required*");
                        etJoiningDate.requestFocus();
                        etJoiningDate.requestFocus();
                        return;
                    }else {
                        etJoiningDate.setError(null);
                    }
                    if (GenderSpinner.getSelectedItem().toString().equals("--Empty--") || GenderSpinner.getSelectedItem().toString().equals("--Select--")) {
                        GenderListItem.setError("");
                        GenderListItem.setTextColor(Color.RED);
                        GenderListItem.setText("Required*");
                        GenderListItem.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(Email.getEditText().getText().toString())){
                        Email.setError("Required*");
                        Email.requestFocus();
                        Email.requestFocus();
                        return;
                    }else {
                        Email.setError(null);
                    }
                    if (RelationshipStatusSpinner.getSelectedItem().toString().equals("--Empty--") || RelationshipStatusSpinner.getSelectedItem().toString().equals("--Select--")) {
                        RelationshipListItem.setError("");
                        RelationshipListItem.setTextColor(Color.RED);
                        RelationshipListItem.setText("Required*");
                        RelationshipListItem.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(Mobile.getEditText().getText().toString())){
                        Mobile.setError("Required*");
                        Mobile.requestFocus();
                        Mobile.requestFocus();
                        return;
                    }else {
                        Mobile.setError(null);
                        createAccount();
                    }
                } else{
                    Toast.makeText(Register.this, "Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createAccount() {
        //show progress dialog
        progressDialog.setMessage("Creating new account...");
        progressDialog.show();
        //store user data in firebase
        String email = Email.getEditText().getText().toString();
        StudentSurname = Surname.getEditText().getText().toString();
        StudentOthername = OtherName.getEditText().getText().toString();
        String password = StudentSurname+""+StudentOthername;
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), bcryptHashString);
        if(result.verified){
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //account creation success and add in realtime firebase database
                            updateUserInfo();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            //create account failed.
                            progressDialog.dismiss();
                            new SweetAlertDialog(Register.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Error!").setContentText(e.getMessage()).show();
                        }
                    });
        }
    }

    private void updateUserInfo() {
        progressDialog.setMessage("Saving Data info..!");
        //timestamp
        long timestamp = System.currentTimeMillis();
        Application = App_type.getSelectedItem().toString();
        Faculty =FacultySpinner.getSelectedItem().toString();
        Department= DepartmentSpinner.getSelectedItem().toString();
        Program = ProgramSpinner.getSelectedItem().toString();
        NiN= National_Identification_Number.getEditText().getText().toString();
        StudentSurname = Surname.getEditText().getText().toString();
        StudentOthername = OtherName.getEditText().getText().toString();
        StudentDateofBirth = etJoiningDate.getText().toString();
        StuntGender= GenderSpinner.getSelectedItem().toString();
        StudentEmail =Email.getEditText().getText().toString();
        StudentRelationStatus = RelationshipStatusSpinner.getSelectedItem().toString();
        StudentPhoneNumber = Mobile.getEditText().getText().toString();
        //Combined surname and other name to hash password
        String password = StudentSurname+""+StudentOthername;

        //hashing Student password
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), bcryptHashString);
        //get current user uid
        String uid = firebaseAuth.getUid();

        String mEmail = StudentEmail;
        String mMessage = StudentSurname+" "+StudentOthername+",\n\n Thank you for your interest in University of Lagos, "+Application+" Programmes\n Kindly make payment online in any branch of a commercial bank in Nigeria.\n\nPlease print out the following information(if you haven't previously printed it out), it will be required at the bank Please print out the following information(if you haven't previously printed it out), it will be required at the bank for payment purposes:";
        String mSubject = Application +" Application";

        //setup a data to add in db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("Application", Application);
        hashMap.put("Faculty", Faculty);
        hashMap.put("Department", Department);
        hashMap.put("Program", Program);
        hashMap.put("NIN", NiN);
        hashMap.put("Surname", StudentSurname);
        hashMap.put("Othername", StudentOthername);
        hashMap.put("DateOfBirth", StudentDateofBirth);
        hashMap.put("Gender", StuntGender);
        hashMap.put("Email", StudentEmail);
        hashMap.put("Relationship", StudentRelationStatus);
        hashMap.put("Mobile", StudentPhoneNumber);
        hashMap.put("Password", bcryptHashString);
        hashMap.put("timestamp", timestamp);

        //set data to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //send student their email
                        JavaMailAPI javaMailAPI = new JavaMailAPI(Register.this, mEmail, mSubject, mMessage);

                        javaMailAPI.execute();
                        //data added to db
                        progressDialog.dismiss();
                        new SweetAlertDialog(Register.this,  SweetAlertDialog.SUCCESS_TYPE).setTitleText("Successful!").setContentText("Verification mail has been sent to the email you provided. Please verify email to continue application.").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                //Redirect if user successfully registered.
                                startActivity(new Intent(Register.this, Login.class));
                                finish();
                            }
                        }).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        //data failed to add to db
                        progressDialog.dismiss();
                        new SweetAlertDialog(Register.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Error!").setContentText(e.getMessage()).show();
                    }
                });
    }

}