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
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.UI.HotelDetailActivity;
import com.verisence.zoackadventures.UI.HotelFragment;
import com.verisence.zoackadventures.models.Hotel;
import com.verisence.zoackadventures.zoack;

import org.parceler.Parcels;

import java.util.ArrayList;

public class FirebaseHotelFavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context mContext;

    String hotelLocation;
    Hotel selectedHotel;

    public FirebaseHotelFavoritesViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindHotel(Hotel hotel){
        selectedHotel = hotel;
        ImageView hotelImageView = mView.findViewById(R.id.destinationImageView);

        TextView nameTextView = (TextView) mView.findViewById(R.id.destinationNameTextView);
        RatingBar rb = (RatingBar) mView.findViewById(R.id.ratingBar1);
        rb.setVisibility(View.VISIBLE);
        rb.setRating(Float.parseFloat(String.valueOf(hotel.getRating())));



        Picasso.get().load(hotel.getImageUrl()).into(hotelImageView);


        nameTextView.setText(hotel.getName());


        zoack.currentLoc = hotel.getLocation();

        this.hotelLocation = zoack.currentLoc;

        Log.e("HOTELHOLDERBIND", "bindHotel: "+hotelLocation);

    }


    @Override
    public void onClick(View v) {

        final ArrayList<Hotel> hotels = new ArrayList<>();
        hotels.add(selectedHotel);
        Intent intent = new Intent(mContext, HotelDetailActivity.class);
        intent.putExtra("position", 0);
        intent.putExtra("isFavorite", true);
        intent.putExtra("hotels", Parcels.wrap(hotels));


        mContext.startActivity(intent);

    }
}
