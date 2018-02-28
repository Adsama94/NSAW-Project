package com.anvil.adsama.nsaw.fragments;

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
import com.anvil.adsama.nsaw.adapters.WeatherAdapter;
import com.anvil.adsama.nsaw.model.DarkSkyCurrent;
import com.anvil.adsama.nsaw.model.DarkSkyDaily;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherFragment extends android.support.v4.app.Fragment {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_fragment_layout, container, false);
        ButterKnife.bind(this, rootView);
        mWeatherData = mWeatherCurrentList.get(0);
        Picasso.with(getContext()).load(mWeatherData.getIcon()).placeholder(R.drawable.weather_nav).into(mIcon);
        mSummary.setText(mWeatherData.getSummary());
        mTemperature.setText(String.valueOf(mWeatherData.getTemperature()));
        mWindSpeed.setText(String.valueOf(mWeatherData.getWindSpeed()));
        mVisibility.setText(String.valueOf(mWeatherData.getVisibility()));
        initialiseWeather(mWeatherDailyList);
        return rootView;
    }

    private void initialiseWeather(ArrayList<DarkSkyDaily> weatherAPIArrayList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mWeatherAdapter = new WeatherAdapter(weatherAPIArrayList, getContext());
        mWeatherRecyclerView.setAdapter(mWeatherAdapter);
        mWeatherRecyclerView.setLayoutManager(linearLayoutManager);
    }
}
