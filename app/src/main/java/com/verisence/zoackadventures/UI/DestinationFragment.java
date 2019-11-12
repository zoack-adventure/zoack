package com.verisence.zoackadventures.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.models.Destination;

import org.parceler.Parcels;

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
            Intent intent = new Intent(getActivity(), HotelsActivity.class);
            intent.putExtra("current location",location);
            startActivity(intent);
            ((Activity) getActivity()).overridePendingTransition(0, 0);
        }

        if (v==mDestinationDescription){
            descDialog.setPositiveButton("CLOSE", (dialog, which) -> dialog.dismiss());
            descDialog.show();
        }
    }
}
