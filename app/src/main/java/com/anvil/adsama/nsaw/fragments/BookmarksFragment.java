package com.anvil.adsama.nsaw.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.adapters.BookmarkNewsAdapter;
import com.anvil.adsama.nsaw.adapters.BookmarkStockAdapter;
import com.anvil.adsama.nsaw.database.NsawContract;
import com.anvil.adsama.nsaw.model.AlphaVantage;
import com.anvil.adsama.nsaw.model.NewsAPI;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarksFragment extends Fragment {

    private static final int NEWS_CURSOR_LOADER_ID = 1;
    private static final int STOCK_CURSOR_LOADER_ID = 2;
    ArrayList<NewsAPI> newsData = new ArrayList<>();
    ArrayList<AlphaVantage> stockData = new ArrayList<>();
    @BindView(R.id.rv_news_horizontal)
    RecyclerView newsRecyclerView;
    @BindView(R.id.rv_stock_horizontal)
    RecyclerView stockRecyclerView;
    @BindView(R.id.cl_bookmark)
    ConstraintLayout mainLayout;
    @BindView(R.id.fl_empty)
    FrameLayout emptyLayout;
    BookmarkNewsAdapter newsAdapter;
    BookmarkStockAdapter stockAdapter;

    public BookmarksFragment() {
    }

    private LoaderManager.LoaderCallbacks<Cursor> newsLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            if (getContext() != null) {
                return new CursorLoader(getContext(), NsawContract.NsawEntry.NEWS_CONTENT_URI, NsawContract.NsawEntry.NEWS_COLUMNS, null, null, null);
            }
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            if (data.getCount() > 0) {
                newsAdapter.addNews(data);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            if (newsData != null) {
                newsAdapter.notifyDataSetChanged();
            } else {
                mainLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            }
        }
    };

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

    private LoaderManager.LoaderCallbacks<Cursor> stockLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            if (getContext() != null) {
                return new CursorLoader(getContext(), NsawContract.NsawEntry.STOCK_CONTENT_URI, NsawContract.NsawEntry.STOCK_COLUMNS, null, null, null);
            }
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            if (data.getCount() > 0) {
                stockAdapter.addStock(data);
            } else {
                mainLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            if (stockData != null) {
                stockAdapter.notifyDataSetChanged();
            } else {
                mainLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        ButterKnife.bind(this, rootView);
        initNewsAdapter();
        initStockAdapter();
        getLoaderManager().initLoader(NEWS_CURSOR_LOADER_ID, null, newsLoader);
        getLoaderManager().initLoader(STOCK_CURSOR_LOADER_ID, null, stockLoader);
        return rootView;
    }
}