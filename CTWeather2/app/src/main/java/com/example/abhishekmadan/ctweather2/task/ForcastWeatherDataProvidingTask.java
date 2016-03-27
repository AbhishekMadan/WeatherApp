package com.example.abhishekmadan.ctweather2.task;

import android.os.AsyncTask;

import com.example.abhishekmadan.ctweather2.fragment.DailyWeatherFragment;
import com.example.abhishekmadan.ctweather2.modal.Contract;
import com.example.abhishekmadan.ctweather2.modal.OnlineModalCity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Async task to fetch data of next 15 day forcasts for the city. This data
 * is displayed in the view pager on the main activity screen.
 */
public class ForcastWeatherDataProvidingTask extends AsyncTask<Long, Void, ArrayList<OnlineModalCity>> {
    private DailyWeatherFragment.TaskResultCommunicator resultCommunicator;

    public ForcastWeatherDataProvidingTask(DailyWeatherFragment.TaskResultCommunicator taskResultCommunicator) {
        resultCommunicator = taskResultCommunicator;
    }

    @Override
    protected ArrayList<OnlineModalCity> doInBackground(Long... params) {

        URL url;
        ArrayList<OnlineModalCity> forcastedList = new ArrayList<OnlineModalCity>();
        OnlineModalCity cityData = null;
        HttpURLConnection urlConnection;
        String response;
        long cityId;

        try {
            cityId = params[0];
            StringBuilder source = new StringBuilder(Contract.FORCAST_API_SOURCE);
            source = source.append(cityId + "&units=metric&cnt=15&appid=" + Contract.APP_ID);

            url = new URL(source.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            response = convertInputStreamToString(inputStream);

            JSONObject rootObj = new JSONObject(response);
            JSONArray forcastList = rootObj.getJSONArray("list");

            for (int i = 0; i < forcastList.length(); i++) {
                cityData = new OnlineModalCity();
                JSONObject obj = forcastList.getJSONObject(i);
                cityData.date = obj.getLong("dt");
                JSONObject tempObj = obj.getJSONObject("temp");
                cityData.minTemperature = tempObj.getDouble("min");
                cityData.maxTemperature = tempObj.getDouble("max");
                cityData.mainTemperature = (cityData.maxTemperature);

                cityData.mainSeaLevelPressure = obj.getDouble("pressure");
                cityData.mainHumidity = obj.getDouble("humidity");

                JSONArray weatherObj = obj.getJSONArray("weather");
                JSONObject weatherItem = weatherObj.getJSONObject(weatherObj.length() - 1);
                cityData.weatherDesc = weatherItem.getString("description");
                cityData.weatherIcon = "img_" + weatherItem.getString("icon");
                cityData.windSpeed = obj.getDouble("speed");
                cityData.windDegree = obj.getDouble("deg");
                cityData.cloudiness = obj.getDouble("clouds");
                try {
                    cityData.rainVolume = obj.getDouble("rain");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    cityData.snowVolume = obj.getDouble("snow");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                forcastedList.add(cityData);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return forcastedList;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }
          /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<OnlineModalCity> list) {
        super.onPostExecute(list);
        resultCommunicator.ServerDataFetched(list);
    }

}
