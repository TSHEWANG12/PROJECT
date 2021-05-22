package com.gcit.funcraft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewActivity extends AppCompatActivity {
    private ImageView imageView;
    TextView textView;
    TextView Description;
    TextView Number;
    TextView Cost;
    Button btnDelete;
    DatabaseReference ref;
    String s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        imageView = findViewById(R.id.image_single_view_Activity);
        Description=findViewById(R.id.Description);
        Cost=findViewById(R.id.Cost);
        Number=findViewById(R.id.Number);
        textView = findViewById(R.id.textView_single_view_Activity);
        ref = FirebaseDatabase.getInstance().getReference().child("Craft");

        String CraftKey = getIntent().getStringExtra("CraftKey");

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
                    s1 = number;
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

    }

    public void Call(View view) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        String p = "tel:" + s1;
        i.setData(Uri.parse(p));
        startActivity(i);
    }
}
