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

public class AdminImageIndividualActivity extends AppCompatActivity {

    private ImageView imageView;
    TextView textView, Description, Number, Cost;
    Button btnDelete;
    DatabaseReference reference;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_image_individual);
        String CraftKey = getIntent().getStringExtra("id");
        databaseReference = FirebaseDatabase.getInstance().getReference("admin");

        imageView = findViewById(R.id.image_single_view_Activity);
        Description=findViewById(R.id.Description);
        btnDelete=findViewById(R.id.btnDelete);
        Cost=findViewById(R.id.Cost);
        Number=findViewById(R.id.Number);
        textView = findViewById(R.id.title);

        // ref.child(CraftKey);
        databaseReference.child(CraftKey).addValueEventListener(new ValueEventListener() {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminImageIndividualActivity.this);
                builder.setTitle("Delete");
                builder.setMessage("Are sure you want to delete?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("admin").child(CraftKey).removeValue();
                        Toast.makeText(getApplicationContext(),"Deleted successfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminImageIndividualActivity.this,AdminDashboardActivity.class);
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
    }
}