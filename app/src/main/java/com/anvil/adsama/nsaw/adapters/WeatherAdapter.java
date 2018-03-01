package com.anvil.adsama.nsaw.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.model.DarkSkyDaily;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {

    private ArrayList<DarkSkyDaily> mWeatherList;
    private Context mContext;

    public WeatherAdapter(ArrayList<DarkSkyDaily> weatherList, Context context) {
        mWeatherList = weatherList;
        mContext = context;
    }

    @Override
    public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.weather_daily_item, parent, false);
        return new WeatherHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherHolder holder, int position) {
        DarkSkyDaily darkSky = mWeatherList.get(position);
        holder.mTime.setText(String.valueOf(darkSky.getTime()));
        holder.mHighTemp.setText(String.valueOf(darkSky.getHighTemp() + "\u2103"));
        holder.mLowTemp.setText(String.valueOf(darkSky.getLowTemp() + "\u2103"));
        holder.mDailySummary.setText(darkSky.getSummary());
    }

    @Override
    public int getItemCount() {
        if (mWeatherList != null) {
            return mWeatherList.size();
        } else {
            return 0;
        }
    }

    class WeatherHolder extends RecyclerView.ViewHolder {

        TextView mDailySummary;
        TextView mHighTemp;
        TextView mLowTemp;
        TextView mTime;

        WeatherHolder(View itemView) {
            super(itemView);
            mDailySummary = itemView.findViewById(R.id.tv_daily_summary);
            mHighTemp = itemView.findViewById(R.id.tv_highTemp);
            mLowTemp = itemView.findViewById(R.id.tv_lowTemp);
            mTime = itemView.findViewById(R.id.tv_time);
        }
    }
}