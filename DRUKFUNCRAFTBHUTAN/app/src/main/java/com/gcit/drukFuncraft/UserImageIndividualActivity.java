package com.gcit.drukFuncraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserImageIndividualActivity extends AppCompatActivity {

    private ImageView imageView;
    LinearLayout linearLayout1, linearLayout2, linearLayout3;
    TextView textView, Description, Number, Cost;
    EditText editTextView, editTextDescription, editTextNumber, editTextCost;
    Button btnDelete, btnEdit, btnUpdate;
    DatabaseReference reference;
    DatabaseReference databaseReference;
    String licenseNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_image_individual);
        String license = getIntent().getStringExtra("License");
        licenseNo = license;
        String CraftKey = getIntent().getStringExtra("id");
        reference = FirebaseDatabase.getInstance().getReference(license);
        databaseReference = FirebaseDatabase.getInstance().getReference("admin");

        imageView = findViewById(R.id.image_single_view_Activity);
        Description=findViewById(R.id.Description);
        btnDelete=findViewById(R.id.btnDelete);
        Cost=findViewById(R.id.Cost);
        Number=findViewById(R.id.Number);
        textView = findViewById(R.id.textView_single_view_Activity);

        linearLayout1 = findViewById(R.id.edit_linear_layout);
        linearLayout2 = findViewById(R.id.edit1_linear_layout);
        linearLayout3 = findViewById(R.id.edit2_linear_layout);

        editTextView = findViewById(R.id.single_view_Activity);
        editTextDescription = findViewById(R.id.Edit_Description);
        editTextNumber = findViewById(R.id.Edit_Number);
        editTextCost = findViewById(R.id.Edit_Cost);
        btnEdit = findViewById(R.id.btnEdit);
        btnUpdate = findViewById(R.id.btnUpdate);

        linearLayout2.setVisibility(View.GONE);

        // ref.child(CraftKey);
        reference.child(CraftKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String CraftName = snapshot.child("title").getValue().toString();
                    String ImageUrl = snapshot.child("photoUri").getValue().toString();
                    String description = snapshot.child("shopName").getValue().toString();
                    String cost=snapshot.child("cost").getValue().toString();
                    String number = snapshot.child("contact").getValue().toString();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(UserImageIndividualActivity.this);
                builder.setTitle("Delete");
                builder.setMessage("Are sure you want to delete?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child(license).child(CraftKey).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("admin").child(CraftKey).removeValue();
                        Toast.makeText(getApplicationContext(),"Deleted successfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserImageIndividualActivity.this,UserDashboardActivity.class);
                        intent.putExtra("License",licenseNo);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }

        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout1.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.VISIBLE);

                reference.child(CraftKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String CraftName = snapshot.child("title").getValue().toString();
                            String description = snapshot.child("shopName").getValue().toString();
                            String cost=snapshot.child("cost").getValue().toString();
                            String number = snapshot.child("contact").getValue().toString();
                            editTextView.setText(CraftName);
                            editTextDescription.setText(description);
                            editTextCost.setText(cost);
                            editTextNumber.setText(number);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        //error
                    }
                });
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextView.getText().toString();
                String description = editTextDescription.getText().toString();
                String cost = editTextCost.getText().toString();
                String number = editTextNumber.getText().toString();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(license);
                Query checkUser = reference.child(CraftKey);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            reference.child(CraftKey).child("title").setValue(name);
                            reference.child(CraftKey).child("shopName").setValue(description);
                            reference.child(CraftKey).child("cost").setValue(cost);
                            reference.child(CraftKey).child("contact").setValue(number);
                            databaseReference.child(CraftKey).child("title").setValue(name);
                            databaseReference.child(CraftKey).child("shopName").setValue(description);
                            reference.child(CraftKey).child("cost").setValue(cost);
                            databaseReference.child(CraftKey).child("contact").setValue(number);
                            linearLayout1.setVisibility(View.VISIBLE);
                            linearLayout2.setVisibility(View.GONE);
                            linearLayout3.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"Details Updated Successfully",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}