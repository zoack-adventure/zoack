package com.verisence.zoackadventures.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.adapters.DestinationPagerAdapter;
import com.verisence.zoackadventures.models.Destination;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DestinationDetailActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    ArrayList<Destination> mDestinations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_detail);

        ButterKnife.bind(this);

        mDestinations = Parcels.unwrap(getIntent().getParcelableExtra("destinations"));

        int startingPosition = getIntent().getIntExtra("position", 0);

//        DestinationPagerAdapter adapterViewPager = new DestinationPagerAdapter(getSupportFragmentManager(), mDestinations);
        DestinationPagerAdapter adapterViewPager = new DestinationPagerAdapter(getSupportFragmentManager(), mDestinations);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
    }
}
