package com.verisence.zoackadventures.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.verisence.zoackadventures.UI.HotelFragment;
import com.verisence.zoackadventures.UI.PaymentFragment;
import com.verisence.zoackadventures.models.Hotel;
import com.verisence.zoackadventures.models.Payment;

import java.util.ArrayList;

public class PaymentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Payment> mPayments;

    public PaymentPagerAdapter(FragmentManager fm, ArrayList<Payment> payments) {
        super(fm);
        mPayments = payments;
    }

    @Override
    public Fragment getItem(int position){
        return PaymentFragment.newInstance(mPayments.get(position));
    }

    @Override
    public int getCount(){
        return mPayments.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return mPayments.get(position).getHotel().getName();
    }
}
