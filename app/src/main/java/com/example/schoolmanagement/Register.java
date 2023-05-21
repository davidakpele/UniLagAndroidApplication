package com.example.schoolmanagement;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class Register extends AppCompatActivity {

    private String selectedFaculty, selectedDepartment, SelectedApplication, SelectedProgram, SelectGender, SelectRelationshipStatus;                 //vars to hold the values of selected State and District
    //declaring TextView to show the errors
    private Spinner  App_type, FacultySpinner, DepartmentSpinner, ProgramSpinner, GenderSpinner, RelationshipStatusSpinner;                //Spinners
    // on below line creating a variable.
    private DatePickerDialog datePickerDialog;
    private TextInputEditText etJoiningDate;
    private ArrayAdapter<CharSequence> AppAdapter, facultyAdapter, DepartmentAdapter, ProgramAdapter, GenderAdapter, RelationshipStatusAdapter;   //declare adapters for the spinners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etJoiningDate=(TextInputEditText)findViewById(R.id.idEdtDate);
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
    }
}