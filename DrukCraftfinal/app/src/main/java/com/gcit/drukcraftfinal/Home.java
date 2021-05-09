package com.gcit.drukcraftfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.sql.Ref;

public class Home extends AppCompatActivity {
    EditText inputSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingbtn;
    FirebaseRecyclerOptions<Craft> options;
    FirebaseRecyclerAdapter<Craft,MyViewHolder>adapter;
    DatabaseReference DataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
}