package com.example.abhishekmadan.ctweather2.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Async task to get the complete present day data for the city selected by the user in the
 * Navigation list of the navigation drawer.
 */
public class WeatherRecordProvidingTask extends AsyncTask<Long,Void,OnlineModalCity> {

    private WeakReference<ImageView> thumbnail;

    private WeakReference<TextView> temperature;

    private WeakReference<TextView> weatherDescription;

    private WeakReference<TextView> cloudCover;

    private WeakReference<TextView> windDirection;

    private WeakReference<TextView> windSpeed;

    private WeakReference<TextView> latitude;

    private WeakReference<TextView> longitude;

    private WeakReference<TextView> pressure;

    private WeakReference<TextView> humidity;

    private int callType;

    private Context mContext;

    public WeatherRecordProvidingTask(Context context,int type, ImageView image, TextView temp) {
        mContext = context;
        callType = type;
        thumbnail = new WeakReference<ImageView>(image);
        temperature = new WeakReference<TextView>(temp);
        weatherDescription = null;
        cloudCover = null;
        windDirection = null;
        windSpeed = null;

    }

    public WeatherRecordProvidingTask(Context context , int type , ImageView image, TextView temp , TextView weatherDesc,
                                      TextView cldCover, TextView windDir, TextView windSpd, TextView latitude,TextView longitude,
                                      TextView pressure, TextView humidity){
        mContext = context;
        callType = type;
        thumbnail = new WeakReference<ImageView>(image);
        temperature = new WeakReference<TextView>(temp);
        weatherDescription = new WeakReference<TextView>(weatherDesc);
        cloudCover = new WeakReference<TextView>(cldCover);
        windDirection = new WeakReference<TextView>(windDir);
        windSpeed = new WeakReference<TextView>(windSpd);
        this.latitude = new WeakReference<TextView>(latitude);
        this.longitude = new WeakReference<TextView>(longitude);
        this.pressure = new WeakReference<TextView>(pressure);
        this.humidity = new WeakReference<TextView>(humidity);

    }

    @Override
    protected OnlineModalCity doInBackground(Long... params) {

        URL url;

        HttpURLConnection urlConnection;

        String response;

        OnlineModalCity cityData =null;

        long cityId;

        try {
            cityId = params[0];
            StringBuilder source = new StringBuilder(Contract.API_SOURCE);
            source = source.append(cityId+"&units=metric&appid="+Contract.APP_ID);

            url = new URL(source.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            response = convertInputStreamToString(inputStream);

            //Parsing Json object
            cityData = new OnlineModalCity();
            JSONObject rootObj = new JSONObject(response);
            JSONObject coordObj = rootObj.getJSONObject("coord");
            cityData.longitude = coordObj.getDouble("lon");
            cityData.latitude = coordObj.getDouble("lat");
            JSONArray weatherArray =  rootObj.getJSONArray("weather");
            JSONObject weatherElement = weatherArray.getJSONObject(weatherArray.length()-1);
            cityData.weatherDesc = weatherElement.getString("description");
            cityData.weatherIcon = "img_"+weatherElement.getString("icon");
            JSONObject mainObj = rootObj.getJSONObject("main");
            cityData.mainTemperature = mainObj.getDouble("temp");
            cityData.mainSeaLevelPressure = mainObj.getDouble("sea_level");
            cityData.mainHumidity = mainObj.getDouble("humidity");
            JSONObject windObj = rootObj.getJSONObject("wind");
            cityData.windSpeed = windObj.getDouble("speed");
            cityData.windDegree = windObj.getDouble("deg");


            JSONObject cloudObj = rootObj.getJSONObject("clouds");
            cityData.cloudiness = cloudObj.getDouble("all");
            try {
                JSONObject rainObj = rootObj.getJSONObject("rain");
                cityData.rainVolume = rainObj.getDouble("3h");
            }catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONObject snowObj = rootObj.getJSONObject("snow");
                cityData.snowVolume = snowObj.getDouble("3h");
            }catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject sysObj = rootObj.getJSONObject("sys");
            cityData.sunRise = sysObj.getLong("sunrise");
            cityData.sunSet = sysObj.getLong("sunset");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("ABHISHEK","BAD Json received");
        }
        return cityData;
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
    protected void onPostExecute(OnlineModalCity cityData) {
        if(cityData!=null){
        if(callType == Contract.LOAD_NAV_ITEM)
        {
           ImageView navThumbnail = thumbnail.get();
           TextView navTemp = temperature.get();
            if(navThumbnail!=null && navTemp!=null){
                navThumbnail.setImageResource(mContext.getResources().getIdentifier(cityData.weatherIcon,"drawable",mContext.getPackageName()));
                navTemp.setText(cityData.mainTemperature+" °C");
            }
        }else if(callType == Contract.LOAD_CURRENT_FRAGMENT_DATA)
        {
            ImageView tempThumbnail = thumbnail.get();
            TextView tempTemperature = temperature.get();
            TextView tempWeatherDescription = weatherDescription.get();
            TextView tempCloudCover = cloudCover.get();
            TextView tempWindDirection = windDirection.get();
            TextView tempWindSpeed = windSpeed.get();
            TextView tempLatitude = latitude.get();
            TextView tempLongitude = longitude.get();
            TextView tempHumidity = humidity.get();
            TextView tempPressure = pressure.get();
            if(tempThumbnail!=null)
                tempThumbnail.setImageResource(mContext.getResources().getIdentifier(cityData.weatherIcon,"drawable",mContext.getPackageName()));
            if(tempTemperature!=null)
                tempTemperature.setText(cityData.mainTemperature+" °C");
            if(tempWeatherDescription!=null)
                tempWeatherDescription.setText(cityData.weatherDesc);
            if(tempCloudCover!=null)
                tempCloudCover.setText(cityData.cloudiness+"%");
            if(tempWindDirection!=null)
                tempWindDirection.setText(String.valueOf(cityData.windDegree)+"°");
            if(tempWindSpeed!=null)
                tempWindSpeed.setText(String.valueOf(cityData.windSpeed));
            if(tempLatitude!=null)
                tempLatitude.setText(String.valueOf(cityData.latitude));
            if(tempLongitude!=null)
                tempLongitude.setText(String.valueOf(cityData.longitude));
            if(tempHumidity!=null)
                tempHumidity.setText(String.valueOf(cityData.mainHumidity)+"%");
            if(tempPressure!=null)
                tempPressure.setText(String.valueOf(cityData.mainSeaLevelPressure)+" hPa");
        }
       }
    }
}