package com.verisence.zoackadventures.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.verisence.zoackadventures.Constants;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.models.Destination;
import com.verisence.zoackadventures.models.Hotel;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DestinationFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.destinationImageView)
    ImageView mImageLabel;
    @BindView(R.id.destinationNameTextView)
    TextView mNameLabel;
    @BindView(R.id.destinationDescTextView)
    TextView mDestinationDescription;
    @BindView(R.id.viewHotels)
    Button mViewHotels;

    AlertDialog.Builder descDialog;

    private Destination mDestination;

    String location;

    public static Fragment newInstance(Destination destination) {
        // Required empty public constructor
        DestinationFragment destinationFragment = new DestinationFragment();
        Bundle args = new Bundle();
        args.putParcelable("destination", Parcels.wrap(destination));
        destinationFragment.setArguments(args);
        return destinationFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDestination = Parcels.unwrap(getArguments().getParcelable("destination"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        descDialog = new AlertDialog.Builder(getActivity());
        descDialog.setTitle("Dialog via Builder");
        descDialog.setMessage("Would you like to take a survey?");

        View view = inflater.inflate(R.layout.fragment_destination_list, container, false);
        ButterKnife.bind(this, view);
        Picasso.get().load(mDestination.getImageUrl()).into(mImageLabel);
        mNameLabel.setText(mDestination.getName());
        mDestinationDescription.setText(mDestination.getDescription());
        mViewHotels.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        if (v == mViewHotels) {
            location = mDestination.getName();
            final ArrayList<Hotel> hotels = new ArrayList<>();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference reference = database.getReference(Constants.FIREBASE_CHILD_HOTELS);
//        final String location = "Diani";

            final Query hotelsByLocation = reference.orderByChild("location").equalTo(location);
            hotelsByLocation.keepSynced(true);
            hotelsByLocation.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        hotels.add(snapshot.getValue(Hotel.class));
                    }
                    int itemPosition = 0;
                    Intent intent = new Intent(getContext(), HotelDetailActivity.class);
                    intent.putExtra("position", itemPosition);
                    intent.putExtra("hotels", Parcels.wrap(hotels));
                    startActivity(intent);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if (v==mDestinationDescription){
            descDialog.setPositiveButton("CLOSE", (dialog, which) -> dialog.dismiss());
            descDialog.show();
        }
    }
}
