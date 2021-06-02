package com.gcit.drukFuncraft;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class viewShopDetailsActivity extends AppCompatActivity {

    private RecyclerView mRecycleView;
    private viewImageAdapter viewImageAdapter;
    private TextView mHeader;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shop_details);

        mRecycleView = (RecyclerView)findViewById(R.id.view_recycle_view);
        mHeader = (TextView)findViewById(R.id.header);
        String license = getIntent().getStringExtra("License");
        System.out.println("HHHHH" +license);
        String fullName = getIntent().getStringExtra("FullName");
        mHeader.setText(fullName);

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<PhotoUploadHelperClass> options =
                new FirebaseRecyclerOptions.Builder<PhotoUploadHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(license),PhotoUploadHelperClass.class)
                        .build();
        viewImageAdapter = new viewImageAdapter(options);
        mRecycleView.setAdapter(viewImageAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        viewImageAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewImageAdapter.stopListening();
    }
}