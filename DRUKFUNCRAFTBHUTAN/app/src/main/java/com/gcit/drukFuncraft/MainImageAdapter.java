package com.gcit.drukFuncraft;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class MainImageAdapter extends FirebaseRecyclerAdapter<PhotoUploadHelperClass,MainImageAdapter.myviewholder> {
    private Context mContext;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainImageAdapter(@NonNull FirebaseRecyclerOptions<PhotoUploadHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull PhotoUploadHelperClass model) {

        holder.mName.setText(model.getTitle());
        holder.mDescription.setText(model.getShopName());
        holder.mCost.setText("Price "+model.getCost());
        holder.mPhoneNo.setText(model.getContact());
        holder.mPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = "tel:" + model.getContact();
                i.setData(Uri.parse(p));
                holder.mPhoneNo.getContext().startActivity(i);
            }
        });
        Glide.with(holder.mImageView.getContext()).load(model.getPhotoUri()).into(holder.mImageView);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mName, mDescription, mCost, mPhoneNo;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.image);
            mName = (TextView)itemView.findViewById(R.id.name);
            mDescription = (TextView)itemView.findViewById(R.id.description);
            mCost = (TextView)itemView.findViewById(R.id.cost);
            mPhoneNo = (TextView) itemView.findViewById(R.id.contact);
        }
    }
}
