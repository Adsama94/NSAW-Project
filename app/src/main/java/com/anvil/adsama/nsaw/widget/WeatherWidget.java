package com.anvil.adsama.nsaw.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.model.DarkSkyCurrent;
import com.anvil.adsama.nsaw.model.DarkSkyDaily;

import java.util.ArrayList;
import java.util.Date;

public class WeatherWidget extends AppWidgetProvider {

    private static ArrayList<DarkSkyCurrent> mWeatherData;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        if (mWeatherData != null) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            views.setTextViewText(R.id.widget_temp, String.valueOf(mWeatherData.get(0).getTemperature() + " \u2103"));
            views.setTextViewText(R.id.widget_summary, mWeatherData.get(0).getSummary());
            views.setImageViewResource(R.id.iv_poweredBy, R.drawable.powerdark);
            ArrayList<DarkSkyDaily> dailyData = mWeatherData.get(0).getDailyList();
            String actualDateOne = setForecastDate(dailyData, 1);
            String actualTempOne = setForecastTemps(dailyData, 1);
            views.setTextViewText(R.id.widget_date_one, actualDateOne);
            views.setTextViewText(R.id.temp_one, actualTempOne);
            String actualDateTwo = setForecastDate(dailyData, 2);
            String actualTempTwo = setForecastTemps(dailyData, 2);
            views.setTextViewText(R.id.widget_date_two, actualDateTwo);
            views.setTextViewText(R.id.temp_two, actualTempTwo);
            String actualDateThree = setForecastDate(dailyData, 3);
            String actualTempThree = setForecastTemps(dailyData, 3);
            views.setTextViewText(R.id.widget_date_three, actualDateThree);
            views.setTextViewText(R.id.temp_three, actualTempThree);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private static String setForecastDate(ArrayList<DarkSkyDaily> dailyData, int position) {
        Long epochTime = dailyData.get(position).getTime();
        Date date = new Date(epochTime * 1000);
        String passedDate = String.valueOf(date);
        return passedDate.substring(0, 3);
    }

    private static String setForecastTemps(ArrayList<DarkSkyDaily> dailyData, int position) {
        String highTemp = String.valueOf(dailyData.get(position).getHighTemp());
        String lowTemp = String.valueOf(dailyData.get(position).getLowTemp());
        return highTemp + " \u2191" + "\n" + lowTemp + " \u2193";
    }

    public static void setWeatherList(ArrayList<DarkSkyCurrent> weatherData) {
        mWeatherData = weatherData;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }
}