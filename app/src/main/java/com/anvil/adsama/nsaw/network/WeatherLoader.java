package com.anvil.adsama.nsaw.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.anvil.adsama.nsaw.model.DarkSkyCurrent;
import com.anvil.adsama.nsaw.model.DarkSkyDaily;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherLoader extends AsyncTaskLoader<ArrayList<DarkSkyCurrent>> {

    private static final String LOG_TAG = WeatherLoader.class.getSimpleName();
    private ArrayList<DarkSkyCurrent> weatherList = new ArrayList<>();
    private ArrayList<DarkSkyDaily> weatherDailyList = new ArrayList<>();
    private String mUrl;

    public WeatherLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<DarkSkyCurrent> loadInBackground() {
        if (mUrl != null) {
            WeatherParser weatherParser = new WeatherParser();
            JSONObject weatherSearchData = weatherParser.getWeatherSearch(mUrl);
            try {
                if (weatherSearchData != null) {
                    JSONObject currentData = weatherSearchData.getJSONObject("currently");
                    String summary = currentData.optString("summary");
                    String icon = currentData.optString("icon");
                    float temperature = currentData.optInt("temperature");
                    float windSpeed = currentData.optInt("windSpeed");
                    float visibility = currentData.optInt("visibility");
                    JSONObject dailyData = weatherSearchData.getJSONObject("daily");
                    String weeklySummary = dailyData.optString("summary");
                    JSONArray dailyArray = dailyData.getJSONArray("data");
                    for (int i = 0; i < dailyArray.length(); i++) {
                        JSONObject currentDailyData = dailyArray.getJSONObject(i);
                        int time = currentDailyData.optInt("time");
                        String dailySummary = currentDailyData.optString("summary");
                        float highTemp = (float) currentDailyData.optInt("temperatureHigh");
                        float lowTemp = (float) currentDailyData.optInt("temperatureLow");
                        float apparentHigh = (float) currentDailyData.optInt("apparentTemperatureHigh");
                        float apparentLow = (float) currentDailyData.optInt("apparentTemperatureLow");
                        float dewPoint = (float) currentDailyData.optInt("dewPoint");
                        float humidity = (float) currentDailyData.optInt("humidity");
                        float dailyVisibility = (float) currentDailyData.optInt("visibility");
                        DarkSkyDaily darkSkyDaily = new DarkSkyDaily(time, dailySummary, highTemp, lowTemp, apparentHigh, apparentLow, dewPoint, humidity, dailyVisibility);
                        weatherDailyList.add(darkSkyDaily);
                        DarkSkyCurrent darkSky = new DarkSkyCurrent(summary, temperature, icon, windSpeed, visibility, weeklySummary, weatherDailyList);
                        weatherList.add(darkSky);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Error occurred fetching weather data " + e.getMessage());
            }
            return weatherList;
        } else {
            WeatherParser weatherParser = new WeatherParser();
            JSONObject weatherData = weatherParser.getWeatherData();
            try {
                if (weatherData != null) {
                    JSONObject currentData = weatherData.getJSONObject("currently");
                    String summary = currentData.optString("summary");
                    String icon = currentData.optString("icon");
                    float temperature = currentData.optInt("temperature");
                    float windSpeed = currentData.optInt("windSpeed");
                    float visibility = currentData.optInt("visibility");
                    JSONObject dailyData = weatherData.getJSONObject("daily");
                    String weeklySummary = dailyData.optString("summary");
                    JSONArray dailyArray = dailyData.getJSONArray("data");
                    for (int i = 0; i < dailyArray.length(); i++) {
                        JSONObject currentDailyData = dailyArray.getJSONObject(i);
                        int time = currentDailyData.optInt("time");
                        String dailySummary = currentDailyData.optString("summary");
                        float highTemp = (float) currentDailyData.optInt("temperatureHigh");
                        float lowTemp = (float) currentDailyData.optInt("temperatureLow");
                        float apparentHigh = (float) currentDailyData.optInt("apparentTemperatureHigh");
                        float apparentLow = (float) currentDailyData.optInt("apparentTemperatureLow");
                        float dewPoint = (float) currentDailyData.optInt("dewPoint");
                        float humidity = (float) currentDailyData.optInt("humidity");
                        float dailyVisibility = (float) currentDailyData.optInt("visibility");
                        DarkSkyDaily darkSkyDaily = new DarkSkyDaily(time, dailySummary, highTemp, lowTemp, apparentHigh, apparentLow, dewPoint, humidity, dailyVisibility);
                        weatherDailyList.add(darkSkyDaily);
                        DarkSkyCurrent darkSky = new DarkSkyCurrent(summary, temperature, icon, windSpeed, visibility, weeklySummary, weatherDailyList);
                        weatherList.add(darkSky);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Error occurred fetching weather data " + e.getMessage());
            }
            return weatherList;
        }
    }
}