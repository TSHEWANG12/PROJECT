package com.gcit.drukcraftfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Pageone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pageone);
    }

    public void Choesham(View view) {

        Intent intent=new Intent(this,Home.class);
        startActivity(intent);
    }
}