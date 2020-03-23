package com.verisence.zoackadventures.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.verisence.zoackadventures.models.Hotel;
import com.verisence.zoackadventures.models.Payment;

import org.parceler.Parcels;

import java.util.ArrayList;

public class FirebasePaymentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context mContext;


    public FirebasePaymentViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }
    public void bindTransaction(Payment payment){
        ImageView hotelImageView = mView.findViewById(R.id.hotelImageView);
        ImageView hotelImage = mView.findViewById(R.id.imageHotel);
        TextView nameTextView = (TextView) mView.findViewById(R.id.hotelNameTextView);




        Picasso.get().load(payment.getHotel().getImageUrl()).into(hotelImageView);
        Picasso.get().load(payment.getHotel().getImageUrl()).into(hotelImage);

        nameTextView.setText(payment.getHotel().getName());



    }


    @Override
    public void onClick(View v) {
        final ArrayList<Payment> payments = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        final DatabaseReference reference = database.getReference(Constants.FIREBASE_CHILD_TRANSACTIONS).child(uid);
//        final String location = "Diani";

        final Query paymentQuery = reference;
        paymentQuery.keepSynced(true);
        paymentQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    payments.add(snapshot.getValue(Payment.class));
                }
                int itemPosition = getLayoutPosition();
                Intent intent = new Intent(mContext, HotelDetailActivity.class);
                intent.putExtra("position", itemPosition);
//                intent.putExtra("hotels", Parcels.wrap(hotels));
                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
