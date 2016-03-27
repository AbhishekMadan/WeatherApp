package com.example.abhishekmadan.ctweather2.modal;

import java.io.Serializable;

/**
 * Modal class corresponding the the structure of the Database table.
 */
public class DbModalCity implements Serializable{
    private long mDbId;
    private long mCityId;
    private String mCity;
    private String mCountry;
    private double mLatitude;
    private double mLongitude;
    private int favorite;

    public DbModalCity(long mDbId, long mCityId, String mCity, String mCountry, double mLatitude, double mLongitude, int favorite) {
        this.mDbId = mDbId;
        this.mCityId = mCityId;
        this.mCity = mCity;
        this.mCountry = mCountry;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.favorite = favorite;
    }

    public DbModalCity(long mCityId, String mCity) {
        this.mCityId = mCityId;
        this.mCity = mCity;
    }

    public DbModalCity(long mCityId, String mCity, String mCountry) {
        this.mCityId = mCityId;
        this.mCity = mCity;
        this.mCountry = mCountry;
    }

    public long getmDbId() {
        return mDbId;
    }

    public long getmCityId() {
        return mCityId;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmCountry() {
        return mCountry;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public int getFavorite() {
        return favorite;
    }
}
