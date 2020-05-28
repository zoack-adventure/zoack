package com.verisence.zoackadventures.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.verisence.zoackadventures.Constants;
import com.verisence.zoackadventures.UI.HotelDetailActivity;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.models.Hotel;
import com.verisence.zoackadventures.zoack;

import org.parceler.Parcels;

import java.util.ArrayList;

public class FirebaseHotelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context mContext;

    String hotelLocation;

    public FirebaseHotelViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindHotel(Hotel hotel){
        ImageView hotelImageView = mView.findViewById(R.id.hotelImageView);
        ImageView hotelImage = mView.findViewById(R.id.imageHotel);
        TextView nameTextView = (TextView) mView.findViewById(R.id.hotelNameTextView);
        RatingBar rb = (RatingBar) mView.findViewById(R.id.ratingBar1);
        rb.setRating(Float.parseFloat(String.valueOf(hotel.getRating())));



        Picasso.get().load(hotel.getImageUrl()).into(hotelImageView);
        Picasso.get().load(hotel.getImageUrl()).into(hotelImage);

        nameTextView.setText(hotel.getName());


        zoack.currentLoc = hotel.getLocation();

        this.hotelLocation = zoack.currentLoc;

        Log.e("HOTELHOLDERBIND", "bindHotel: "+hotelLocation);

    }


    @Override
    public void onClick(View v) {
        final ArrayList<Hotel> hotels = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference(Constants.FIREBASE_CHILD_HOTELS);
//        final String location = "Diani";
        Log.e("HOTELHOLDERONCLICK", "bindHotel: "+hotelLocation);
        final Query hotelsByLocation = reference.orderByChild("location").equalTo(hotelLocation);
        hotelsByLocation.keepSynced(true);
        hotelsByLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    hotels.add(snapshot.getValue(Hotel.class));
                }
                int itemPosition = getLayoutPosition();
                Intent intent = new Intent(mContext, HotelDetailActivity.class);
                intent.putExtra("position", itemPosition);
                intent.putExtra("hotels", Parcels.wrap(hotels));
                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
