package com.anvil.adsama.nsaw.network;

import android.os.AsyncTask;
import android.util.Log;

import com.anvil.adsama.nsaw.fragments.StockFragment;
import com.anvil.adsama.nsaw.model.AlphaVantage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class StockSearchTask extends AsyncTask<String, Void, ArrayList<AlphaVantage>> {

    private static final String LOG_TAG = StockSearchTask.class.getSimpleName();
    private StockListener mStockListener;
    private ArrayList<AlphaVantage> stockList = new ArrayList<>();
    private StockFragment stockFragment;

    public StockSearchTask(StockFragment fragment, StockListener stockListener) {
        stockFragment = fragment;
        mStockListener = stockListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        stockFragment.showProgress();
    }

    @Override
    protected ArrayList<AlphaVantage> doInBackground(String... strings) {
        StockParser stockParser = new StockParser();
        JSONObject stockData = stockParser.getStockSearch(strings[0]);
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

    @Override
    protected void onPostExecute(ArrayList<AlphaVantage> alphaVantages) {
        super.onPostExecute(alphaVantages);
        mStockListener.returnStockList(alphaVantages);
        stockFragment.hideProgress();
    }
}