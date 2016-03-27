package com.example.abhishekmadan.ctweather2.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.abhishekmadan.ctweather2.service.WidgetUpdateService;

/**
 * Created by abhishek.madan on 2/19/2016.
 */
public class SimpleWidgetProvider extends AppWidgetProvider
{
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        ComponentName name = new ComponentName(context,SimpleWidgetProvider.class);

        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(name);

        Intent intent = new Intent(context.getApplicationContext(), WidgetUpdateService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        context.startService(intent);
    }
}
