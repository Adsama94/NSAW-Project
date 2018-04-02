package com.anvil.adsama.nsaw.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.activities.DetailActivity;
import com.anvil.adsama.nsaw.adapters.WeatherAdapter;
import com.anvil.adsama.nsaw.adapters.WeatherPositionInterface;
import com.anvil.adsama.nsaw.analytics.NsawApp;
import com.anvil.adsama.nsaw.model.DarkSkyCurrent;
import com.anvil.adsama.nsaw.model.DarkSkyDaily;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherFragment extends android.support.v4.app.Fragment implements WeatherPositionInterface {

    @BindView(R.id.recycler_weather)
    RecyclerView mWeatherRecyclerView;
    ArrayList<DarkSkyCurrent> mWeatherCurrentList;
    ArrayList<DarkSkyDaily> mWeatherDailyList;
    WeatherAdapter mWeatherAdapter;
    DarkSkyCurrent mWeatherData;
    @BindView(R.id.iv_icon)
    ImageView mIcon;
    @BindView(R.id.tv_visibility)
    TextView mVisibility;
    @BindView(R.id.tv_windSpeed)
    TextView mWindSpeed;
    @BindView(R.id.tv_temperature)
    TextView mTemperature;
    @BindView(R.id.tv_summary)
    TextView mSummary;
    @BindView(R.id.tv_weekly_summary)
    TextView mWeeklySummary;

    public WeatherFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState = getArguments();
        mWeatherCurrentList = new ArrayList<>();
        if (savedInstanceState != null)
            mWeatherCurrentList = savedInstanceState.getParcelableArrayList("DarkSkyCurrent");
        if (mWeatherCurrentList != null)
            mWeatherDailyList = mWeatherCurrentList.get(0).getDailyList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_fragment_layout, container, false);
        ButterKnife.bind(this, rootView);
        mWeatherData = mWeatherCurrentList.get(0);
        setWeatherIcon();
        mSummary.setText(mWeatherData.getSummary());
        mTemperature.setText(String.valueOf(mWeatherData.getTemperature() + " \u2103"));
        mWindSpeed.setText(String.valueOf(mWeatherData.getWindSpeed() + " m/s"));
        mVisibility.setText(String.valueOf(mWeatherData.getVisibility() + " Km"));
        mWeeklySummary.setText(mWeatherData.getWeeklySummary());
        initialiseWeather(mWeatherDailyList);
        return rootView;
    }

    private void initialiseWeather(ArrayList<DarkSkyDaily> weatherAPIArrayList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mWeatherAdapter = new WeatherAdapter(weatherAPIArrayList, getContext(), this);
        mWeatherRecyclerView.setAdapter(mWeatherAdapter);
        mWeatherRecyclerView.setLayoutManager(linearLayoutManager);
        mWeatherRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setWeatherIcon() {
        String weatherIconData = mWeatherData.getSummary();
        if (weatherIconData.contains("Rain")) {
            mIcon.setImageResource(R.drawable.ic_rainy);
        } else if (weatherIconData.contains("Clear")) {
            mIcon.setImageResource(R.drawable.ic_sunny);
        } else if (weatherIconData.contains("Cloudy")) {
            mIcon.setImageResource(R.drawable.ic_cloudy);
        } else if (weatherIconData.contains("Snow")) {
            mIcon.setImageResource(R.drawable.ic_snowflake);
        } else if (weatherIconData.contains("Thunder")) {
            mIcon.setImageResource(R.drawable.ic_lightning);
        } else if (weatherIconData.contains("Partly Sunny")) {
            mIcon.setImageResource(R.drawable.ic_partly_sunny);
        } else if (weatherIconData.contains("Showers")) {
            mIcon.setImageResource(R.drawable.ic_showers);
        }
    }

    @Override
    public void getWeatherPosition(int position) {
        Intent detailIntent = new Intent(getContext(), DetailActivity.class);
        if (mWeatherCurrentList != null)
            detailIntent.putParcelableArrayListExtra("Weather List", mWeatherCurrentList);
        detailIntent.putExtra("UID WEATHER", "FROM WEATHER");
        detailIntent.putExtra("Weather Position", position);
        startActivity(detailIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        NsawApp.getInstance().trackScreenView("WEATHER FRAGMENT");
    }
}