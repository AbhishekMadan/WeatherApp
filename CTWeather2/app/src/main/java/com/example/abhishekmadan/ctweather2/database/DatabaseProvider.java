package com.example.abhishekmadan.ctweather2.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * DbProvider class to perform the CRUD operation required in the application
 */
public class DatabaseProvider extends ContentProvider {


    static UriMatcher mUriMatcher;
    public static final int SINGLE = 1;
    public static final int MANY = 2;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(DbContract.PROVIDER_NAME, DbContract.TABLE_NAME, MANY);
        mUriMatcher.addURI(DbContract.PROVIDER_NAME, DbContract.TABLE_NAME + "/#", SINGLE);
    }

    SQLiteDatabase mSQLiteDatabase;

    @Override
    public boolean onCreate() {
        mSQLiteDatabase = new DatabaseHelper(getContext()).getWritableDatabase();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;
        switch (mUriMatcher.match(uri)) {
            case SINGLE:
                String[] args = {uri.getPathSegments().get(1)};
                cursor = mSQLiteDatabase.query(true, DbContract.TABLE_NAME, projection, selection, selectionArgs, null, null, null, null);
                break;
            case MANY:
                cursor = mSQLiteDatabase.query(true, DbContract.TABLE_NAME, projection, selection, selectionArgs, null, null, null, null);
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Long id = mSQLiteDatabase.insert(DbContract.TABLE_NAME, null, values);
        Uri _uri = ContentUris.withAppendedId(uri, id);
        return _uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (mUriMatcher.match(uri)) {
            case SINGLE:
                count = mSQLiteDatabase.delete(DbContract.TABLE_NAME, DbContract.COL_CID + " =? ", selectionArgs);
                break;
            case MANY:
                count = mSQLiteDatabase.delete(DbContract.TABLE_NAME, selection, selectionArgs);
                break;
        }
        if (count > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (mUriMatcher.match(uri)) {
            case SINGLE:
                count = mSQLiteDatabase.update(DbContract.TABLE_NAME, values, DbContract.COL_CID + "=?", selectionArgs);
                break;
            case MANY:
                count = mSQLiteDatabase.update(DbContract.TABLE_NAME, values, selection, selectionArgs);
                break;
        }
        return count;
    }

    /**
     * Method used to get the obtain the max id of the cities stored in the db, and assign it as a favorite city
     * in case the used deletes the favorite city.
     * @param uri
     * @return
     */
    public long getMaxId(Uri uri) {
        Cursor cursor = null;
        cursor = mSQLiteDatabase.rawQuery("SELECT MAX(" + DbContract.COL_CID + ") FROM " + DbContract.TABLE_NAME, null);
        long maxId = cursor.getLong(0);
        cursor.close();
        return maxId;
    }
}
