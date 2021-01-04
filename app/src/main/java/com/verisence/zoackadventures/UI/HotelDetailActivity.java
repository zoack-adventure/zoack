package com.verisence.zoackadventures.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.adapters.HotelPagerAdapter;
import com.verisence.zoackadventures.models.Hotel;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotelDetailActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    ArrayList<Hotel> mHotels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.WHITE);
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        final ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ViewPager viewPager=findViewById(R.id.viewPager);

        if(viewPager!=null)
        {
            mHotels = Parcels.unwrap(getIntent().getParcelableExtra("hotels"));

            int startingPosition = getIntent().getIntExtra("position", 0);

            HotelPagerAdapter adapterViewPager = new HotelPagerAdapter(getSupportFragmentManager(), mHotels);
            mViewPager.setAdapter(adapterViewPager);
            mViewPager.setCurrentItem(startingPosition);
        }

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }
}
