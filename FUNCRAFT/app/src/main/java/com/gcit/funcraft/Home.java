package com.gcit.funcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    EditText inputSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingbtn;
    FirebaseRecyclerOptions<Craft> options;
    FirebaseRecyclerAdapter<Craft,MyViewHolder> adapter;
    DatabaseReference DataRef;
    private Context context= Home.this;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = (DrawerLayout) findViewById(R.id.homedrawerlayout);
        navigationView = (NavigationView) findViewById(R.id.homenav);
        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.admin_navigation_drawer_open,R.string.admin_navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.homenav);

        inputSearch =findViewById(R.id.inpuSearch);
        recyclerView=findViewById(R.id.recyclerView);
        floatingbtn=findViewById(R.id.floatingbtn);
        DataRef = FirebaseDatabase.getInstance().getReference().child("Craft");
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
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
        Query query=DataRef.orderByChild("CraftName").startAt(data).endAt(data+"\uf8ff");
        options=new FirebaseRecyclerOptions.Builder<Craft>().setQuery(query,Craft.class).build();
        adapter=new FirebaseRecyclerAdapter<Craft, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Craft model) {
                holder.textView.setText(model.getCraftName());
                Picasso.get().load(model.getImageUrl()).into(holder.imageView);
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(Home.this, ViewActivity.class);
                        intent.putExtra("CraftKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });  

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view,parent,false);
                return  new MyViewHolder(v);

            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                Intent homeIntent=new Intent(this,Home.class);
                startActivity(homeIntent);
                break;
            case R.id.nav_post:
                Intent intent=new Intent(this,Login.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    public void RATE(MenuItem item) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + this.getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }
}