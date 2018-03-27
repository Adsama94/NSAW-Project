package com.anvil.adsama.nsaw.adapters;

import com.anvil.adsama.nsaw.model.AlphaVantage;
import com.robinhood.spark.SparkAdapter;

import java.util.ArrayList;

public class StockGraphAdapter extends SparkAdapter {

    private ArrayList<AlphaVantage> mStockList;

    public StockGraphAdapter() {
    }

    @Override
    public int getCount() {
        return mStockList.size();
    }

    @Override
    public Object getItem(int index) {
        return index;
    }

    @Override
    public float getY(int index) {
        return index;
    }
}