package com.neversitup.currency.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.neversitup.currency.R;
import com.neversitup.currency.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    TabLayout currencyTabLayout;
    ViewPager currencyViewPager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currencyTabLayout = findViewById(R.id.currency_tab_layout);
        currencyViewPager = findViewById(R.id.currency_view_pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        currencyViewPager.setAdapter(adapter);
        currencyTabLayout.setupWithViewPager(currencyViewPager);
    }
}