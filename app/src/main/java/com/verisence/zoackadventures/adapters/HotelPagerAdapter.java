package com.verisence.zoackadventures.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//import com.example.myhotel.models.Hotel;
//import com.example.myhotel.ui.HotelDetailFragment;
import com.verisence.zoackadventures.UI.HotelFragment;
import com.verisence.zoackadventures.models.Hotel;

import java.util.ArrayList;

public class HotelPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Hotel> mHotels;

    public HotelPagerAdapter(FragmentManager fm, ArrayList<Hotel> hotels) {
        super(fm);
        mHotels = hotels;
    }

    @Override
    public Fragment getItem(int position){
        return HotelFragment.newInstance(mHotels.get(position));
    }

    @Override
    public int getCount(){
        return mHotels.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return mHotels.get(position).getName();
    }
}