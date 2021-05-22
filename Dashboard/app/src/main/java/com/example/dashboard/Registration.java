package com.example.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    TextView btn;
    private EditText inputUsername;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private EditText inputCall;
    private EditText inputAddress;
    Button btnRegister;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btn = findViewById(R.id.account);
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        inputCall = findViewById(R.id.inputCall);
        inputAddress = findViewById(R.id.inputAddress);
        btnRegister = findViewById(R.id.registerbtn);
        mAuth=FirebaseAuth.getInstance();
        mLoadingBar=new ProgressDialog(Registration.this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { checkCrededentials();
                rootNode =FirebaseDatabase.getInstance();
                reference=rootNode.getReference("Users");
                //get all the values
                String username = inputUsername.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String confirmPassword = inputConfirmPassword.getText().toString();
                String number = inputCall.getText().toString();
                String address = inputAddress.getText().toString();
                UserHelperClass helperClass = new UserHelperClass(username,email,password,confirmPassword,number,address);
                reference.child(number).setValue(helperClass);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, Login.class));
            }
        });
    }
    private void checkCrededentials() {
        String username = inputUsername.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPassword.getText().toString();
        String number = inputCall.getText().toString();
        String address = inputAddress.getText().toString();
        if (username.isEmpty() || username.length() < 7) {
            showError(inputUsername, "Username is not valid");
        }
        else if (email.isEmpty() || !email.contains("@")) {
            showError(inputEmail, "email.is not valid");
        }
        else if (password.isEmpty() || password.length() < 7) {
            showError(inputPassword, "Password is not valid");
        }
        else if (confirmPassword.isEmpty() || confirmPassword.equals(inputPassword)) {
            showError(inputConfirmPassword, "password is not match");
        }
        else if (number.isEmpty() || number.length() < 8) {
            showError(inputCall, "invalid number");
        }
        else if  (address.isEmpty() || address.length() < 7) {
            showError(inputAddress, "invalid address");
        }
        else{
            mLoadingBar.setTitle("Registration");
            mLoadingBar.setMessage("Please wait while registering");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Registration.this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Registration.this, "Successful registration,Check your email to verify", Toast.LENGTH_SHORT).show();
                                    mLoadingBar.dismiss();
                                    Intent intent= new Intent(Registration.this,Login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(Registration.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(Registration.this,task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}