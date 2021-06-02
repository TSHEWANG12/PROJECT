package com.gcit.drukFuncraft;

import android.content.Context;
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

public class viewImageAdapter extends FirebaseRecyclerAdapter<PhotoUploadHelperClass,viewImageAdapter.myviewholder> {
    private Context mContext;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public viewImageAdapter(@NonNull FirebaseRecyclerOptions<PhotoUploadHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull PhotoUploadHelperClass model) {

        holder.mShopName.setText(model.getShopName());
        holder.mAddress.setText(model.getTitle());
        holder.mPhoneNo.setText(model.getContact());
        Glide.with(holder.mImageView.getContext()).load(model.getPhotoUri()).into(holder.mImageView);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_item,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mShopName, mAddress, mPhoneNo;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.image);
            mShopName = (TextView)itemView.findViewById(R.id.shopname);
            mAddress = (TextView) itemView.findViewById(R.id.title);
            mPhoneNo = (TextView) itemView.findViewById(R.id.contact);
        }
    }
}

