package com.anvil.adsama.nsaw.network;

import android.os.AsyncTask;

import com.anvil.adsama.nsaw.model.AlphaVantage;

import java.util.ArrayList;

public class StockAsyncTask extends AsyncTask<Void, Void, ArrayList<AlphaVantage>> {

    private static final String LOG_TAG = StockAsyncTask.class.getSimpleName();
    private StockListener mStockListener;
    private ArrayList<AlphaVantage> stockList = new ArrayList<>();

    public StockAsyncTask(StockListener stockListener) {
        mStockListener = stockListener;
    }

    @Override
    protected ArrayList<AlphaVantage> doInBackground(Void... voids) {
        return stockList;
    }

    @Override
    protected void onPostExecute(ArrayList<AlphaVantage> alphaVantages) {
        super.onPostExecute(alphaVantages);
        mStockListener.returnStockList(alphaVantages);
    }
}