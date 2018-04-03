package com.anvil.adsama.nsaw.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.anvil.adsama.nsaw.model.AlphaVantage;

import java.util.ArrayList;

public class BookmarkStockAdapter extends RecyclerView.Adapter<BookmarkStockAdapter.StockHolder> {

    private Context mContext;
    private ArrayList<AlphaVantage> mStockData = new ArrayList<>();

    public BookmarkStockAdapter(Context context, ArrayList<AlphaVantage> alphaVantage) {
        mContext = context;
        mStockData = alphaVantage;
    }

    @NonNull
    @Override
    public StockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull StockHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class StockHolder extends RecyclerView.ViewHolder {
        public StockHolder(View itemView) {
            super(itemView);
        }
    }
}