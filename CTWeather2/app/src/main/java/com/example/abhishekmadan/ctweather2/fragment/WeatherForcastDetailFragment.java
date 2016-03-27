package com.example.abhishekmadan.ctweather2.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekmadan.ctweather2.R;
import com.example.abhishekmadan.ctweather2.modal.OnlineModalCity;
import com.example.abhishekmadan.ctweather2.util.Conversion;

import org.w3c.dom.Text;

/**
 * fragment which is used to display the complete forcast details of the city
 * selected from the list on the main activity.
 */
public class WeatherForcastDetailFragment extends Fragment {

    private TextView mDateTextView;
    private TextView mTemperatureTextView;
    private ImageView mWeatherThumbnailImageView;
    private TextView mWeatherDescriptionTextView;
    private TextView mMinimumTempTextView;
    private TextView mMaximumTempTextView;
    private TextView mWindSpeedTextView;
    private TextView mWindDirectionTextView;
    private TextView mRainVolumeTextView;
    private TextView mSnowVolumeTextView;
    private TextView mHumidityTextView;
    private TextView mPressureTextView;
    private Bundle mBundle;
    private OnlineModalCity mWeatherData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.complete_summary_layout,container,false);

        mDateTextView = (TextView) view.findViewById(R.id.date_textview);
        mWeatherThumbnailImageView = (ImageView) view.findViewById(R.id.weather_thumbnail_imageview);
        mTemperatureTextView = (TextView) view.findViewById(R.id.temperature_textview);
        mWeatherDescriptionTextView = (TextView) view.findViewById(R.id.weather_desecription_textview);

        mMinimumTempTextView = (TextView) view.findViewById(R.id.min_temp_textview);
        mMaximumTempTextView = (TextView) view.findViewById(R.id.max_temp_textview);
        mWindSpeedTextView = (TextView) view.findViewById(R.id.wind_speed_textview);
        mWindDirectionTextView = (TextView) view.findViewById(R.id.wind_direction_textview);
        mRainVolumeTextView = (TextView) view.findViewById(R.id.rain_volume_textview);
        mSnowVolumeTextView = (TextView) view.findViewById(R.id.snow_volume_textview);
        mHumidityTextView = (TextView) view.findViewById(R.id.humidity_textview);
        mPressureTextView = (TextView) view.findViewById(R.id.pressure_textview);

        mBundle = getArguments();
        if(mBundle!=null){
            mWeatherData = (OnlineModalCity) mBundle.get("WeatherData");
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mWeatherData!=null) {
            mDateTextView.setText(Conversion.getDate(mWeatherData.date));
            try {
                mWeatherThumbnailImageView.setImageResource(getActivity().getResources().getIdentifier(mWeatherData.weatherIcon, "drawable", getActivity().getPackageName()));
            }catch (Exception e){
                Log.d("HANDLED EXPT",e.toString());
            }
            mTemperatureTextView.setText(mWeatherData.mainTemperature + "° C");
            mWeatherDescriptionTextView.setText(mWeatherData.weatherDesc);
            mMaximumTempTextView.setText(mWeatherData.maxTemperature + "° C");
            mMinimumTempTextView.setText(mWeatherData.minTemperature + "° C");

            mHumidityTextView.setText(String.valueOf(mWeatherData.mainHumidity)+"%");
            mPressureTextView.setText(String.valueOf(mWeatherData.mainSeaLevelPressure)+"hPa");

            mWindSpeedTextView.setText(String.valueOf(mWeatherData.windSpeed));
            mWindDirectionTextView.setText(String.valueOf(mWeatherData.windDegree)+"°");
            try {
                mSnowVolumeTextView.setText(String.valueOf(mWeatherData.snowVolume)+" mm");
            }catch (Exception e){
                Log.d("HANDLED EXPT",e.toString());
            }
            try {
                mRainVolumeTextView.setText(String.valueOf(mWeatherData.rainVolume)+" mm");
            }catch (Exception e){
                Log.d("HANDLED EXPT",e.toString());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
