package com.anvil.adsama.nsaw.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.model.DarkSkyDaily;

import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {

    private final ArrayList<DarkSkyDaily> mWeatherList;
    private final Context mContext;
    private final WeatherPositionInterface mPositionInterface;

    public WeatherAdapter(ArrayList<DarkSkyDaily> weatherList, Context context, WeatherPositionInterface positionInterface) {
        mWeatherList = weatherList;
        mContext = context;
        mPositionInterface = positionInterface;
    }

    @NonNull
    @Override
    public WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.weather_daily_item, parent, false);
        return new WeatherHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WeatherHolder holder, int position) {
        DarkSkyDaily darkSky = mWeatherList.get(position);
        Long epochTime = darkSky.getTime();
        Date date = new Date(epochTime * 1000);
        String passedDate = String.valueOf(date);
        String actualDate = passedDate.substring(0, 11);
        holder.mTime.setText(String.valueOf(actualDate));
        holder.mHighTemp.setText(String.valueOf(darkSky.getHighTemp() + " \u2103"));
        holder.mLowTemp.setText(String.valueOf(darkSky.getLowTemp() + " \u2103"));
        holder.mDailySummary.setText(darkSky.getSummary());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPositionInterface.getWeatherPosition(holder.getAdapterPosition());
            }
        });
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

        final TextView mDailySummary;
        final TextView mHighTemp;
        final TextView mLowTemp;
        final TextView mTime;

        WeatherHolder(View itemView) {
            super(itemView);
            mDailySummary = itemView.findViewById(R.id.tv_daily_summary);
            mHighTemp = itemView.findViewById(R.id.tv_highTemp);
            mLowTemp = itemView.findViewById(R.id.tv_lowTemp);
            mTime = itemView.findViewById(R.id.tv_time);
        }
    }
}