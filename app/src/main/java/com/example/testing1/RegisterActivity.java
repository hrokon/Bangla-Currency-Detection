package com.example.testing1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFullName, editTextRegisterEmail, editTextRegisterDoB, editTextRegisterMobile, editTextRegisterPwd,
            editTextRegisterConfirmPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;
    private DatePickerDialog picker;
    private static final String TAG="RegisterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");

        Toast.makeText(RegisterActivity.this, "You cann register now", Toast.LENGTH_LONG).show();

        progressBar = findViewById(R.id.progressbBar);

        editTextRegisterFullName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDoB = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPwd = findViewById(R.id.editText_register_confirm_password);

        //Radio button for gender

        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();

        //Date Picker
        editTextRegisterDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              final Calendar calendar = Calendar.getInstance();
              int day = calendar.get(Calendar.DAY_OF_MONTH);
              int month = calendar.get(Calendar.MONTH);
              int year = calendar.get(Calendar.YEAR);

              //Date Picker Dialog
                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        editTextRegisterDoB.setText(dayOfMonth + "/" +(month+1) + "/" +year);
                    }
                 }, year,month,day);
                picker.show();
            }
        });


        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected = findViewById(selectGenderId);



                //obtain the entered data
                String textFullname = editTextRegisterFullName.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textDoB = editTextRegisterDoB.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textConfirmPwd = editTextRegisterConfirmPwd.getText().toString();
                String textGender;     //can'T get the value if gender is not selected





                // Checkeing whether any field is empty or not?

                if (TextUtils.isEmpty(textFullname)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Full Name", Toast.LENGTH_LONG).show();
                    editTextRegisterFullName.setError("Full Name is Required");
                    editTextRegisterFullName.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your E-mail", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("E-mail is Required");
                    editTextRegisterEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please Re-enter Your E-mail", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError(" Valid E-mail is Required");
                    editTextRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(textDoB)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Date of Birth", Toast.LENGTH_LONG).show();
                    editTextRegisterDoB.setError("Birth Date is Required");
                    editTextRegisterDoB.requestFocus();
                } else if (radioGroupRegisterGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(RegisterActivity.this, "Please Select Your Gender", Toast.LENGTH_LONG).show();
                    radioButtonRegisterGenderSelected.setError("Gender is Required");
                    radioButtonRegisterGenderSelected.requestFocus();
                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Phone Number", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Phone Number is Required");
                    editTextRegisterMobile.requestFocus();
                } else if (textMobile.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "Please Re-enter Your Phone Number", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Phone Number Should be 10 digits");
                    editTextRegisterMobile.requestFocus();
                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Password", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Password is Required");
                    editTextRegisterPwd.requestFocus();
                } else if (textPwd.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password Should be at least 6 digits", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Too weak password");
                    editTextRegisterPwd.requestFocus();
                } else if (TextUtils.isEmpty(textConfirmPwd)) {
                    Toast.makeText(RegisterActivity.this, "Please Confirm Your Password", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("Confirm Password is required");
                    editTextRegisterConfirmPwd.requestFocus();
                } else if (!textPwd.equals(textConfirmPwd)) {
                    Toast.makeText(RegisterActivity.this, "Password Doesn't Match", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("Confirm Password is required");
                    editTextRegisterConfirmPwd.requestFocus();

                    //clear the entered password

                    editTextRegisterPwd.clearComposingText();
                    editTextRegisterConfirmPwd.clearComposingText();
                } else {
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullname, textEmail, textDoB, textGender, textMobile, textPwd);
                }
            }

        });


    }

    //Users regisretion
    private void registerUser(String textFullname, String textEmail, String textDob, String textGender, String textMobile, String textPwd) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //Enter user data into firevbase.
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFullname, textDob, textGender,textMobile);


                    //User Reference from database for Register users

                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                         if (task.isSuccessful()){
                             //send verification.

                             firebaseUser.sendEmailVerification();

                             Toast.makeText(RegisterActivity.this, "User Registration Succseefully. Please verify your email", Toast.LENGTH_LONG).show();

//                             //open user profile after verification
//                             Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
//                             //To prevent user
//                             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                             startActivity(intent);
//                             finish(); //to close register activity

                         } else {
                             Toast.makeText(RegisterActivity.this, "Registration failed, try again", Toast.LENGTH_LONG).show();

                         }
                            progressBar.setVisibility(View.GONE);

                        }
                    });



                } else{
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        editTextRegisterPwd.setError("Your Password is too weak; Use mixed letters and numbers");
                        editTextRegisterPwd.requestFocus();
                    }  catch (FirebaseAuthInvalidCredentialsException e){
                        editTextRegisterPwd.setError("Invalid email or already in use");
                        editTextRegisterPwd.requestFocus();
                    }  catch (FirebaseAuthUserCollisionException e){
                        editTextRegisterPwd.setError("User already registered");
                        editTextRegisterPwd.requestFocus();
                    }  catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(RegisterActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }

            }
        });

    }



}
