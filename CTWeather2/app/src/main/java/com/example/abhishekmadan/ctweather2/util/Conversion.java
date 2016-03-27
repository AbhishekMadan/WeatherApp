package com.example.abhishekmadan.ctweather2.util;

import java.util.Calendar;

/**
 * class with conversion function
 */
public class Conversion
{
    /**
     * Method to convert Unix time to IST
     * @param unix
     * @return
     */
    public static String getDate(long unix)
    {
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(unix*1000);
        int day = calender.get(Calendar.DAY_OF_MONTH);
        int month = calender.get(Calendar.MONTH)+1;
        return (day<10?"0"+day:String.valueOf(day))+"/"+(month<10?"0"+month:String.valueOf(month));
    }

}
