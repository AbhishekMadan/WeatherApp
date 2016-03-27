package com.example.abhishekmadan.ctweather2.activity;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishekmadan.ctweather2.R;
import com.example.abhishekmadan.ctweather2.adapter.NavigationListAdapter;
import com.example.abhishekmadan.ctweather2.adapter.NavigationListCursorAdapter;
import com.example.abhishekmadan.ctweather2.adapter.PagerAdapter;
import com.example.abhishekmadan.ctweather2.database.DbContract;
import com.example.abhishekmadan.ctweather2.modal.Contract;
import com.example.abhishekmadan.ctweather2.modal.DbModalCity;

import java.util.ArrayList;

/**
 * Main Activity class which is display to the use as the first screen of the activity
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private DrawerLayout mNavDrawer;

    private Toolbar mToolbar;

    private TabLayout mTitleTab;

    private ViewPager mViewPager;

    private NavigationView mNavView;

    private DbModalCity mFavCity;

    private PagerAdapter adapter;

    private ListView mCityListView;

    private ArrayList<DbModalCity> mDbModalCityList;

    private NavigationListAdapter mNavListAdapter;

    private FragmentManager mFragmentManager;

    private LinearLayout mEditNavList;

    private LinearLayout mAddToNavList;

    private Cursor mDbCursorCityList;

    private NavigationListCursorAdapter mNavListCursroAdapter;

    private Handler mHandler = new Handler();

    private Intent mIntent;

    private MenuItem mMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFragmentManager = getSupportFragmentManager();
        //Obtaining cursor from db and also the city selected as favorite
        createCityListAndFavorite();
        //initialize variables
        init();
        //populate the navigation drawer list
        populateNavList();
        mCityListView.setOnItemClickListener(this);
    }

    /**
     * On click callback method getting called when the user clicks on on of the row in the forcast
     * fragment of the view pager
     *
     * @param parent
     * @param view     the user clicks
     * @param position of the view being clicked
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        PagerAdapter currentPagerAdapter = (PagerAdapter) mViewPager.getAdapter();
        TextView cityName = (TextView) view.findViewById(R.id.navigation_list_city_textview);
        TextView cityCode = (TextView) view.findViewById(R.id.navigation_list_city_code_textview);
        mFavCity = new DbModalCity(Long.parseLong(cityCode.getText().toString()), cityName.getText().toString());
        currentPagerAdapter.setNewData(mFavCity);
        currentPagerAdapter.notifyDataSetChanged();
        mNavDrawer.closeDrawers();
    }


    public void setPagerAdapter(DbModalCity obj) {
        adapter = new PagerAdapter(getSupportFragmentManager(), this, obj);
    }

    /**
     * Method to perform preliminary initialization
     */
    public void init() {

        mEditNavList = (LinearLayout) findViewById(R.id.edit_location_option);
        mEditNavList.setOnClickListener(this);
        mAddToNavList = (LinearLayout) findViewById(R.id.add_location_option);
        mAddToNavList.setOnClickListener(this);
        mNavDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mTitleTab = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        setPagerAdapter(mFavCity);
        mViewPager.setAdapter(adapter);
        mTitleTab.setupWithViewPager(mViewPager);
        mNavView = (NavigationView) findViewById(R.id.navigation_view);
        ActionBarDrawerToggle toggler = new ActionBarDrawerToggle(this, mNavDrawer, mToolbar, R.string.close, R.string.open) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                createCityListAndFavorite();
                mNavListCursroAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mNavDrawer.setDrawerListener(toggler);
        toggler.syncState();
        mCityListView = (ListView) findViewById(R.id.city_listview);
    }

    /**
     * Method to obtain the favorite city and the list of saved cities from the Database
     */
    public void createCityListAndFavorite() {
        String[] favCitySelectionArgs = {String.valueOf(Contract.FAVORITE_CITY)};
        mDbCursorCityList = getContentResolver().query(DbContract.CONTENT_URI, null, null, null, null);
        Cursor favCityCursor = getContentResolver().query(DbContract.CONTENT_URI, null, DbContract.COL_FAVORITE + "=?", favCitySelectionArgs, null);
        favCityCursor.moveToFirst();
        mFavCity = new DbModalCity(favCityCursor.getLong(favCityCursor.getColumnIndex(DbContract.COL_CID)),
                favCityCursor.getLong(favCityCursor.getColumnIndex(DbContract.COL_CITY_CODE)),
                favCityCursor.getString(favCityCursor.getColumnIndex(DbContract.COL_CITY)),
                favCityCursor.getString(favCityCursor.getColumnIndex(DbContract.COL_COUNTRY)),
                favCityCursor.getDouble(favCityCursor.getColumnIndex(DbContract.COL_LATITUDE)),
                favCityCursor.getDouble(favCityCursor.getColumnIndex(DbContract.COL_COUNTRY)),
                favCityCursor.getInt(favCityCursor.getColumnIndex(DbContract.COL_FAVORITE)));
        favCityCursor.close();
    }

    /**
     * Method to populate the navigation list, visible in the navigation drawer with the list of
     * cities preseleted by the user.
     */
    public void populateNavList() {
        mNavListCursroAdapter = new NavigationListCursorAdapter(this, mDbCursorCityList, 0);
        mCityListView.setAdapter(mNavListCursroAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();

                if (activeNetInfo != null && activeNetInfo.isConnected()) {
                    mMenuItem = item;
                    mMenuItem.setActionView(R.layout.progress_bar_menu_layout);
                    mMenuItem.expandActionView();
                    PagerAdapter currentPagerAdapter = (PagerAdapter) mViewPager.getAdapter();
                    currentPagerAdapter.notifyDataSetChanged();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mMenuItem.collapseActionView();
                            mMenuItem.setActionView(null);
                        }
                    }, 2500);
                }else{
                    Toast.makeText(MainActivity.this,"No network connection",Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return true;
    }

    /**
     * Method gets called when the user either selects 'Add City' or 'Edit City'
     * from the navigation drawer.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Bundle bundle;
        switch (v.getId()) {
            case R.id.edit_location_option:
                mIntent = new Intent(this, ContainerActivity.class);
                bundle = new Bundle();
                bundle.putInt("operationToPerform", Contract.LOAD_EDIT_CITY_WINDOW_FRAGMENT);
                mIntent.putExtras(bundle);
                startActivity(mIntent);
                break;
            case R.id.add_location_option:
                mIntent = new Intent(this, ContainerActivity.class);
                bundle = new Bundle();
                bundle.putInt("operationToPerform", Contract.LOAD_ADD_CITY_WINDOW_FRAGMENT);
                mIntent.putExtras(bundle);
                startActivity(mIntent);
                break;
            default:
                ;
        }
    }
}
