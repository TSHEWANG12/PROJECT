package com.gcit.drukcraftfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Registration extends AppCompatActivity {
    TextView btn;
    private EditText inputUsername;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private EditText inputLicenseno;
    private EditText inputCall;
    private ImageView imageLicense;
    private Uri imageUri=null;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    Button btnRegister;
    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        btn = findViewById(R.id.Account);
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        inputCall = findViewById(R.id.inputCall);
        inputLicenseno = findViewById(R.id.inputLicenseno);
        btnRegister = findViewById(R.id.btnRegister);
//        imageLicense=findViewById(R.id.imageLicense);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
//        imageLicense.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                choosePicture();
//            }
//        });
        mAuth= FirebaseAuth.getInstance();
        mLoadingBar=new ProgressDialog(Registration.this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
                rootNode =FirebaseDatabase.getInstance();
                reference=rootNode.getReference("Users");
                String username = inputUsername.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String confirmPassword = inputConfirmPassword.getText().toString();
                String number = inputCall.getText().toString();
                String address = inputLicenseno.getText().toString();
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

    //    private void choosePicture() {
//        Intent intent =new Intent();
//        intent.setType("image/");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent,1);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
//            imageUri = data.getData();
//            imageLicense.setImageURI(imageUri);
//            uploadPicture();
//        }
//    }
//
//    private void uploadPicture() {
//
//        final String randomKey = UUID.randomUUID().toString();
//        StorageReference riversRef =storageReference.child("image/"+ randomKey);
//        final ProgressDialog pd=new ProgressDialog(this);
//        pd.setTitle("upload image");
//        pd.show();
//
//       riversRef.putFile(imageUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        pd.dismiss();
//                        Snackbar.make(findViewById(android.R.id.content),"Image upload",Snackbar.LENGTH_LONG).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        pd.dismiss();
//                       Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    private StreamDownloadTask.TaskSnapshot taskSnapshot;
//
//                    @Override
//                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                        double progressPercent =(100.00 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
//                        pd.setMessage("Percentage:"+(int)progressPercent + "%");
//                    }
//
//                });
//
//    }
    private void checkCredentials() {
        String username = inputUsername.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPassword.getText().toString();
        String number = inputCall.getText().toString();
        String address = inputLicenseno.getText().toString();

        if (username.isEmpty() || username.length() < 5) {
            showError(inputUsername, "Username should be more than 5.");
        }
        else if (email.isEmpty() || !email.contains("@")) {
            showError(inputEmail, "Email is not valid");
        }
        else if (password.isEmpty() || password.length()<6 || password.length()>8 ) {
            showError(inputPassword, "Password should not be less than 6 and more than 8");
        }
        else if (confirmPassword.isEmpty() || confirmPassword.equals(inputPassword)) {
            showError(inputConfirmPassword, "password is not match");
        }
        else if (number.isEmpty() || number.length()<7 || number.length()>8) {
            showError(inputCall, "Phone number should be of 8 digits.");
        }
        else if  (address.isEmpty() || address.length()<6 || address.length()>7) {
            showError(inputLicenseno, "License number should be of 7 digits.");
        }
        else
        {

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
