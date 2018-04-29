package com.anvil.adsama.nsaw.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class WeatherParser {

    private static final String LOG_TAG = WeatherParser.class.getSimpleName();
    private static final String WEATHER_URL = "https://api.darksky.net/forecast/6baefba9f2a860bd68ecb53fd8024caa/28.7041,77.1025?units=si";

    JSONObject getWeatherData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(WEATHER_URL).build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String actualResponse = responseBody.string();
                return new JSONObject(actualResponse);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "WEATHER EXCEPTION IS " + e);
        }
        return null;
    }

    JSONObject getWeatherSearch(String passedUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(passedUrl).build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String actualResponse = responseBody.string();
                return new JSONObject(actualResponse);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "WEATHER SEARCH EXCEPTION IS " + e);
        }
        return null;
    }
}