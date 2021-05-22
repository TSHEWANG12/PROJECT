package com.gcit.funcraft;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static com.gcit.funcraft.R.*;

public class MainActivity extends AppCompatActivity {
    private ImageView ImageViewAdd;
    private EditText inputImageName,Description,Cost,Number;
    private TextView textViewProgress;
    private ProgressBar progressBar;
    private Button btnUpload;
    String emailIntent;

    private int REQUEST_CODE_IMAGE=101;
    Uri imageUri;
    boolean isImageAdded=false;
    DatabaseReference Dataref;
    StorageReference StorageRef;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get data from login page
        String email = getIntent().getStringExtra("Email");
        emailIntent = email;

        ImageViewAdd=findViewById(id.imageViewAdd);
        inputImageName=findViewById(id.inputImageName);
        Description =findViewById(id.inputDescription);
        Cost=findViewById(id.inputcost);
        Number=findViewById(id.inputNumber);
        textViewProgress=findViewById(id.textviewProgress);
        progressBar=findViewById(id.progressBar);
        btnUpload=findViewById(id.btnUpload);

        navigationView = (BottomNavigationView)findViewById(id.bottom_nav);
        navigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case id.pofileid:
                        Intent intent = new Intent(getApplicationContext(), Editprofile.class);
                        intent.putExtra("Email", emailIntent);
                        startActivity(intent);
                        break;

                    case id.homeid:
                        Intent intent1=new Intent(getApplicationContext(),Home.class);
                        intent1.putExtra("Email",emailIntent);
                        startActivity(intent1);
                        break;
                    case id.galleryid:
                        Intent intent3=new Intent(getApplicationContext(),UserHome.class);
                        intent3.putExtra("Email",emailIntent);
                        startActivity(intent3);
                        break;
                }
            }
        });

        textViewProgress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        Dataref = FirebaseDatabase.getInstance().getReference().child("Craft");
        StorageRef= FirebaseStorage.getInstance().getReference().child("CraftName");

        ImageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String imageName =inputImageName.getText().toString();
                final String description =Description.getText().toString();
                final String cost=Cost.getText().toString();
                final String number =Number.getText().toString();
                if(isImageAdded!=false && imageName!=null){
                    uploadImage(imageName,description,cost,number);
                }

            }
        });
    }

    private void uploadImage(final String imageName, final String description,final String cost,final String number) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.show();
        final String key=Dataref.push().getKey();
        StorageRef.child(key+".jpg").putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        StorageRef.child(key+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap hashMap= new HashMap();
                                hashMap.put("CraftName",imageName);
                                hashMap.put("Description",description);
                                hashMap.put("Cost",cost);
                                hashMap.put("Number",number);
                                hashMap.put("ImageUrl",uri.toString());
                                Dataref.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        startActivity(new Intent(getApplicationContext(),Home.class));
                                        //Toast.makeText(MainActivity.this,"Data sucessfully uploaded",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress=(snapshot.getBytesTransferred()*100)/snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploading..." + (int) progress + "%");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_IMAGE && data!=null){
            imageUri=data.getData();
            isImageAdded=true;
            ImageViewAdd.setImageURI(imageUri);
        }
    }
}