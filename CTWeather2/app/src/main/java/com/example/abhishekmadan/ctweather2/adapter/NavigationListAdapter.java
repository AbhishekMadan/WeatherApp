package com.example.abhishekmadan.ctweather2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekmadan.ctweather2.R;
import com.example.abhishekmadan.ctweather2.modal.Contract;
import com.example.abhishekmadan.ctweather2.modal.DbModalCity;
import com.example.abhishekmadan.ctweather2.task.WeatherRecordProvidingTask;

import java.util.ArrayList;

/**
 * Created by abhishek.madan on 2/7/2016.
 */
public class NavigationListAdapter extends BaseAdapter
{
    private ArrayList<DbModalCity> mCityList;
    private Context mContext;
    private LayoutInflater mInflater;
    private DbModalCity city;

    public NavigationListAdapter(Context context , ArrayList<DbModalCity> list){
        mCityList = list;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return mCityList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        city = mCityList.get(position);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.navigation_view_list_single_item,parent,false);
            holder = new ViewHolder();
            holder.weatherThumbnail = (ImageView) convertView.findViewById(R.id.navigation_list_weather_thumbnail_imageview);
            holder.cityName = (TextView) convertView.findViewById(R.id.navigation_list_city_textview);
            holder.cityTemperature = (TextView) convertView.findViewById(R.id.navigation_list_city_temp_textview);
            holder.cityCode= (TextView) convertView.findViewById(R.id.navigation_list_city_code_textview);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.cityName.setText(city.getmCity()+", "+city.getmCountry());
        holder.cityCode.setText(String.valueOf(city.getmCityId()));
        WeatherRecordProvidingTask loadTask = new WeatherRecordProvidingTask(mContext, Contract.LOAD_NAV_ITEM,holder.weatherThumbnail,holder.cityTemperature);
        loadTask.execute(city.getmCityId());
        return convertView;
    }

    static class ViewHolder{
        ImageView weatherThumbnail;
        TextView cityName;
        TextView cityTemperature;
        TextView cityCode;
    }
}
