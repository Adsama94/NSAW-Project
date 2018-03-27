package com.anvil.adsama.nsaw.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.model.AlphaVantage;

import java.util.ArrayList;

public class StockRecyclerAdapter extends RecyclerView.Adapter<StockRecyclerAdapter.StockHolder> {

    private ArrayList<AlphaVantage> mStockList;
    private Context mContext;
    private StockPositionInterface mPositionInterface;

    public StockRecyclerAdapter(ArrayList<AlphaVantage> stockList, Context context, StockPositionInterface positionInterface) {
        mStockList = stockList;
        mContext = context;
        mPositionInterface = positionInterface;
    }

    @NonNull
    @Override
    public StockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.stock_card_layout, parent, false);
        return new StockHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StockHolder holder, int position) {
        AlphaVantage alphaVantage = mStockList.get(position);
        holder.companyName.setText(alphaVantage.getCompanyName());
        holder.open.setText(String.valueOf(alphaVantage.getOpen()));
        holder.close.setText(String.valueOf(alphaVantage.getClose()));
        holder.high.setText(String.valueOf(alphaVantage.getHigh()));
        holder.low.setText(String.valueOf(alphaVantage.getLow()));
        holder.refresh.setText(String.valueOf(alphaVantage.getRefreshTime()));
        holder.volume.setText(String.valueOf(alphaVantage.getVolume()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPositionInterface.getStockPosition(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mStockList != null) {
            return 30;
        } else {
            return 0;
        }
    }

    class StockHolder extends RecyclerView.ViewHolder {

        TextView companyName;
        TextView open;
        TextView close;
        TextView high;
        TextView low;
        TextView refresh;
        TextView volume;

        StockHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.tv_company_name);
            open = itemView.findViewById(R.id.tv_open);
            close = itemView.findViewById(R.id.tv_close);
            high = itemView.findViewById(R.id.tv_high);
            low = itemView.findViewById(R.id.tv_low);
            refresh = itemView.findViewById(R.id.tv_refresh);
            volume = itemView.findViewById(R.id.tv_volume);
        }
    }
}