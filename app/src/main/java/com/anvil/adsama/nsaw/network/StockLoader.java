package com.anvil.adsama.nsaw.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.anvil.adsama.nsaw.model.AlphaVantage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class StockLoader extends AsyncTaskLoader<ArrayList<AlphaVantage>> {

    private static final String LOG_TAG = StockLoader.class.getSimpleName();
    private ArrayList<AlphaVantage> stockList = new ArrayList<>();
    private String mUrl;

    public StockLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<AlphaVantage> loadInBackground() {
        if (mUrl != null) {
            StockParser stockParser = new StockParser();
            JSONObject stockSearchData = stockParser.getStockSearch(mUrl);
            if (stockSearchData != null) {
                try {
                    JSONObject stockMetaData = stockSearchData.getJSONObject("Meta Data");
                    String companyName = stockMetaData.optString("2. Symbol");
                    String lastRefTime = stockMetaData.optString("3. Last Refreshed");
                    JSONObject stockTimeSeries = stockSearchData.getJSONObject("Time Series (Daily)");
                    for (Iterator<String> iterator = stockTimeSeries.keys(); iterator.hasNext(); ) {
                        String key = iterator.next();
                        JSONObject currentStockDate = stockTimeSeries.optJSONObject(key);
                        float open = (float) currentStockDate.optInt("1. open");
                        float high = (float) currentStockDate.optInt("2. high");
                        float low = (float) currentStockDate.optInt("3. low");
                        float close = (float) currentStockDate.optInt("4. close");
                        float volume = (float) currentStockDate.optInt("5. volume");
                        AlphaVantage alphaVantage = new AlphaVantage(companyName, lastRefTime, open, high, low, close, volume);
                        stockList.add(alphaVantage);
                        Log.d(LOG_TAG, "DATA IS " + stockList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "Error occurred fetching stock data " + e.getMessage());
                }
            }
            return stockList;
        } else {
            StockParser stockParser = new StockParser();
            JSONObject stockData = stockParser.getStockData();
            if (stockData != null) {
                try {
                    JSONObject stockMetaData = stockData.getJSONObject("Meta Data");
                    String companyName = stockMetaData.optString("2. Symbol");
                    String lastRefTime = stockMetaData.optString("3. Last Refreshed");
                    JSONObject stockTimeSeries = stockData.getJSONObject("Time Series (Daily)");
                    for (Iterator<String> iterator = stockTimeSeries.keys(); iterator.hasNext(); ) {
                        String key = iterator.next();
                        JSONObject currentStockDate = stockTimeSeries.optJSONObject(key);
                        float open = (float) currentStockDate.optInt("1. open");
                        float high = (float) currentStockDate.optInt("2. high");
                        float low = (float) currentStockDate.optInt("3. low");
                        float close = (float) currentStockDate.optInt("4. close");
                        float volume = (float) currentStockDate.optInt("5. volume");
                        AlphaVantage alphaVantage = new AlphaVantage(companyName, lastRefTime, open, high, low, close, volume);
                        stockList.add(alphaVantage);
                        Log.d(LOG_TAG, "DATA IS " + stockList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "Error occurred fetching stock data " + e.getMessage());
                }
            }
            return stockList;
        }
    }
}