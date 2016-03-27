package com.example.abhishekmadan.ctweather2.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishekmadan.ctweather2.R;
import com.example.abhishekmadan.ctweather2.database.DbContract;
import com.example.abhishekmadan.ctweather2.modal.Contract;

/**
 * Adapter to populate the list visible in the 'Edit City List' window visible to the
 * user from the navigation drawer.
 */

public class EditCityListOptionRecyclerCursorAdapter extends RecyclerView.Adapter {

    private Cursor mCityCursor;

    private Context mContext;

    private LayoutInflater inflater;

    public EditCityListOptionRecyclerCursorAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCityCursor = cursor;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = inflater.inflate(R.layout.edit_window_single_row_layout, viewGroup, false);
        Holder viewHolder = new Holder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i)
    {
        Holder rowHolder = (Holder) viewHolder;
        mCityCursor.moveToPosition(i);
        rowHolder.bindData(mCityCursor);
    }

    @Override
    public int getItemCount() {

        if (!mCityCursor.isClosed() && mCityCursor != null) {
            return mCityCursor.getCount();
        } else {
            return 0;
        }
    }

    /**
     * Method to remove the specific item which the user swipes from the Recycler view.
     * @param position of the item to be deleted from the list.
     */
    public void removeItem(int position)
    {
        mCityCursor.moveToFirst();
        if (mCityCursor.getCount() > 1) {
            mCityCursor.moveToPosition(position);
            if (mCityCursor.getInt(mCityCursor.getColumnIndex(DbContract.COL_FAVORITE)) != Contract.FAVORITE_CITY) {
                Long id = mCityCursor.getLong(mCityCursor.getColumnIndex(DbContract.COL_CITY_CODE));
                mContext.getContentResolver().delete(DbContract.CONTENT_URI, DbContract.COL_CITY_CODE + "=?", new String[]{String.valueOf(id)});

            } else if (mCityCursor.getInt(mCityCursor.getColumnIndex(DbContract.COL_FAVORITE)) == Contract.FAVORITE_CITY) {
                Long id = mCityCursor.getLong(mCityCursor.getColumnIndex(DbContract.COL_CITY_CODE));
                mContext.getContentResolver().delete(DbContract.CONTENT_URI, DbContract.COL_CITY_CODE + "=?", new String[]{String.valueOf(id)});
                Cursor maxIdCursor = mContext.getContentResolver().query(DbContract.CONTENT_URI, new String[]{"MAX(" + DbContract.COL_CID + ")"}, null, null, null);
                maxIdCursor.moveToNext();
                String maxId = maxIdCursor.getString(0);
                ContentValues values = new ContentValues();
                values.put(DbContract.COL_FAVORITE, Contract.FAVORITE_CITY);
                int count = mContext.getContentResolver().update(DbContract.CONTENT_URI, values, DbContract.COL_CID + "=?", new String[]{maxId});
            }
            getNewCursorAndReshiftList(position);
            Toast.makeText(mContext, "City Record Deleted Successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Cannot Delete this City!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to get new data from the Database after the user deletes any city.
     * @param position
     */
    public void getNewCursorAndReshiftList(int position)
    {
        String[] projection = {DbContract.COL_CITY_CODE,
                DbContract.COL_CITY,
                DbContract.COL_FAVORITE};
        mCityCursor = mContext.getContentResolver().query(DbContract.CONTENT_URI, projection, null, null, DbContract.COL_CID + " ASC");
        notifyDataSetChanged();
    }

    static class Holder extends RecyclerView.ViewHolder
    {
        TextView cityCode;

        TextView cityName;

        public Holder(View itemView) {
            super(itemView);
            cityCode = (TextView) itemView.findViewById(R.id.city_code_textview);
            cityName = (TextView) itemView.findViewById(R.id.city_name_textview);
        }

        public void bindData(Cursor cursor)
        {
            cityCode.setText(cursor.getString(cursor.getColumnIndex(DbContract.COL_CITY_CODE)));
            cityName.setText(cursor.getString(cursor.getColumnIndex(DbContract.COL_CITY)));
        }
    }

}
