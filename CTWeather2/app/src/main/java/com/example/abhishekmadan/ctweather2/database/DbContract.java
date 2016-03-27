package com.example.abhishekmadan.ctweather2.database;

import android.net.Uri;

/**
 * Class to store all the constants pertaining to the database
 */
public class DbContract {

    public final static String DATABASE_NAME = "CTWeatherDatabase";

    public final static String TABLE_NAME = "TableCity";

    public final static int DATABASE_VERSION = 4;

    public final static String COL_CID = "_id";

    public final static String COL_CITY = "city";

    public final static String COL_CITY_CODE = "code";

    public final static String COL_COUNTRY = "country";

    public final static String COL_LATITUDE = "latitude";

    public final static String COL_LONGITUDE = "longitude";

    public final static String COL_FAVORITE = "favorite";

    public static final String PROVIDER_NAME = "com.example.abhishekmadan."+DATABASE_NAME;

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/" + DbContract.TABLE_NAME);

}
