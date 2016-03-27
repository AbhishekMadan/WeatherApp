package com.example.abhishekmadan.ctweather2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekmadan.ctweather2.R;
import com.example.abhishekmadan.ctweather2.modal.OnlineModalCity;
import com.example.abhishekmadan.ctweather2.util.Conversion;

import java.util.ArrayList;

/**
 * Adapter to populate the list for the forcasted weather shown in the view pager on the first
 * page of the activity.
 */
public class DailyFragmentAdapter extends BaseAdapter {

    private ArrayList<OnlineModalCity> mForcastList;

    private Context mContext;

    private LayoutInflater mInflater;

    public DailyFragmentAdapter(Context context, ArrayList<OnlineModalCity> list) {
        mContext = context;
        mForcastList = list;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mForcastList.size();
    }

    @Override
    public Object getItem(int position) {
        return mForcastList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewContainer container;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.daily_single_row_layout, parent, false);
            container = new ViewContainer();
            //Visible items
            container.mForcastDate = (TextView) convertView.findViewById(R.id.date_textview);
            container.mWeatherIcon = (ImageView) convertView.findViewById(R.id.weather_imageview);
            container.mWeatherDescription = (TextView) convertView.findViewById(R.id.description_textview);
            container.mTemp = (TextView) convertView.findViewById(R.id.temperature_textview);
            //hidden items
            container.mMinTemp = (TextView) convertView.findViewById(R.id.min_temp_textview);
            container.mMaxTemp = (TextView) convertView.findViewById(R.id.max_temp_textview);
            container.mWindSpeed = (TextView) convertView.findViewById(R.id.wind_speed_textview);
            container.mWindDirection = (TextView) convertView.findViewById(R.id.wind_direction_textview);
            container.mRainVolume = (TextView) convertView.findViewById(R.id.rain_volume_textview);
            container.mSnowVolume = (TextView) convertView.findViewById(R.id.snow_volume_textview);
            convertView.setTag(container);
        }
        container = (ViewContainer) convertView.getTag();
        OnlineModalCity tempObj = mForcastList.get(position);
        container.mForcastDate.setText(Conversion.getDate(tempObj.date));
        try {
            container.mWeatherIcon.setImageResource(mContext.getResources().getIdentifier(tempObj.weatherIcon, "drawable", mContext.getPackageName()));
        } catch (Exception e) {
            Log.d("ABHISHEK", "image icon not found : " + tempObj.weatherIcon);
        }
        container.mWeatherDescription.setText(tempObj.weatherDesc);
        container.mTemp.setText(String.valueOf(tempObj.maxTemperature) + "°/" + String.valueOf(tempObj.minTemperature) + "°");

        return convertView;
    }

    static class ViewContainer {
        TextView mForcastDate;
        TextView mTemp;
        TextView mMinTemp;
        TextView mMaxTemp;
        TextView mWeatherDescription;
        ImageView mWeatherIcon;
        TextView mWindSpeed;
        TextView mWindDirection;
        TextView mCloudCover;
        TextView mRainVolume;
        TextView mSnowVolume;
    }

}
