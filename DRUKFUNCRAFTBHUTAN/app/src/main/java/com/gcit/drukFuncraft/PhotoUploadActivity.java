package com.gcit.drukFuncraft;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;

public class PhotoUploadActivity extends AppCompatActivity {

    //Declare all required variable
    private FirebaseAuth firebaseAuth;
    private EditText mTitle, mShopName,mCost,mContact;
    private Button mChooseBtn, mUploadBtn;
    private ImageView uploadPhoto;
    Uri imageFilePath;
    StorageReference storageReference;
    private Context context = PhotoUploadActivity.this;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uploadPhotoUri;
    Bitmap bitmap;
    private DatabaseReference reference;
    String email, licenseNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload);

        //Get value from Login activity
        String emailIntent = getIntent().getStringExtra("Email");
        String license = getIntent().getStringExtra("License");
        email = emailIntent;
        licenseNo = license;
        reference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        //Instantiate all the variable with XML
        firebaseAuth = FirebaseAuth.getInstance();
        mTitle = (EditText) findViewById(R.id.phototitle);
        mShopName = (EditText)findViewById(R.id.shopname);
        mCost=(EditText)findViewById(R.id.cost);
        mContact = (EditText)findViewById(R.id.contactnumber);
        uploadPhoto = (ImageView) findViewById(R.id.uploadImage);
        mChooseBtn = (Button) findViewById(R.id.choosefile);
        mUploadBtn = (Button)findViewById(R.id.uploadfile);

        //Get data from realtime database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot db : snapshot.getChildren()){
                    if(db.child("email").getValue().equals(email)){
                        licenseNo = db.child("license").getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Image Files"),101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processUpload(imageFilePath);
            }
        });
    }

    private void processUpload(Uri imageFilePath) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.show();
        if(imageFilePath != null){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(licenseNo);
            String photoId = databaseReference.push().getKey();
            StorageReference reference = storageReference.child("Images/" +photoId + ".png");
            reference.putFile(imageFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(getApplicationContext(),"Uploaded Successfully", Toast.LENGTH_LONG).show();
                                    String title = mTitle.getText().toString().trim();
                                    String shopName = mShopName.getText().toString().trim();
                                    String cost=mCost.getText().toString().trim();
                                    String contact = mContact.getText().toString().trim();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("admin");
                                    PhotoUploadHelperClass photo = new PhotoUploadHelperClass(title, shopName,cost, contact,uri.toString());
                                    databaseReference.child(photoId).setValue(photo);
                                    reference.child(photoId).setValue(photo);
                                    Intent intent2=new Intent(getApplicationContext(),UserDashboardActivity.class);
                                    intent2.putExtra("Email",email);
                                    intent2.putExtra("License",licenseNo);
                                    startActivity(intent2);
                                    //Clear editText
                                    mTitle.setText("");
                                    mShopName.setText("");
                                    mContact.setText("");
                                    mCost.setText("");
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float percent = (100 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            progressDialog.setMessage("File Uploading...." + (int)percent + "%");
                        }
                    });
        }
        else{
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"No file selected",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK){
            imageFilePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageFilePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                uploadPhoto.setImageBitmap(bitmap);
            }
            catch(Exception e){

            }
        }
    }
}