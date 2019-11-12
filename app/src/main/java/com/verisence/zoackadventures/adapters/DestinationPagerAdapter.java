package com.verisence.zoackadventures.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//import com.example.mydestination.models.Destination;
//import com.example.mydestination.ui.DestinationDetailFragment;
import com.verisence.zoackadventures.UI.DestinationFragment;
import com.verisence.zoackadventures.models.Destination;

import java.util.ArrayList;

public class DestinationPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Destination> mDestinations;

    public DestinationPagerAdapter(FragmentManager fm, ArrayList<Destination> destinations) {
        super(fm);
        mDestinations = destinations;
    }

    @Override
    public Fragment getItem(int position){
        return DestinationFragment.newInstance(mDestinations.get(position));
    }

    @Override
    public int getCount(){
        return mDestinations.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return mDestinations.get(position).getName();
    }
}