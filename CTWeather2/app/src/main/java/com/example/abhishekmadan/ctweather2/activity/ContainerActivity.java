package com.example.abhishekmadan.ctweather2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.example.abhishekmadan.ctweather2.R;
import com.example.abhishekmadan.ctweather2.fragment.AddCityToListFragment;
import com.example.abhishekmadan.ctweather2.fragment.EditCityListOptionFragment;
import com.example.abhishekmadan.ctweather2.fragment.WeatherForcastDetailViewPagerFragment;
import com.example.abhishekmadan.ctweather2.modal.Contract;
import com.example.abhishekmadan.ctweather2.modal.OnlineModalCity;

import java.util.ArrayList;

/**
 * Class which acts as a Container which is used to Display different fragments
 * depending on the message received from the main Activity.
 */
public class ContainerActivity extends AppCompatActivity
{
    private Toolbar mToolBar;

    private int mPosition;

    private ArrayList<OnlineModalCity> mDataList;

    private FrameLayout mContainerFrame;

    private int mFragmentToLoad;

    WeatherForcastDetailViewPagerFragment mForcastPagerFragment;

    FragmentManager mFragmentanager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_layout);
        init();
        /*retrieving the information regarding the fragment to be displayed in the container activity.
         The bundle also contains some useful information required by the fragment*/
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mFragmentToLoad = bundle.getInt("operationToPerform");

        if(mFragmentToLoad == Contract.LOAD_FORCAST_VIEW_PAGER) {
            mPosition = bundle.getInt("position");
            mDataList = (ArrayList<OnlineModalCity>) bundle.getSerializable("dataList");
            //sending received bundler to view pager fragment
            mForcastPagerFragment = new WeatherForcastDetailViewPagerFragment();
            mForcastPagerFragment.setArguments(bundle);
            insertForcastFragment();
        }else if (mFragmentToLoad == Contract.LOAD_EDIT_CITY_WINDOW_FRAGMENT){
            insertEditOptionFragment();
        }else if(mFragmentToLoad == Contract.LOAD_ADD_CITY_WINDOW_FRAGMENT){
            insertAddOptionFragment();
        }
    }


    /**
     * Method to add a fragment which provides a UI to the user to add cities to his wish list.
     **/
   public void insertAddOptionFragment(){
        getSupportActionBar().setTitle("Add City to List");
        mFragmentanager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentanager.beginTransaction();
        AddCityToListFragment addOptionFragment = new AddCityToListFragment();
        transaction.replace(R.id.frame_container,addOptionFragment);
        transaction.commit();
    }

    /**
     * Method to add a fragment to the container activity which allows the user to delete city
     * from his wish list.
     */
   public void insertEditOptionFragment(){
        getSupportActionBar().setTitle("Edit City List");
        mFragmentanager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentanager.beginTransaction();
        EditCityListOptionFragment editFragment = new EditCityListOptionFragment();
        transaction.replace(R.id.frame_container,editFragment);
        transaction.commit();
   }

    /**
     * Method to add the weather forcast fragments which give information about the weather forcast
     * at a specific date selected by the user in the main activity.
     */
    public void insertForcastFragment(){
        mFragmentanager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentanager.beginTransaction();
        transaction.replace(R.id.frame_container,mForcastPagerFragment);
        transaction.commit();
    }

    /**
     * Method to perform initialization
     */
    public void init(){
        mToolBar = (Toolbar) findViewById(R.id.forcast_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mContainerFrame = (FrameLayout) findViewById(R.id.frame_container);
    }
}
