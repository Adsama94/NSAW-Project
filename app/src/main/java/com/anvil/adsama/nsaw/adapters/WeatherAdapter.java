package com.anvil.adsama.nsaw.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.model.DarkSky;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {

    private ArrayList<DarkSky> mWeatherList;
    private Context mContext;

    public WeatherAdapter(ArrayList<DarkSky> weatherList, Context context) {
        mWeatherList = weatherList;
        mContext = context;
    }

    @Override
    public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.weather_list_item, parent, false);
        return new WeatherHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherHolder holder, int position) {
        DarkSky darkSky = mWeatherList.get(position);
        Picasso.with(mContext).load(darkSky.getIcon()).into(holder.mIconView);
        holder.mSummary.setText(darkSky.getSummary());
        holder.mWindSpeed.setText(String.valueOf(darkSky.getWindSpeed()));
        holder.mTemperature.setText(String.valueOf(darkSky.getTemperature()));
        holder.mVisibility.setText(String.valueOf(darkSky.getVisibility()));
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

        ImageView mIconView;
        TextView mSummary;
        TextView mWindSpeed;
        TextView mTemperature;
        TextView mVisibility;

        WeatherHolder(View itemView) {
            super(itemView);
            mIconView = itemView.findViewById(R.id.iv_icon);
            mSummary = itemView.findViewById(R.id.tv_summary);
            mWindSpeed = itemView.findViewById(R.id.tv_windSpeed);
            mTemperature = itemView.findViewById(R.id.tv_temperature);
            mVisibility = itemView.findViewById(R.id.tv_visibility);
        }
    }
}