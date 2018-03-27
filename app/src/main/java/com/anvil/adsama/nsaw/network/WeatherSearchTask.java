package com.anvil.adsama.nsaw.network;

import android.os.AsyncTask;
import android.util.Log;

import com.anvil.adsama.nsaw.model.DarkSkyCurrent;
import com.anvil.adsama.nsaw.model.DarkSkyDaily;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherSearchTask extends AsyncTask<String, Void, ArrayList<DarkSkyCurrent>> {

    private static final String LOG_TAG = WeatherSearchTask.class.getSimpleName();
    private WeatherListener mWeatherListener;
    private ArrayList<DarkSkyCurrent> mWeatherCurrentList = new ArrayList<>();
    private ArrayList<DarkSkyDaily> weatherDailyList = new ArrayList<>();

    public WeatherSearchTask(WeatherListener weatherListener) {
        mWeatherListener = weatherListener;
    }

    @Override
    protected ArrayList<DarkSkyCurrent> doInBackground(String... strings) {
        WeatherParser weatherParser = new WeatherParser();
        JSONObject weatherData = weatherParser.getWeatherSearch(strings[0]);
        try {
            if (weatherData != null) {
                JSONObject currentData = weatherData.getJSONObject("currently");
                String summary = currentData.optString("summary");
                float temperature = Float.valueOf(currentData.optString("temperature"));
                String icon = currentData.optString("icon");
                float windSpeed = Float.valueOf(currentData.optString("windSpeed"));
                float visibility = Float.valueOf(currentData.optString("visibility"));
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
                    mWeatherCurrentList.add(darkSky);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error occurred fetching weather data " + e.getMessage());
        }
        return mWeatherCurrentList;
    }

    @Override
    protected void onPostExecute(ArrayList<DarkSkyCurrent> darkSkyCurrents) {
        super.onPostExecute(darkSkyCurrents);
        mWeatherListener.returnWeatherList(darkSkyCurrents);
    }
}