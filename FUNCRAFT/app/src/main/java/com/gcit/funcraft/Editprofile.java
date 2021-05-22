package com.gcit.funcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Editprofile extends AppCompatActivity {
    private DatabaseReference reference;
    private TextView userName, userEmail, userPhone, userLicenseNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        //Get data from MainActivity
        String email = getIntent().getStringExtra("Email");

        reference = FirebaseDatabase.getInstance().getReference("Users");
        userName = (TextView)findViewById(R.id.userName);
        userEmail = (TextView)findViewById(R.id.email);
        userPhone = (TextView)findViewById(R.id.phoneNo);
        userLicenseNo = (TextView)findViewById(R.id.licenseNo);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot db : snapshot.getChildren()){
                    if(db.child("inputEmail").getValue().equals(email)){
                        String userNameDB = db.child("inputUsername").getValue(String.class);
                        String emailDB = db.child("inputEmail").getValue(String.class);
                        String phoneDB = db.child("inputCall").getValue(String.class);
                        String licenseDB = db.child("inputLicenseno").getValue(String.class);
                        userName.setText(userNameDB);
                        userEmail.setText(emailDB);
                        userPhone.setText(phoneDB);
                        userLicenseNo.setText(licenseDB);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}