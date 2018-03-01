package com.anvil.adsama.nsaw.network;

import android.os.AsyncTask;
import android.util.Log;

import com.anvil.adsama.nsaw.model.DarkSkyCurrent;
import com.anvil.adsama.nsaw.model.DarkSkyDaily;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherAsyncTask extends AsyncTask<Void, Void, ArrayList<DarkSkyCurrent>> {

    private static final String LOG_TAG = WeatherAsyncTask.class.getSimpleName();
    private WeatherListener mWeatherListener;
    private ArrayList<DarkSkyCurrent> weatherList = new ArrayList<>();
    private ArrayList<DarkSkyDaily> weatherDailyList = new ArrayList<>();

    public WeatherAsyncTask(WeatherListener weatherListener) {
        mWeatherListener = weatherListener;
    }

    @Override
    protected ArrayList<DarkSkyCurrent> doInBackground(Void... voids) {
        JSONObject weatherData = WeatherParser.getWeatherData();
        try {
            if (weatherData != null) {
                JSONObject currentData = weatherData.getJSONObject("currently");
                String summary = currentData.getString("summary");
                float temperature = Float.valueOf(currentData.getString("temperature"));
                String icon = currentData.getString("icon");
                float windSpeed = Float.valueOf(currentData.getString("windSpeed"));
                float visibility = Float.valueOf(currentData.getString("visibility"));
                JSONObject dailyData = weatherData.getJSONObject("daily");
                String weeklySummary = dailyData.getString("summary");
                JSONArray dailyArray = dailyData.getJSONArray("data");
                for (int i = 0; i < dailyArray.length(); i++) {
                    JSONObject currentDailyData = dailyArray.getJSONObject(i);
                    int time = currentDailyData.getInt("time");
                    String dailySummary = currentDailyData.getString("summary");
                    float highTemp = (float) currentDailyData.getInt("temperatureHigh");
                    float lowTemp = (float) currentDailyData.getInt("temperatureLow");
                    float apparentHigh = (float) currentDailyData.getInt("apparentTemperatureHigh");
                    float apparentLow = (float) currentDailyData.getInt("apparentTemperatureLow");
                    float dewPoint = (float) currentDailyData.getInt("dewPoint");
                    float humidity = (float) currentDailyData.getInt("humidity");
                    float dailyVisibility = (float) currentDailyData.getInt("visibility");
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

    @Override
    protected void onPostExecute(ArrayList<DarkSkyCurrent> darkSkyArrayList) {
        super.onPostExecute(darkSkyArrayList);
        mWeatherListener.returnWeatherList(darkSkyArrayList);
    }
}