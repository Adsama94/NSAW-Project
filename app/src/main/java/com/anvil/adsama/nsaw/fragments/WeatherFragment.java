package com.anvil.adsama.nsaw.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.adapters.WeatherAdapter;
import com.anvil.adsama.nsaw.model.DarkSky;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherFragment extends android.support.v4.app.Fragment {

    @BindView(R.id.recycler_weather)
    RecyclerView mWeatherRecyclerView;
    ArrayList<DarkSky> mWeatherList;
    WeatherAdapter mWeatherAdapter;

    public WeatherFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState = getArguments();
        mWeatherList = new ArrayList<>();
        if (savedInstanceState != null)
            mWeatherList = savedInstanceState.getParcelableArrayList("DarkSky");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_layout_main, container, false);
        ButterKnife.bind(this, rootView);
        initialiseWeather(mWeatherList);
        return rootView;
    }

    private void initialiseWeather(ArrayList<DarkSky> weatherAPIArrayList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mWeatherAdapter = new WeatherAdapter(weatherAPIArrayList, getContext());
        mWeatherRecyclerView.setAdapter(mWeatherAdapter);
        mWeatherRecyclerView.setLayoutManager(linearLayoutManager);
    }
}
