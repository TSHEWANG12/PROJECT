package com.gcit.drukFuncraft;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterImageUploadActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private Button mChooseBtn, mUploadBtn;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uploadPhotoUri;
    private ImageView uploadPhoto;
    private Context context = RegisterImageUploadActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_image_upload);


        //Instantiate the variables with Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users");

        mChooseBtn = (Button) findViewById(R.id.choosefile);
        mUploadBtn = (Button)findViewById(R.id.uploadfile);
        uploadPhoto = (ImageView)findViewById(R.id.uploadImage);

        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadPhoto();
            }
        });
    }
    //Creates file name
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //Upload photo btn
    private void uploadPhoto() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Registering...");
        progressDialog.show();
        String name = getIntent().getStringExtra("FullName");
        String email = getIntent().getStringExtra("Email");
        String license = getIntent().getStringExtra("License");
        String number = getIntent().getStringExtra("Number");
        String password = getIntent().getStringExtra("Password");
        if(uploadPhotoUri != null){
            final String photoId = reference.push().getKey();
            final StorageReference fileReference = FirebaseStorage.getInstance().getReference();
            StorageReference reference1 = fileReference.child("Users/" +photoId + ".png");
            reference1.putFile(uploadPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        user.sendEmailVerification().addOnCompleteListener(RegisterImageUploadActivity.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                RegisterHelperClass user = new RegisterHelperClass(name,email,license,number,password,uri.toString());
                                                reference.child(license).setValue(user);
                                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                                finish();
                                                Toast.makeText(getApplicationContext(),"The verification link has been successfully sent to your Email" + email, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                    else{
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Registering..." + (int)progress +"%");
                }
            });
        }
        else{
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"No file selected",Toast.LENGTH_SHORT).show();
        }
    }

    //Choose photo
    private void choosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    //Confirm file selected or not && uploaded or not && click on btn upload or not
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            uploadPhotoUri = data.getData();
            Picasso.get().load(uploadPhotoUri).into(uploadPhoto);
        }
    }
}