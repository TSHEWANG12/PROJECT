package com.gcit.funcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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

public class UserHome extends AppCompatActivity {
    EditText inputSearch;
    RecyclerView recyclerView;

    FirebaseRecyclerOptions<Craft> options;
    FirebaseRecyclerAdapter<Craft,MyViewHolder> adapter;
    DatabaseReference DataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        recyclerView=findViewById(R.id.recyclerView);
        DataRef = FirebaseDatabase.getInstance().getReference().child("Craft");
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);


        LoadData("");

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
                        Intent intent =new Intent(UserHome.this, UserViewActivity.class);
                        intent.putExtra("CraftKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.usersingle_view,parent,false);
                return  new MyViewHolder(v);

            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}