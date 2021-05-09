package com.gcit.drukcraftfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewActivity extends AppCompatActivity {
    private ImageView imageView;
    TextView textView;
    Button btnDelete;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        imageView=findViewById(R.id.image_single_view_Activity);
        textView=findViewById(R.id.textView_single_view_Activity);
        btnDelete=findViewById(R.id.btnDelete);
        ref= FirebaseDatabase.getInstance().getReference().child("Craft");

        String CraftKey=getIntent().getStringExtra("CraftKey");

       ref.child(CraftKey);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String CraftName = snapshot.child("CraftName").getValue().toString();
                    String ImageUrl = snapshot.child("ImageUrl").getValue().toString();
                    Picasso.get().load(ImageUrl).into(imageView);
                    textView.setText(CraftName);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            //error
            }
        });

    }
}