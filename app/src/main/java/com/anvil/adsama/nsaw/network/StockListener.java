package com.anvil.adsama.nsaw.network;

import com.anvil.adsama.nsaw.model.AlphaVantage;

import java.util.ArrayList;

public interface StockListener {
    void returnStockList(ArrayList<AlphaVantage> alphaVantageList);
}