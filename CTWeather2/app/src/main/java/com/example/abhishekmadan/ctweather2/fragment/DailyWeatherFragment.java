package com.example.abhishekmadan.ctweather2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.abhishekmadan.ctweather2.R;
import com.example.abhishekmadan.ctweather2.activity.ContainerActivity;
import com.example.abhishekmadan.ctweather2.adapter.DailyFragmentAdapter;
import com.example.abhishekmadan.ctweather2.modal.Contract;
import com.example.abhishekmadan.ctweather2.modal.DbModalCity;
import com.example.abhishekmadan.ctweather2.modal.OnlineModalCity;
import com.example.abhishekmadan.ctweather2.task.ForcastWeatherDataProvidingTask;

import java.util.ArrayList;

/**
 * Fragment to display the list of the forcasted weather condition of the
 * city in the form of a list view.
 */

public class DailyWeatherFragment extends Fragment implements AdapterView.OnItemClickListener {

    private DbModalCity mFavCity;

    private ListView mDailyWeatherForcast;

    private DailyFragmentAdapter mDailyFragmentAdapter;

    private ArrayList<OnlineModalCity> mForcastList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mFavCity = (DbModalCity) bundle.getSerializable("city");
        }
        View view = inflater.inflate(R.layout.fragment_daily_layout, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ForcastWeatherDataProvidingTask task = new ForcastWeatherDataProvidingTask(new TaskResultCommunicator() {
            @Override
            public void ServerDataFetched(ArrayList<OnlineModalCity> list) {
                populateListView(list);
            }
        });
        mDailyWeatherForcast = (ListView) getActivity().findViewById(R.id.daily_listview);
        task.execute(mFavCity.getmCityId());
        mDailyWeatherForcast.setOnItemClickListener(this);

    }

    /**
     * Method to populate the forcast weather condition list in this fragment.
     * @param dataList is the weather data corresponding to 15 days obtained from the server
     */
    public void populateListView(ArrayList<OnlineModalCity> dataList) {

        mForcastList = dataList;
        mDailyFragmentAdapter = new DailyFragmentAdapter(getActivity().getApplicationContext(), dataList);
        mDailyWeatherForcast.setAdapter(mDailyFragmentAdapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ContainerActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt("operationToPerform", Contract.LOAD_FORCAST_VIEW_PAGER);
        bundle.putSerializable("dataList", mForcastList);
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    /**
     * A call back interface which is used to get a callback when the data is
     * available from the async task and can be used to populate the list.
     */
    public interface TaskResultCommunicator {
        public void ServerDataFetched(ArrayList<OnlineModalCity> list);
    }
}
