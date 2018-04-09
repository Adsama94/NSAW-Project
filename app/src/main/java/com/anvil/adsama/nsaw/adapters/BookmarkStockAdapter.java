package com.anvil.adsama.nsaw.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.database.NsawContract;
import com.anvil.adsama.nsaw.model.AlphaVantage;

import java.util.ArrayList;

public class BookmarkStockAdapter extends RecyclerView.Adapter<BookmarkStockAdapter.StockHolder> {

    private Context mContext;
    private ArrayList<AlphaVantage> mStockData;

    public BookmarkStockAdapter(Context context, ArrayList<AlphaVantage> alphaVantage) {
        mContext = context;
        mStockData = alphaVantage;
    }

    @NonNull
    @Override
    public StockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bookmark_stock_item, parent, false);
        return new StockHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockHolder holder, int position) {
        AlphaVantage alphaVantage = mStockData.get(position);
        holder.companyName.setText(alphaVantage.getCompanyName());
        holder.refreshTime.setText(alphaVantage.getRefreshTime());
        holder.high.setText(String.valueOf(alphaVantage.getHigh()));
        holder.low.setText(String.valueOf(alphaVantage.getLow()));
        holder.volume.setText(String.valueOf(alphaVantage.getVolume()));
    }

    @Override
    public int getItemCount() {
        if (mStockData != null) {
            return mStockData.size();
        } else return 0;
    }

    public void addStock(Cursor cursor) {
        mStockData.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String company = cursor.getString(NsawContract.NsawEntry.COL_STOCK_NAME);
                String refresh = cursor.getString(NsawContract.NsawEntry.COL_STOCK_REFRESH);
                float open = cursor.getFloat(NsawContract.NsawEntry.COL_STOCK_OPEN);
                float close = cursor.getFloat(NsawContract.NsawEntry.COL_STOCK_CLOSE);
                float high = cursor.getFloat(NsawContract.NsawEntry.COL_STOCK_HIGH);
                float low = cursor.getFloat(NsawContract.NsawEntry.COL_STOCK_LOW);
                float volume = cursor.getFloat(NsawContract.NsawEntry.COL_STOCK_VOLUME);
                AlphaVantage alphaVantage = new AlphaVantage(company, refresh, open, close, high, low, volume);
                mStockData.add(alphaVantage);
            } while (cursor.moveToNext());
        }
        notifyDataSetChanged();
    }

    class StockHolder extends RecyclerView.ViewHolder {

        TextView companyName;
        TextView high;
        TextView low;
        TextView refreshTime;
        TextView volume;

        StockHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.tv_bookmark_stock_name);
            refreshTime = itemView.findViewById(R.id.tv_bookmark_stock_refresh);
            high = itemView.findViewById(R.id.tv_bookmark_stock_high);
            low = itemView.findViewById(R.id.tv_bookmark_stock_low);
            volume = itemView.findViewById(R.id.tv_bookmark_stock_volume);
        }
    }
}