package com.anvil.adsama.nsaw.network;

import com.anvil.adsama.nsaw.model.DarkSkyCurrent;

import java.util.ArrayList;

public interface WeatherListener {
    void returnWeatherList(ArrayList<DarkSkyCurrent> darkSkyList);
}