package com.gcit.drukFuncraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    //All Variable Declaration
    private TextInputEditText mFullName, mEmail, mLicense, mContactNumber, mPassword, mConfirmPassword;
    private Button signup_btn;
    private TextView Account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Instantiate the variables with XML file
        mFullName =  findViewById(R.id.FullName);
        mEmail = findViewById(R.id.Email);
        mLicense = findViewById(R.id.LicenseNo);
        mContactNumber = findViewById(R.id.ContactNumber);
        mPassword = findViewById(R.id.Password);
        mConfirmPassword = findViewById(R.id.ConfirmPassword);
        signup_btn = (Button) findViewById(R.id.signup_btn);
        Account=(TextView) findViewById(R.id.Account);
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent) ;
            }
        });

        //Create account
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateFullName() | !validateEmail() | !validateLicense() | !validateContact() | !validatePassword()){
                    return;
                }
                else{
                    String fullName = mFullName.getText().toString().trim();
                    String email = mEmail.getText().toString().trim();
                    String license = mLicense.getText().toString().trim();
                    String number = mContactNumber.getText().toString().trim();
                    String password = mPassword.getText().toString().trim();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LicenseNo");
                    Query checkUser = databaseReference.orderByChild("licenseNo").equalTo(license);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                mLicense.setError(null);
                                mLicense.setEnabled(false);
                                Intent intent = new Intent(getApplicationContext(),RegisterImageUploadActivity.class);
                                intent.putExtra("FullName",fullName);
                                intent.putExtra("Email",email);
                                intent.putExtra("License",license);
                                intent.putExtra("Number",number);
                                intent.putExtra("Password",password);
                                startActivity(intent);
                                finish();
                                mFullName.setText("");
                                mEmail.setText("");
                                mLicense.setText("");
                                mContactNumber.setText("");
                                mPassword.setText("");
                                mConfirmPassword.setText("");
                            }
                            else{
                                mLicense.setError("No such license number");
                                mLicense.requestFocus();
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //Full Name validation
    private boolean validateFullName() {
        String val = mFullName.getText().toString().trim();
        if(val.isEmpty()){
            mFullName.setError("Name is Required!");
            mFullName.requestFocus();
            return false;
        }
        return true;
    }

    //Email validation
    private boolean validateEmail() {
        String val = mEmail.getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(val.isEmpty()){
            mEmail.setError("Email Address is Required!");
            mEmail.requestFocus();
            return false;
        }
        else if(!val.matches(checkEmail)){
            mEmail.setError("Invalid Email Address!");
            mEmail.requestFocus();
            return false;
        }
        return true;
    }
    //License validation
    private boolean validateLicense() {
        String val = mLicense.getText().toString().trim();
        if(val.isEmpty()){
            mLicense.setError("License number is Required!");
            mLicense.requestFocus();
            return false;
        }
        else if(val.length() != 6){
            mLicense.setError("Your license number should exactly six!");
            mLicense.requestFocus();
            return false;
        }
        return true;
    }
    //Validate phone number
    private boolean validateContact() {
        String val = mContactNumber.getText().toString().trim();
        final Pattern BPHONE_NUMBER = Pattern.compile("[1][7][0-9]{6}",Pattern.CASE_INSENSITIVE);
        if(val.isEmpty()){
            mContactNumber.setError("Contact number is Required!");
            mContactNumber.requestFocus();
            return false;
        }
        else if(!BPHONE_NUMBER.matcher(val).matches()){
            mContactNumber.setError("Invalid Contact Number");
            mContactNumber.requestFocus();
            return false;
        }
        return true;
    }

    //Password validation
    private boolean validatePassword() {
        String val = mPassword.getText().toString().trim();
        String val1 = mConfirmPassword.getText().toString().trim();
        if(val.isEmpty()){
            mPassword.setError("Password is Required!");
            mPassword.requestFocus();
            return false;
        }
        else if(val1.isEmpty()){
            mConfirmPassword.setError("Confirm Password is Required!");
            mConfirmPassword.requestFocus();
            return false;
        }
        else if(val.length() < 8) {
            mPassword.setError("Password is too short, it should be at least 8!");
            mPassword.requestFocus();
            return false;
        }
        else if(!val.equals(val1)){
            mConfirmPassword.setError("Confirm Password is didn't match");
            mConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }

    public void GoToLoginPage(View view) {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
}