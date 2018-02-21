package com.anvil.adsama.nsaw.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsParser {

    private static final String LOG_TAG = NewsParser.class.getSimpleName();
    private static final String NEWS_URL = "https://newsapi.org/v2/everything?q=bitcoin&sortBy=publishedAt&apiKey=f89ab3ddfae84bd8866a8d7d26d961f1";

    public static JSONObject getNewsData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(NEWS_URL).build();
        try {
            Response response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "NEWS EXCEPTION IS " + e);
        }
        return null;
    }
}