package com.example.abhishekmadan.ctweather2.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.example.abhishekmadan.ctweather2.R;
import com.example.abhishekmadan.ctweather2.fragment.CurrentWeatherFragment;
import com.example.abhishekmadan.ctweather2.fragment.DailyWeatherFragment;
import com.example.abhishekmadan.ctweather2.modal.DbModalCity;

import java.util.ArrayList;

/**
 * Adapter to populate the view pager visible on the Main Activity screen.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    private String[] mTabList;

    private Context mContext;

    private DbModalCity mFavCity;

    Bundle mBundle;

    public PagerAdapter(FragmentManager fm, Context context, DbModalCity mCity) {
        super(fm);
        mFavCity = mCity;
        mContext = context;
        mTabList = mContext.getResources().getStringArray(R.array.tab_list);
    }

    public void setNewData(DbModalCity city) {
        mFavCity = city;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CurrentWeatherFragment tab1 = new CurrentWeatherFragment();
                mBundle = new Bundle();
                mBundle.putSerializable("city", mFavCity);
                tab1.setArguments(mBundle);
                return tab1;
            case 1:
                DailyWeatherFragment tab2 = new DailyWeatherFragment();
                mBundle = new Bundle();
                mBundle.putSerializable("city", mFavCity);
                tab2.setArguments(mBundle);
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabList.length;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabList[position];
    }
}
