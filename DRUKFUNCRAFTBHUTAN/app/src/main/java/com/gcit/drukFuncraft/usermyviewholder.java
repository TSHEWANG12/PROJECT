package com.gcit.drukFuncraft;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
 
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
public class usermyviewholder extends RecyclerView.ViewHolder{
    ImageView mImageView;
    TextView mTitle, mDescription, mCost, mContact;
    public usermyviewholder(@NonNull View itemView) {
        super(itemView);
        mImageView = (ImageView) itemView.findViewById(R.id.image);
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mDescription = (TextView) itemView.findViewById(R.id.description);
        mCost = (TextView) itemView.findViewById(R.id.cost);
        mContact = (TextView) itemView.findViewById(R.id.contact);
    }
}
