package com.anvil.adsama.nsaw.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherParser {

    private static final String LOG_TAG = WeatherParser.class.getSimpleName();
    private static final String WEATHER_URL = "";

    public static JSONArray getWeatherData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(WEATHER_URL).build();
        try {
            Response response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "WEATHER EXCEPTION IS " + e);
        }
        return null;
    }
}