package com.anvil.adsama.nsaw.network;

import com.anvil.adsama.nsaw.model.DarkSky;

import java.util.ArrayList;

public interface WeatherListener {
    void returnWeatherList(ArrayList<DarkSky> darkSkyList);
}