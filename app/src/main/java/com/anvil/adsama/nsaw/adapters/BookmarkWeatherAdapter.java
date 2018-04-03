package com.anvil.adsama.nsaw.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.anvil.adsama.nsaw.model.DarkSkyCurrent;

import java.util.ArrayList;

public class BookmarkWeatherAdapter extends RecyclerView.Adapter<BookmarkWeatherAdapter.WeatherHolder> {

    private Context mContext;
    private ArrayList<DarkSkyCurrent> mWeatherData = new ArrayList<>();

    public BookmarkWeatherAdapter(Context context, ArrayList<DarkSkyCurrent> darkSkyCurrent) {
        mContext = context;
        mWeatherData = darkSkyCurrent;
    }

    @NonNull
    @Override
    public WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class WeatherHolder extends RecyclerView.ViewHolder {

        public WeatherHolder(View itemView) {
            super(itemView);
        }
    }
}