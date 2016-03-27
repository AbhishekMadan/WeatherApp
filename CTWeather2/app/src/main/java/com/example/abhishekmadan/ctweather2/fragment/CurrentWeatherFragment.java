package com.example.abhishekmadan.ctweather2.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekmadan.ctweather2.R;
import com.example.abhishekmadan.ctweather2.modal.Contract;
import com.example.abhishekmadan.ctweather2.modal.DbModalCity;
import com.example.abhishekmadan.ctweather2.task.WeatherRecordProvidingTask;

import java.util.ArrayList;

/**
 * Fragment which Current city selected in the navigation list.
 * This fragment appears as the main fragment of the activity
 */
public class CurrentWeatherFragment extends Fragment implements View.OnClickListener {

    private DbModalCity mFavCity;

    private CardView mCurrentDataCard;

    private CardView mLookingAheadDataCard;

    private TextView mCityNameTextView;

    private TextView mWeatherStatusNowTextView;

    private TextView mTemperatureNowTextView;

    private ImageView mWeatherStatusImageView;

    private TextView mCloudCoverTextView;

    private TextView mWindDirectionTextView;

    private TextView mWindGustTextView;

    private TextView mMoreDetailTextView;

    private TextView mLongitudeTextView;

    private TextView mLatitudeTextView;

    private TextView mPressureTextView;

    private TextView mHumidityTextView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mFavCity = (DbModalCity) bundle.getSerializable("city");
        }
        View view = inflater.inflate(R.layout.fragment_now_layout, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mCurrentDataCard = (CardView) getActivity().findViewById(R.id.current_cardview);
        mCurrentDataCard.setOnClickListener(this);
        mLookingAheadDataCard = (CardView) getActivity().findViewById(R.id.looking_ahead_card_view);
        mLookingAheadDataCard.setVisibility(View.GONE);
        mCityNameTextView = (TextView) getActivity().findViewById(R.id.city_name_placeholder);
        mWeatherStatusImageView = (ImageView) getActivity().findViewById(R.id.weather_status_now_image);
        mTemperatureNowTextView = (TextView) getActivity().findViewById(R.id.temp_now);
        mWeatherStatusNowTextView = (TextView) getActivity().findViewById(R.id.weather_status_now);
        mCloudCoverTextView = (TextView) getActivity().findViewById(R.id.cloud_cover);
        mWindDirectionTextView = (TextView) getActivity().findViewById(R.id.wind_direction);
        mWindGustTextView = (TextView) getActivity().findViewById(R.id.wind_gusts);
        mCityNameTextView.setText(mFavCity.getmCity());
        mMoreDetailTextView = (TextView) getActivity().findViewById(R.id.more_detail_textview);
        mLatitudeTextView = (TextView) getActivity().findViewById(R.id.latitude);
        mLongitudeTextView = (TextView) getActivity().findViewById(R.id.longitude);
        mPressureTextView = (TextView) getActivity().findViewById(R.id.pressure);
        mHumidityTextView = (TextView) getActivity().findViewById(R.id.humidity);

        WeatherRecordProvidingTask loadTask =
                new WeatherRecordProvidingTask(getActivity(),
                        Contract.LOAD_CURRENT_FRAGMENT_DATA,
                        mWeatherStatusImageView,
                        mTemperatureNowTextView,
                        mWeatherStatusNowTextView,
                        mCloudCoverTextView,
                        mWindDirectionTextView,
                        mWindGustTextView,
                        mLatitudeTextView,
                        mLongitudeTextView,
                        mPressureTextView,
                        mHumidityTextView);

        loadTask.execute(mFavCity.getmCityId());
    }


    @Override
    public void onClick(View v) {
        if (mLookingAheadDataCard.getVisibility() != View.VISIBLE) {
            mLookingAheadDataCard.setVisibility(View.VISIBLE);
            mMoreDetailTextView.setVisibility(View.GONE);

        } else {
            mLookingAheadDataCard.setVisibility(View.GONE);
            mMoreDetailTextView.setVisibility(View.VISIBLE);
        }
    }
}
