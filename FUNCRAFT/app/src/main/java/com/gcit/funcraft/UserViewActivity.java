package com.gcit.funcraft;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserViewActivity extends AppCompatActivity {
    private ImageView imageView;
    TextView textView;
    TextView Description;
    TextView Number;
    TextView Cost;
    Button btnDelete;
    DatabaseReference ref,DataRef;
    StorageReference StrogeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);
        imageView = findViewById(R.id.image_single_view_Activity);
        Description=findViewById(R.id.Description);
        btnDelete=findViewById(R.id.btnDelete);
        Cost=findViewById(R.id.Cost);
        Number=findViewById(R.id.Number);
        textView = findViewById(R.id.textView_single_view_Activity);
        ref = FirebaseDatabase.getInstance().getReference().child("Craft");
        String CraftKey = getIntent().getStringExtra("CraftKey");
        DataRef=FirebaseDatabase.getInstance().getReference().child("Craft").child(CraftKey);
        StrogeRef= FirebaseStorage.getInstance().getReference().child("CraftName").child(CraftKey+".jpg");


        // ref.child(CraftKey);
        ref.child(CraftKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String CraftName = snapshot.child("CraftName").getValue().toString();
                    String ImageUrl = snapshot.child("ImageUrl").getValue().toString();
                    String description = snapshot.child("Description").getValue().toString();
                    String cost=snapshot.child("Cost").getValue().toString();
                    String number = snapshot.child("Number").getValue().toString();
                    Picasso.get().load(ImageUrl).into(imageView);
                    textView.setText(CraftName);
                    Description.setText(description);
                    Cost.setText(cost);
                    Number.setText(number);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //error
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                        StrogeRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(),Home.class));
                            }
                        });
                    }
                });
            }
        });

    }

}
