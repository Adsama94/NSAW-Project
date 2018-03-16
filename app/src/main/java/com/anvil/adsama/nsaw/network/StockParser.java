package com.anvil.adsama.nsaw.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StockParser {

    private static final String LOG_TAG = StockParser.class.getSimpleName();
    private static final String STOCK_URL = "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=USD&to_currency=INR&apikey=A3G413PBID5IBS0W";

    public static JSONArray getStockData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(STOCK_URL).build();
        try {
            Response response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "STOCK EXCEPTION IS " + e);
        }
        return null;
    }
}