package com.anvil.adsama.nsaw.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.activities.DetailActivity;
import com.anvil.adsama.nsaw.adapters.StockPositionInterface;
import com.anvil.adsama.nsaw.adapters.StockRecyclerAdapter;
import com.anvil.adsama.nsaw.analytics.NsawApp;
import com.anvil.adsama.nsaw.model.AlphaVantage;
import com.anvil.adsama.nsaw.network.CompanyList;
import com.anvil.adsama.nsaw.network.StockLoader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockFragment extends Fragment implements StockPositionInterface {

    private static final int STOCK_LOADER_ID = 4;
    @BindView(R.id.recycler_stock)
    RecyclerView mStockRecyclerView;
    @BindView(R.id.text_company)
    TextView mCompanyText;
    @BindView(R.id.actual_open)
    TextView mOpenText;
    @BindView(R.id.actual_close)
    TextView mCloseText;
    @BindView(R.id.text_refresh)
    TextView mRefreshText;
    @BindView(R.id.cl_loading)
    ConstraintLayout mLoadingLayout;
    @BindView(R.id.cv_stock_primary)
    CardView mPrimaryLayout;
    private StockRecyclerAdapter mStockAdapter;
    private ArrayList<AlphaVantage> mStockData;
    private String searchText;
    private String updatedShit;
    private SearchView searchView;

    public StockFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stock, container, false);
        ButterKnife.bind(this, rootView);
        getLoaderManager().initLoader(STOCK_LOADER_ID, null, stockLoader);
        return rootView;
    }

    private android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<AlphaVantage>> stockLoader = new android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<AlphaVantage>>() {
        @NonNull
        @Override
        public android.support.v4.content.Loader<ArrayList<AlphaVantage>> onCreateLoader(int id, @Nullable Bundle args) {
            showProgress();
            if (searchText != null) {
                return new StockLoader(getContext(), makeStockSearchUrl());
            } else {
                return new StockLoader(getContext(), null);
            }
        }

        @Override
        public void onLoadFinished(@NonNull android.support.v4.content.Loader<ArrayList<AlphaVantage>> loader, ArrayList<AlphaVantage> data) {
            hideProgress();
            mStockData = data;
            initialiseStockData(mStockData);
            setStockData(mStockData);
        }

        @Override
        public void onLoaderReset(@NonNull android.support.v4.content.Loader<ArrayList<AlphaVantage>> loader) {
            mStockAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Search", searchView.getQuery().toString());
    }

    private void setStockData(ArrayList<AlphaVantage> stockData) {
        if (stockData != null) {
            mCompanyText.setText(stockData.get(0).getCompanyName());
            mOpenText.setText(String.valueOf(stockData.get(0).getOpen()));
            mCloseText.setText(String.valueOf(stockData.get(0).getClose()));
            mRefreshText.setText(String.valueOf(stockData.get(0).getRefreshTime()));
        }
    }

    private void initialiseStockData(ArrayList<AlphaVantage> stockData) {
        if (stockData != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mStockAdapter = new StockRecyclerAdapter(stockData, getContext(), this);
            mStockRecyclerView.setAdapter(mStockAdapter);
            mStockRecyclerView.setLayoutManager(linearLayoutManager);
            mStockRecyclerView.setNestedScrollingEnabled(false);
        }
    }

    public void showProgress() {
        mPrimaryLayout.setVisibility(View.GONE);
        mStockRecyclerView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mPrimaryLayout.setVisibility(View.VISIBLE);
        mStockRecyclerView.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null)
            updatedShit = savedInstanceState.getString("Search");
    }

    private boolean readStockList() {
        InputStream inputStream = getResources().openRawResource(R.raw.alphacompany);
        CompanyList csvFile = new CompanyList(inputStream);
        List scoreList = csvFile.read();
        return scoreList.contains(searchText);
    }

    public String makeStockSearchUrl() {
        return "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + searchText + "&outputsize=compact&apikey=A3G413PBID5IBS0W";
    }

    @Override
    public void onResume() {
        super.onResume();
        NsawApp.getInstance().trackScreenView("STOCK FRAGMENT");
    }

    @Override
    public void getStockPosition(int position) {
        Intent detailIntent = new Intent(getContext(), DetailActivity.class);
        detailIntent.putExtra("Stock Position", position);
        detailIntent.putExtra("UID STOCK", "FROM STOCK");
        detailIntent.putParcelableArrayListExtra("Stock List", mStockData);
        startActivity(detailIntent);
    }

    private void showErrorBar(View errorView) {
        Snackbar snackbar = Snackbar.make(errorView, "UNREACHABLE QUERY", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        if (getContext() != null)
            snackBarView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        snackbar.show();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        if (!TextUtils.isEmpty(updatedShit)) {
            searchMenuItem.expandActionView();
            searchView.setQuery(updatedShit, false);
            searchView.clearFocus();
        }
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchText = query;
                if (readStockList()) {
                    getLoaderManager().restartLoader(STOCK_LOADER_ID, null, stockLoader);
                    searchView.clearFocus();
                } else {
                    searchView.clearFocus();
                    showErrorBar(searchView);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}