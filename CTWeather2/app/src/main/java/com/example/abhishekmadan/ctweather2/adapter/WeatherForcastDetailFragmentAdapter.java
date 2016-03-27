package com.example.abhishekmadan.ctweather2.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.abhishekmadan.ctweather2.fragment.WeatherForcastDetailFragment;
import com.example.abhishekmadan.ctweather2.modal.OnlineModalCity;

import java.util.ArrayList;

/**
 * Adapter to populate the the list of forcasted weather conditions in the second fragment
 * of the view pager visible in the main activity.
 */
public class WeatherForcastDetailFragmentAdapter extends FragmentStatePagerAdapter {

    private ArrayList<OnlineModalCity> mDataList;
    private Context mContext;
    Bundle bundle;

    public WeatherForcastDetailFragmentAdapter(FragmentManager fm, Context context, ArrayList<OnlineModalCity> dataList) {
        super(fm);
        mDataList = dataList;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        WeatherForcastDetailFragment fragment = new WeatherForcastDetailFragment();
        OnlineModalCity obj = mDataList.get(position);
        bundle = new Bundle();
        bundle.putSerializable("WeatherData", obj);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {

        return mDataList.size();
    }
}
