package com.anvil.adsama.nsaw.network;

import com.anvil.adsama.nsaw.model.NewsAPI;

import java.util.ArrayList;

public interface NewsListener {
    void returnNewsList(ArrayList<NewsAPI> newsAPIList);
}