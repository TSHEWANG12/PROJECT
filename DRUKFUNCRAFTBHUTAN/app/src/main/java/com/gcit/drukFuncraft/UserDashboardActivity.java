package com.gcit.drukFuncraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class UserDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecycleView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    FirebaseRecyclerOptions<PhotoUploadHelperClass> options;
    FirebaseRecyclerAdapter<PhotoUploadHelperClass,usermyviewholder> adapter;
    String email, license;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        //Get data from dashboard
        email = getIntent().getStringExtra("Email");
        license = getIntent().getStringExtra("License");
        databaseReference = FirebaseDatabase.getInstance().getReference(license);

        //Instantiate
        firebaseAuth = FirebaseAuth.getInstance();
        drawerLayout = (DrawerLayout) findViewById(R.id.user_drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.user_navigation);
        toolbar = (Toolbar)findViewById(R.id.user_toolbar);
        mRecycleView = (RecyclerView) findViewById(R.id.user_recycle_view);

        //Set toolbar in user dashboard
        setSupportActionBar(toolbar);

        //Set Navigation bar
        navigationView.bringToFront();

        //Set open and close navigation
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.user_open,R.string.user_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //User Click button checking
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.user_navigation);

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        Query check = databaseReference.orderByChild("title");
        options = new FirebaseRecyclerOptions.Builder<PhotoUploadHelperClass>()
                        .setQuery(check,PhotoUploadHelperClass.class)
                        .build();
        adapter=new FirebaseRecyclerAdapter<PhotoUploadHelperClass, usermyviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull usermyviewholder holder, int position, @NonNull PhotoUploadHelperClass model) {
                holder.mTitle.setText(model.getTitle());
                holder.mDescription.setText(model.getShopName());
                holder.mCost.setText("Price " +model.getCost());
                holder.mContact.setText(model.getContact());
                Glide.with(holder.mImageView.getContext()).load(model.getPhotoUri()).into(holder.mImageView);
                holder.mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(holder.mImageView.getContext(), UserImageIndividualActivity.class);
                        intent.putExtra("id", getRef(position).getKey());
                        intent.putExtra("License",license);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        holder.mImageView.getContext().startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public usermyviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_image_item_list, parent, false);
                return new usermyviewholder(v);
            }
        };
        adapter.startListening();
        mRecycleView.setAdapter(adapter);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case  R.id.home:
                Intent intent1=new Intent(getApplicationContext(),UserDashboardActivity.class);
                intent1.putExtra("Email",email);
                intent1.putExtra("License",license);
                startActivity(intent1);
                break;

            case R.id.upload:
            Intent intent = new Intent(this,PhotoUploadActivity.class);
            intent.putExtra("Email",email);
            intent.putExtra("License",license);
            startActivity(intent);
            break;

            case R.id.profile:
                Intent intent2=new Intent(this,UserprofileActivity.class);
                intent2.putExtra("Email",email);
                intent2.putExtra("License",license);
                startActivity(intent2);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intentHome = new Intent(this,MainActivity.class);
                startActivity(intentHome);
                finish();
                break;
        }
        return true;
    }

//    //Checks user is login or not
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(firebaseAuth.getCurrentUser() == null){
//            finish();
//            startActivity(new Intent(this,PhotoUploadActivity.class));
//        }
//    }
}