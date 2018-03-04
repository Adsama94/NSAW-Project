package com.anvil.adsama.nsaw.network;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsParser {

    private static final String LOG_TAG = NewsParser.class.getSimpleName();
    private static final String NEWS_URL = "https://newsapi.org/v2/everything?q=bitcoin&language=en&pageSize=30&sortBy=publishedAt&apiKey=f89ab3ddfae84bd8866a8d7d26d961f1";
    private String queryResponse;

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

    public String makeNewsQuery(String queryUrl) {
        OkHttpClient queryClient = new OkHttpClient();
        final Request queryRequest = new Request.Builder().url(queryUrl).build();
        queryClient.newCall(queryRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("News Parser ", "error onFailure " + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                queryResponse = String.valueOf(response);
                Log.d("News Parser ", queryResponse);
            }
        });
        return queryResponse;
    }
}