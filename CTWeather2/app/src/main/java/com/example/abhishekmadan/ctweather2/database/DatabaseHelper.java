package com.example.abhishekmadan.ctweather2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper class to create the necessary tables in the Database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private String query = "CREATE TABLE " + DbContract.TABLE_NAME + " (" +
            DbContract.COL_CID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DbContract.COL_CITY + " VARCHAR(240), " +
            DbContract.COL_CITY_CODE + " INTEGER, " +
            DbContract.COL_COUNTRY + " VARCHAR(240), " +
            DbContract.COL_LATITUDE + " VARCHAR(240), " +
            DbContract.COL_LONGITUDE + " VARCHAR(240), " +
            DbContract.COL_FAVORITE + " INTEGER );";

    public DatabaseHelper(Context context) {
        super(context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.TABLE_NAME);
        onCreate(db);
    }
}
