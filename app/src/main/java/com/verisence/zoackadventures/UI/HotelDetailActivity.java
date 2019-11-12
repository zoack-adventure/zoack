package com.verisence.zoackadventures.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.adapters.HotelPagerAdapter;
import com.verisence.zoackadventures.models.Hotel;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotelDetailActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    ArrayList<Hotel> mHotels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        ButterKnife.bind(this);

        mHotels = Parcels.unwrap(getIntent().getParcelableExtra("hotels"));

        int startingPosition = getIntent().getIntExtra("position", 0);

        HotelPagerAdapter adapterViewPager = new HotelPagerAdapter(getSupportFragmentManager(), mHotels);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
    }
}
