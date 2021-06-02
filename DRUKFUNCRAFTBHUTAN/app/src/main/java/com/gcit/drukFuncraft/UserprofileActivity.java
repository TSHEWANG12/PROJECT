package com.gcit.drukFuncraft;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserprofileActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private ImageView profile, editProfile;
    private LinearLayout linearLayout1, linearLayout2;
    private TextView userName, userEmail, userPhone, userLicenseNo;
    private EditText editUserName, editUserEmail, editUserPhone, editUserLicenseNo;
    String emailIntent, license;
    private Button edit, update;
    private Uri uploadPhotoUri;
    private Context context = UserprofileActivity.this;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        String email = getIntent().getStringExtra("Email");
        emailIntent = email;
        String licensNo = getIntent().getStringExtra("License");
        license = licensNo;

        editUserName = findViewById(R.id.editUserName);
        editUserEmail = findViewById(R.id.editEmail);
        editUserPhone = findViewById(R.id.editPhoneNo);
        editUserLicenseNo = findViewById(R.id.editLicenseNo);
        editProfile = findViewById(R.id.EditProfile);

        linearLayout1 = findViewById(R.id.linear);
        linearLayout2 = findViewById(R.id.linear2);
        linearLayout2.setVisibility(View.GONE);

        reference = FirebaseDatabase.getInstance().getReference("Users");
        userName = (TextView) findViewById(R.id.userName);
        userEmail = (TextView) findViewById(R.id.email);
        userPhone = (TextView) findViewById(R.id.phoneNo);
        userLicenseNo = (TextView) findViewById(R.id.licenseNo);
        profile = findViewById(R.id.profile);


        //Button
        edit = findViewById(R.id.edit);
        update = findViewById(R.id.update);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot db : snapshot.getChildren()) {
                    if ((db.child("email").getValue()).equals(emailIntent)) {
                        String userNameDB = db.child("fullName").getValue(String.class);
                        String photoUri = db.child("photoUri").getValue(String.class);
                        String emailDB = db.child("email").getValue(String.class);
                        String phoneDB = db.child("contact").getValue(String.class);
                        String licenseDB = db.child("license").getValue(String.class);
                        Picasso.get().load(photoUri).into(profile);
                        userName.setText(userNameDB);
                        userEmail.setText(emailDB);
                        userPhone.setText(phoneDB);
                        userLicenseNo.setText(licenseDB);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.VISIBLE);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot db : snapshot.getChildren()) {
                            if ((db.child("email").getValue()).equals(emailIntent)) {
                                String userNameDB = db.child("fullName").getValue(String.class);
                                String photoUri = db.child("photoUri").getValue(String.class);
                                String emailDB = db.child("email").getValue(String.class);
                                String phoneDB = db.child("contact").getValue(String.class);
                                String licenseDB = db.child("license").getValue(String.class);
                                Picasso.get().load(photoUri).into(editProfile);
                                editUserName.setText(userNameDB);
                                editUserEmail.setText(emailDB);
                                editUserPhone.setText(phoneDB);
                                editUserLicenseNo.setText(licenseDB);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
    }
    //Upload photo btn
    private void uploadPhoto() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Registering...");
        progressDialog.show();
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
                            String userName = editUserName.getText().toString().trim();
                            String userEmail = editUserEmail.getText().toString().trim();
                            String userPhone = editUserPhone.getText().toString().trim();
                            String userLicense = editUserLicenseNo.getText().toString().trim();
                            String photoUri = uri.toString();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                            Query checkUser = reference.orderByChild("license").equalTo(license);
                            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        reference.child(license).child("fullName").setValue(userName);
                                        reference.child(license).child("email").setValue(userEmail);
                                        reference.child(license).child("license").setValue(userLicense);
                                        reference.child(license).child("contact").setValue(userPhone);
                                        reference.child(license).child("photoUri").setValue(photoUri);
                                        linearLayout1.setVisibility(View.VISIBLE);
                                        linearLayout2.setVisibility(View.GONE);
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Details Updated Successfully",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
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
                    progressDialog.setMessage("Updating..." + (int)progress +"%");
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
            Picasso.get().load(uploadPhotoUri).into(editProfile);
        }
    }
}