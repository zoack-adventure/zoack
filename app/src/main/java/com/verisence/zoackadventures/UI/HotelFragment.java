package com.verisence.zoackadventures.UI;

import android.content.Intent;
import android.net.Uri;
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
import com.verisence.zoackadventures.models.Hotel;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotelFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.hotelImageView)
    ImageView mImageLabel;
    @BindView(R.id.hotelNameTextView)
    TextView mNameLabel;
    @BindView(R.id.hotelDescTextView)
    TextView mHotelDescription;
    @BindView(R.id.phoneTextView)
    TextView mHotelPhone;
    @BindView(R.id.addressTextView)
    TextView mHotelAddress;
    @BindView(R.id.hotelPrice)
    TextView mHotelPrice;
    @BindView(R.id.bookHotel)
    Button mBookHotel;



    private Hotel mHotel;

    public static Fragment newInstance(Hotel hotel) {
        // Required empty public constructor
        HotelFragment hotelFragment = new HotelFragment();
        Bundle args = new Bundle();
        args.putParcelable("hotel", Parcels.wrap(hotel));
        hotelFragment.setArguments(args);
        return hotelFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHotel = Parcels.unwrap(getArguments().getParcelable("hotel"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotel_list, container, false);
        ButterKnife.bind(this, view);
        Picasso.get().load(mHotel.getImageUrl()).into(mImageLabel);
        mNameLabel.setText(mHotel.getName());
        mHotelPrice.setText("Kshs. "+String.valueOf(mHotel.getPrice()));
        mHotelDescription.setText(mHotel.getDescription());
        mHotelPhone.setText(mHotel.getPhone());
        mHotelAddress.setText(mHotel.getAddress());
        mBookHotel.setOnClickListener(this);

        mHotelPhone.setOnClickListener(this);
        mHotelAddress.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

//        if (v == mViewHotels) {
//            Intent i = new Intent(getActivity(), HotelsActivity.class);
//            startActivity(i);
//            ((Activity) getActivity()).overridePendingTransition(0, 0);
//        }
        if (v==mHotelAddress){
            Double lat = mHotel.getLatitude();
            Double longitude = mHotel.getLongitude();
            Uri gmmIntentUri = Uri.parse("geo:" + lat
                    + "," + longitude
                    + "?q=(" + mHotel.getName() + ")");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }

        if (v == mHotelPhone) {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + mHotel.getPhone()));
            startActivity(phoneIntent);
        }

    }
}
