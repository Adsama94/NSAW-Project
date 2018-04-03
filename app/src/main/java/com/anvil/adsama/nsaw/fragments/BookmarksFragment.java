package com.anvil.adsama.nsaw.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.adapters.BookmarkNewsAdapter;
import com.anvil.adsama.nsaw.adapters.BookmarkStockAdapter;
import com.anvil.adsama.nsaw.adapters.BookmarkWeatherAdapter;
import com.anvil.adsama.nsaw.database.NsawContract;
import com.anvil.adsama.nsaw.model.AlphaVantage;
import com.anvil.adsama.nsaw.model.DarkSkyCurrent;
import com.anvil.adsama.nsaw.model.DarkSkyDaily;
import com.anvil.adsama.nsaw.model.NewsAPI;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarksFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER_ID = 1;
    ArrayList<NewsAPI> newsData = new ArrayList<>();
    ArrayList<AlphaVantage> stockData = new ArrayList<>();
    ArrayList<DarkSkyCurrent> weatherCurrentData = new ArrayList<>();
    ArrayList<DarkSkyDaily> weatherDailyData = new ArrayList<>();
    @BindView(R.id.rv_news_horizontal)
    RecyclerView newsRecyclerView;
    @BindView(R.id.rv_stock_horizontal)
    RecyclerView stockRecyclerView;
    @BindView(R.id.rv_weather_horizontal)
    RecyclerView weatherRecyclerView;
    BookmarkNewsAdapter newsAdapter;
    BookmarkStockAdapter stockAdapter;
    BookmarkWeatherAdapter weatherAdapter;

    public BookmarksFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        ButterKnife.bind(this, rootView);
        initNewsAdapter();
        initStockAdapter();
        initWeatherAdapter();
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        return rootView;
    }

    private void initNewsAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        newsAdapter = new BookmarkNewsAdapter(getContext(), newsData);
        newsRecyclerView.setAdapter(newsAdapter);
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        newsRecyclerView.setNestedScrollingEnabled(false);
    }

    private void initStockAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        stockAdapter = new BookmarkStockAdapter(getContext(), stockData);
        stockRecyclerView.setAdapter(stockAdapter);
        stockRecyclerView.setLayoutManager(linearLayoutManager);
        stockRecyclerView.setNestedScrollingEnabled(false);
    }

    private void initWeatherAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        weatherAdapter = new BookmarkWeatherAdapter(getContext(), weatherCurrentData);
        weatherRecyclerView.setAdapter(weatherAdapter);
        weatherRecyclerView.setLayoutManager(linearLayoutManager);
        weatherRecyclerView.setNestedScrollingEnabled(false);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (newsData != null) {
            if (getContext() != null)
                return new CursorLoader(getContext(), NsawContract.NsawEntry.NEWS_CONTENT_URI, NsawContract.NsawEntry.NEWS_COLUMNS, null, null, null);
        } else if (stockData != null) {
            if (getContext() != null)
                return new CursorLoader(getContext(), NsawContract.NsawEntry.STOCK_CONTENT_URI, NsawContract.NsawEntry.STOCK_COLUMNS, null, null, null);
        } else if (weatherCurrentData != null) {
            if (getContext() != null)
                return new CursorLoader(getContext(), NsawContract.NsawEntry.WEATHER_CURRENT_CONTENT_URI, NsawContract.NsawEntry.CURRENT_COLUMNS, null, null, null);
        } else if (weatherDailyData != null) {
            if (getContext() != null)
                return new CursorLoader(getContext(), NsawContract.NsawEntry.WEATHER_CURRENT_CONTENT_URI, NsawContract.NsawEntry.DAILY_COLUMNS, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data.getCount() > 0) {
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (newsData != null) {
            newsAdapter.notifyDataSetChanged();
        } else if (stockData != null) {
            stockAdapter.notifyDataSetChanged();
        } else if (weatherDailyData != null) {
            weatherAdapter.notifyDataSetChanged();
        }
    }
}