package com.neversitup.currency.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.neversitup.currency.fragment.ConverterFragment;
import com.neversitup.currency.fragment.HistoryFragment;
import com.neversitup.currency.fragment.HomeFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0){
            fragment = new HomeFragment();
        }else if (position == 1){
            fragment = new ConverterFragment();
        } else if (position == 2){
            fragment = new HistoryFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0){
            title = "Exchange";
        }else if (position == 1){
            title = "Converter";
        }else if (position ==2){
            title = "History";
        }
        return title;
    }
}
