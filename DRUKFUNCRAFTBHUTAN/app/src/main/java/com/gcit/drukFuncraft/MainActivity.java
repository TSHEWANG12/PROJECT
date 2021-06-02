package com.gcit.drukFuncraft;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton addNewUser;
    private RecyclerView mRecycleView;
    public EditText inputSearch;
    private MainImageAdapter mainImageAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    DatabaseReference reference;
    FirebaseRecyclerOptions<PhotoUploadHelperClass> options;
    FirebaseRecyclerAdapter<PhotoUploadHelperClass,usermyviewholder> adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.main_drawer_layout);
        navigationView = findViewById(R.id.main_nav);
        toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        reference = FirebaseDatabase.getInstance().getReference("admin");

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.main_navigation_drawer_open,R.string.main_navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.main_nav);


        mRecycleView = (RecyclerView)findViewById(R.id.main_recycle_view);
        addNewUser = (FloatingActionButton)findViewById(R.id.addNewUser);
        inputSearch=(EditText)findViewById(R.id.inpuSearch);

        addNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<PhotoUploadHelperClass> options =
                new FirebaseRecyclerOptions.Builder<PhotoUploadHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("admin"),PhotoUploadHelperClass.class)
                        .build();
        mainImageAdapter = new MainImageAdapter(options);
        mRecycleView.setAdapter(mainImageAdapter);

        LoadData("");
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //search
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //search
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString()!=null){
                    LoadData(s.toString());
                }
                else {
                    LoadData("");
                }
            }
        });
    }
    private void LoadData(String data) {
        Query check= reference.orderByChild("title").startAt(data).endAt(data+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<PhotoUploadHelperClass>()
                .setQuery(check,PhotoUploadHelperClass.class)
                .build();
        adapter=new FirebaseRecyclerAdapter<PhotoUploadHelperClass, usermyviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull usermyviewholder holder, int position, @NonNull PhotoUploadHelperClass model) {
                holder.mTitle.setText(model.getTitle());
                holder.mDescription.setText(model.getShopName());
                holder.mCost.setText("Price "+model.getCost());
                holder.mContact.setText(model.getContact());
                Glide.with(holder.mImageView.getContext()).load(model.getPhotoUri()).into(holder.mImageView);
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
    protected void onStart() {
        super.onStart();
        mainImageAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainImageAdapter.stopListening();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case  R.id.nav_home:
                Intent intent1=new Intent(this,MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_about:
                Intent intent = new Intent(this,AboutusActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_Rate:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + this.getPackageName())));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
                break;
        }
        return true;
    }
}