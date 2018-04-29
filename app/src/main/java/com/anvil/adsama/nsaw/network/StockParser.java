package com.anvil.adsama.nsaw.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class StockParser {

    private static final String LOG_TAG = StockParser.class.getSimpleName();
    private static final String STOCK_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=MSFT&outputsize=compact&apikey=A3G413PBID5IBS0W";

    JSONObject getStockData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(STOCK_URL).build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String actualResponse = responseBody.string();
                return new JSONObject(actualResponse);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "STOCK EXCEPTION IS " + e);
        }
        return null;
    }

    JSONObject getStockSearch(String passedUrl) {
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
            Log.e(LOG_TAG, "STOCK SEARCH EXCEPTION IS " + e);
        }
        return null;
    }
}