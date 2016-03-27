package com.example.abhishekmadan.ctweather2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekmadan.ctweather2.R;
import com.example.abhishekmadan.ctweather2.database.DbContract;
import com.example.abhishekmadan.ctweather2.modal.Contract;
import com.example.abhishekmadan.ctweather2.task.WeatherRecordProvidingTask;


/**
 * Adapter to populate the Navigation drawer with the list of preselected cities.
 */
public class NavigationListCursorAdapter extends CursorAdapter {

    private Context mContext;

    private LayoutInflater mInflater;

    private Cursor mCursor;

    public NavigationListCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        ViewHolder holder;
        View view = mInflater.inflate(R.layout.navigation_view_list_single_item, parent, false);
        holder = new ViewHolder();
        holder.weatherThumbnail = (ImageView) view.findViewById(R.id.navigation_list_weather_thumbnail_imageview);
        holder.cityName = (TextView) view.findViewById(R.id.navigation_list_city_textview);
        holder.cityTemperature = (TextView) view.findViewById(R.id.navigation_list_city_temp_textview);
        holder.cityCode = (TextView) view.findViewById(R.id.navigation_list_city_code_textview);

        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ViewHolder holder;
        holder = (ViewHolder) view.getTag();
        holder.cityName.setText(cursor.getString(cursor.getColumnIndex(DbContract.COL_CITY)) + ", " + cursor.getString(cursor.getColumnIndex(DbContract.COL_COUNTRY)));
        holder.cityCode.setText(String.valueOf(cursor.getLong(cursor.getColumnIndex(DbContract.COL_CITY_CODE))));
        if(cursor.getInt(cursor.getColumnIndex(DbContract.COL_FAVORITE))==Contract.FAVORITE_CITY){

        }else
        {

        }
        WeatherRecordProvidingTask loadTask = new WeatherRecordProvidingTask(mContext, Contract.LOAD_NAV_ITEM, holder.weatherThumbnail, holder.cityTemperature);
        loadTask.execute(cursor.getLong(cursor.getColumnIndex(DbContract.COL_CITY_CODE)));
    }

    static class ViewHolder {
        ImageView weatherThumbnail;
        TextView cityName;
        TextView cityTemperature;
        TextView cityCode;

    }
}
