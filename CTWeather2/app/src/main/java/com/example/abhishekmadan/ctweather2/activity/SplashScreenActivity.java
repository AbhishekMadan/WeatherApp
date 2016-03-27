package com.example.abhishekmadan.ctweather2.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.abhishekmadan.ctweather2.R;
import com.example.abhishekmadan.ctweather2.database.DbContract;
import com.example.abhishekmadan.ctweather2.modal.Contract;


/**
 * Activity to create the splash screen visible to the user.
 * This activity checks if the internet connection is available and if not, it
 * does not allows the user to navigate to the main application.
 * Also if the user if opening the application for the first time then the
 * application adds a favorite city for the user by default.
 */
public class SplashScreenActivity extends Activity{

    private Handler mHandler = new Handler();
    private ProgressBar mBar;
    private LinearLayout mWelcomeLabel;
    private TextView mErrorNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.splash_screen_layout);
       mBar = (ProgressBar) findViewById(R.id.progressBar);
       mWelcomeLabel = (LinearLayout) findViewById(R.id.linearlayout);
       mErrorNotification = (TextView) findViewById(R.id.error_notification);

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkDbStatus();
        mErrorNotification.setVisibility(View.GONE);
        mBar.setVisibility(View.VISIBLE);
        if(checkDbStatus()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkNetworkStatus();
                }
            }, 4000);

        }
    }

    /**
     * Method to check if the application Database has a default city. If not, then the
     * application inserts a default city.
     * @return
     */
    public boolean checkDbStatus(){
        Cursor cursor = getContentResolver().query(DbContract.CONTENT_URI,null,DbContract.COL_FAVORITE+"=?",new String[] {
            String.valueOf(Contract.FAVORITE_CITY)},null);
        if(cursor.getCount()==0){
            ContentValues values = new ContentValues();
            values.put(DbContract.COL_CITY_CODE,"1262180");
            values.put(DbContract.COL_COUNTRY,"IN");
            values.put(DbContract.COL_CITY,"Nagpur");
            values.put(DbContract.COL_FAVORITE,Contract.FAVORITE_CITY);
            Uri uri =getContentResolver().insert(DbContract.CONTENT_URI,values);

            if(Integer.parseInt(uri.getLastPathSegment())!=0){
                cursor.close();
                return true;
            }

        }
        cursor.close();
        return true;
    }

    /**
     * Method to check if internet connection is available.
     */
    public void checkNetworkStatus(){

        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();

        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            mWelcomeLabel.setVisibility(View.GONE);
            mErrorNotification.setVisibility(View.VISIBLE);
        }
    }
}
