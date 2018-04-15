package com.anvil.adsama.nsaw.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.model.DarkSkyCurrent;
import com.anvil.adsama.nsaw.model.DarkSkyDaily;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class WeatherWidget extends AppWidgetProvider {

    private static ArrayList<DarkSkyCurrent> mWeatherData;
    private static String mLocationName;
    private static int mImageId;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetId) {
        if (mWeatherData != null) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            views.setTextViewText(R.id.widget_temp, String.valueOf(mWeatherData.get(0).getTemperature() + " \u2103"));
            views.setTextViewText(R.id.widget_summary, mWeatherData.get(0).getSummary());
            views.setTextViewText(R.id.widget_location, mLocationName);
            mImageId = setWeatherId();
            setFinalIcon(views);
            Picasso.with(context).load(R.drawable.powerdarktwo).into(views, R.id.iv_poweredBy, appWidgetId);
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

    private static void setFinalIcon(RemoteViews remoteViews) {
        switch (mImageId) {
            case 0: {
                remoteViews.setImageViewResource(R.id.iv_widget_icon, R.drawable.ic_rainy);
                break;
            }
            case 1: {
                remoteViews.setImageViewResource(R.id.iv_widget_icon, R.drawable.ic_sunny);
                break;
            }
            case 2: {
                remoteViews.setImageViewResource(R.id.iv_widget_icon, R.drawable.ic_cloudy);
                break;
            }
            case 3: {
                remoteViews.setImageViewResource(R.id.iv_widget_icon, R.drawable.ic_snowflake);
                break;
            }
            case 4: {
                remoteViews.setImageViewResource(R.id.iv_widget_icon, R.drawable.ic_lightning);
                break;
            }
            case 5: {
                remoteViews.setImageViewResource(R.id.iv_widget_icon, R.drawable.ic_partly_sunny);
                break;
            }
            case 6: {
                remoteViews.setImageViewResource(R.id.iv_widget_icon, R.drawable.ic_showers);
                break;
            }
            case 7: {
                remoteViews.setImageViewResource(R.id.iv_widget_icon, R.drawable.ic_foggy);
                break;
            }
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

    public static void setWeatherList(ArrayList<DarkSkyCurrent> weatherData, String locationName) {
        mWeatherData = weatherData;
        mLocationName = locationName;
    }

    private static int setWeatherId() {
        String weatherIconData = mWeatherData.get(0).getSummary();
        if (weatherIconData.contains("Rain")) {
            mImageId = 0;
        } else if (weatherIconData.contains("Clear")) {
            mImageId = 1;
        } else if (weatherIconData.contains("Cloudy")) {
            mImageId = 2;
        } else if (weatherIconData.contains("Snow")) {
            mImageId = 3;
        } else if (weatherIconData.contains("Thunder")) {
            mImageId = 4;
        } else if (weatherIconData.contains("Partly Sunny")) {
            mImageId = 5;
        } else if (weatherIconData.contains("Showers")) {
            mImageId = 6;
        } else if (weatherIconData.contains("Foggy")) {
            mImageId = 7;
        }
        return mImageId;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateAppWidget(context, appWidgetManager, appWidgetIds);
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