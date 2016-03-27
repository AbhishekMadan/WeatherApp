package com.example.abhishekmadan.ctweather2.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.abhishekmadan.ctweather2.R;
import com.example.abhishekmadan.ctweather2.adapter.WeatherForcastDetailFragmentAdapter;
import com.example.abhishekmadan.ctweather2.modal.OnlineModalCity;

import java.util.ArrayList;

/**
 * Created by abhishek.madan on 2/8/2016.
 */
public class WeatherForcastDetailViewPagerFragment extends Fragment implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private Bundle bundle;
    private int mPosition;
    private ArrayList<OnlineModalCity> mDataList;
    private ViewPager mForcastViewPager;
    private WeatherForcastDetailFragmentAdapter mPagerAdapter;
    private ImageView mLeftShiftButton;
    private ImageView mRightShiftButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        bundle = getArguments();
        mPosition = bundle.getInt("position");
        mDataList = (ArrayList<OnlineModalCity>) bundle.getSerializable("dataList");
        View view = inflater.inflate(R.layout.weather_forcast_detail_view_pager_layout,container,false);
        mLeftShiftButton = (ImageView) view.findViewById(R.id.left_shift_button);
        mLeftShiftButton.setOnClickListener(this);
        mRightShiftButton = (ImageView) view.findViewById(R.id.right_shift_button);
        mRightShiftButton.setOnClickListener(this);
        mForcastViewPager = (ViewPager) view.findViewById(R.id.forcast_view_pager);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPagerAdapter = new WeatherForcastDetailFragmentAdapter(getActivity().getSupportFragmentManager(),getActivity(),mDataList);
        mForcastViewPager.setAdapter(mPagerAdapter);
        mForcastViewPager.setCurrentItem(mPosition,false);
        mForcastViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = mForcastViewPager.getCurrentItem();
        int totalPages = mPagerAdapter.getCount();
        switch(v.getId()){
            case R.id.left_shift_button:
                if(position>0){
                    mForcastViewPager.setCurrentItem(position-1,true);
                }
                break;
            case R.id.right_shift_button:
                if(position<totalPages-1){
                    mForcastViewPager.setCurrentItem(position+1,true);
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int totalPages = mPagerAdapter.getCount();
        if(position==0){
            mLeftShiftButton.setVisibility(View.GONE);
        }
        if(position==1){
            mLeftShiftButton.setVisibility(View.VISIBLE);
        }
        if(position==totalPages-1){
            mRightShiftButton.setVisibility(View.GONE);
        }if(position==totalPages-2){
            mRightShiftButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
