package com.gcit.drukFuncraft;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class LicenseAdapter extends FirebaseRecyclerAdapter<LicenseNumberHelperClass,LicenseAdapter.viewholder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public LicenseAdapter(@NonNull FirebaseRecyclerOptions<LicenseNumberHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewholder holder, int position, @NonNull LicenseNumberHelperClass model) {
        holder.license.setText(model.getLicenseNo());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.delete.getContext());
                builder.setTitle("Delete");
                builder.setMessage("Are sure you want to delete?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("LicenseNo").child(getRef(position).getKey()).removeValue();
                        Intent intent = new Intent(holder.license.getContext(),AdminDashboardActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        holder.license.getContext().startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_license_view,parent,false);
        return new viewholder(v);
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView license;
        Button delete;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            license = itemView.findViewById(R.id.licenseNo);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
