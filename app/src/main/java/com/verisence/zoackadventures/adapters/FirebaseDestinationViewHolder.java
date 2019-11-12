package com.verisence.zoackadventures.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.verisence.zoackadventures.Constants;
import com.verisence.zoackadventures.UI.DestinationDetailActivity;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.models.Destination;

import org.parceler.Parcels;

import java.util.ArrayList;

public class FirebaseDestinationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context mContext;

    public FirebaseDestinationViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindDestination(Destination destination){
        ImageView destinationImageView = mView.findViewById(R.id.destinationImageView);
        ImageView destinationImage = mView.findViewById(R.id.imageCategory);
        TextView nameTextView = (TextView) mView.findViewById(R.id.destinationNameTextView);
        TextView descriptionTextView = (TextView) mView.findViewById(R.id.destinationDescTextView);

        Picasso.get().load(destination.getImageUrl()).into(destinationImageView);
        Picasso.get().load(destination.getImage()).into(destinationImage);

        nameTextView.setText(destination.getName());
        

    }

    @Override
    public void onClick(View v) {
        final ArrayList<Destination> destinations = new ArrayList<>();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_DESTINATIONS);
        reference.keepSynced(true);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    destinations.add(snapshot.getValue(Destination.class));
                }
                int itemPosition = getLayoutPosition();
                Intent intent = new Intent(mContext, DestinationDetailActivity.class);
                intent.putExtra("position", itemPosition);
                intent.putExtra("destinations", Parcels.wrap(destinations));
                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
