package com.gcit.drukFuncraft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminLicenseActivity extends AppCompatActivity {
    private RecyclerView mRecycleView;
    LinearLayout linearLayout1, linearLayout2;
    EditText licenseNo;
    Button add;
    DatabaseReference reference;
    FloatingActionButton floatingActionButton;
    LicenseAdapter licenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_license);

        mRecycleView = findViewById(R.id.user_recycle_view);
        linearLayout1 = findViewById(R.id.linear1);
        linearLayout2 = findViewById(R.id.linear2);
        licenseNo = findViewById(R.id.licenseNo);
        add = findViewById(R.id.add);
        floatingActionButton = findViewById(R.id.floating_add);
        linearLayout2.setVisibility(View.GONE);
        reference = FirebaseDatabase.getInstance().getReference("LicenseNo");

        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<LicenseNumberHelperClass> options =
                new FirebaseRecyclerOptions.Builder<LicenseNumberHelperClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference("LicenseNo"),LicenseNumberHelperClass.class)
                .build();
        licenseAdapter = new LicenseAdapter(options);
        mRecycleView.setAdapter(licenseAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.VISIBLE);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String license = licenseNo.getText().toString().trim();
                if(license.isEmpty()){
                    licenseNo.setError("Field is Required");
                    licenseNo.requestFocus();
                }
                else{
                    LicenseNumberHelperClass user = new LicenseNumberHelperClass(license);
                    reference.child(license).setValue(user);
                    Toast.makeText(getApplicationContext(),"Added successfully",Toast.LENGTH_SHORT).show();
                    licenseNo.setText("");
                    linearLayout1.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        licenseAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        licenseAdapter.startListening();
    }
}