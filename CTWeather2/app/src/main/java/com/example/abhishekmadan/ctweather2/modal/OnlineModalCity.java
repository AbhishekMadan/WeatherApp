package com.example.abhishekmadan.ctweather2.modal;

import java.io.Serializable;

/**
 * Modal class corresponding to the data obtained from the Server.
 */
public class OnlineModalCity implements Serializable
{
    public long date;
    public double longitude;
    public double latitude;
    public String weatherDesc;
    public String weatherIcon;
    public double windSpeed;
    public double windDegree;
    public double mainTemperature;
    public double minTemperature;
    public double maxTemperature;
    public double mainHumidity;
    public double mainSeaLevelPressure;
    public double cloudiness;
    public double rainVolume;
    public double snowVolume;
    public long sunRise;
    public long sunSet;



}
