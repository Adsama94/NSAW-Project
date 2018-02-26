package com.anvil.adsama.nsaw.network;

import android.os.AsyncTask;
import android.util.Log;

import com.anvil.adsama.nsaw.model.DarkSky;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherAsyncTask extends AsyncTask<Void, Void, ArrayList<DarkSky>> {

    private static final String LOG_TAG = WeatherAsyncTask.class.getSimpleName();
    private WeatherListener mWeatherListener;
    private ArrayList<DarkSky> weatherList = new ArrayList<>();

    public WeatherAsyncTask(WeatherListener weatherListener) {
        mWeatherListener = weatherListener;
    }

    @Override
    protected ArrayList<DarkSky> doInBackground(Void... voids) {
        JSONObject weatherData = WeatherParser.getWeatherData();
        try {
            if (weatherData != null) {
                JSONObject currentData = weatherData.getJSONObject("currently");
                String summary = currentData.getString("summary");
                float temperature = Float.valueOf(currentData.getString("temperature"));
                String icon = currentData.getString("icon");
                float windSpeed = Float.valueOf(currentData.getString("windSpeed"));
                float visibility = Float.valueOf(currentData.getString("visibility"));
                DarkSky darkSky = new DarkSky(summary, temperature, icon, windSpeed, visibility);
                weatherList.add(darkSky);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error occurred fetching weather data " + e.getMessage());
        }
        return weatherList;
    }

    @Override
    protected void onPostExecute(ArrayList<DarkSky> darkSkyArrayList) {
        super.onPostExecute(darkSkyArrayList);
        mWeatherListener.returnWeatherList(darkSkyArrayList);
    }
}