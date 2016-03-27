package com.example.abhishekmadan.ctweather2.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.abhishekmadan.ctweather2.R;
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
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by abhishek.madan on 2/19/2016.
 */
public class WidgetUpdateService extends IntentService
{

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public WidgetUpdateService() {
        super("CTWeather2");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        URL url;

        HttpURLConnection urlConnection;

        String response;

        OnlineModalCity cityData =null;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);

        int[] allWidgetIds = intent
                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);


        try {

            StringBuilder source = new StringBuilder(Contract.API_SOURCE);
            source = source.append(1262180+"&units=metric&appid="+Contract.APP_ID);

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
            Log.d("ABHISHEK", "BAD Json received");
        }

        for(int appId : allWidgetIds){

            RemoteViews remoteViews = new RemoteViews(this
                    .getApplicationContext().getPackageName(),
                    R.layout.widget_layout);
            remoteViews.setImageViewResource(R.id.widget_weather_thumbnail_imageview,getResources().getIdentifier(cityData.weatherIcon,"drawable",getApplicationContext().getPackageName()));
            remoteViews.setTextViewText(R.id.widget_temperature_textview, cityData.mainTemperature + " °C");
            remoteViews.setTextViewText(R.id.widget_weather_description,cityData.weatherDesc);
            remoteViews.setTextViewText(R.id.widget_current_date_textview,calendar.getTime().toString());
            appWidgetManager.updateAppWidget(appId, remoteViews);
        }

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

}
