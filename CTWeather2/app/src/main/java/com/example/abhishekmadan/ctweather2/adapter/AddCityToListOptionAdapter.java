package com.example.abhishekmadan.ctweather2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekmadan.ctweather2.R;
import com.example.abhishekmadan.ctweather2.database.DbContract;
import com.example.abhishekmadan.ctweather2.modal.Contract;
import com.example.abhishekmadan.ctweather2.modal.DbModalCity;
import com.example.abhishekmadan.ctweather2.task.WeatherRecordProvidingTask;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Adapter to populate the list of city from which the user can choose from, to show them
 * in the navigation drawer.
 */
public class AddCityToListOptionAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private ArrayList<DbModalCity> mCityList;
    private ArrayList<DbModalCity> mCityListStorage;
    private LayoutInflater mInflater;

    public AddCityToListOptionAdapter(Context context, ArrayList<DbModalCity> list) {
        mContext = context;
        mCityList = list;
        mCityListStorage = list;
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
        DbModalCity city = mCityList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.edit_window_single_row_layout, parent, false);
            holder = new ViewHolder();
            holder.cityCode = (TextView) convertView.findViewById(R.id.city_code_textview);
            holder.cityName = (TextView) convertView.findViewById(R.id.city_name_textview);
            holder.country = (TextView) convertView.findViewById(R.id.country_textview);
            holder.swipIcon = (ImageView) convertView.findViewById(R.id.swip_imageview);
            holder.swipIcon.setVisibility(View.GONE);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.cityName.setText(city.getmCity() + ", " + city.getmCountry());
        holder.country.setText(city.getmCountry());
        holder.cityCode.setText(String.valueOf(city.getmCityId()));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }


    static class ViewHolder {
        TextView cityCode;
        TextView cityName;
        TextView country;
        ImageView swipIcon;
    }

    Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<DbModalCity> tempList = new ArrayList<DbModalCity>();
            if (constraint != null) {
                for (DbModalCity obj : mCityListStorage) {
                    if (obj.getmCity().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        tempList.add(obj);
                    }
                }
            }
            results.values = tempList;
            results.count = tempList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mCityList = (ArrayList<DbModalCity>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            }
        }
    };
}
