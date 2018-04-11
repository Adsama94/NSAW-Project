package com.anvil.adsama.nsaw.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.activities.DetailActivity;
import com.anvil.adsama.nsaw.adapters.StockPositionInterface;
import com.anvil.adsama.nsaw.adapters.StockRecyclerAdapter;
import com.anvil.adsama.nsaw.analytics.NsawApp;
import com.anvil.adsama.nsaw.model.AlphaVantage;
import com.anvil.adsama.nsaw.network.StockListener;
import com.anvil.adsama.nsaw.network.StockSearchTask;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockFragment extends Fragment implements StockPositionInterface, StockListener {

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
    @BindView(R.id.fl_loading)
    FrameLayout mLoadingLayout;
    @BindView(R.id.cv_stock_primary)
    CardView mPrimaryLayout;
    private StockRecyclerAdapter mStockAdapter;
    private ArrayList<AlphaVantage> mStockData;
    private String searchText;

    public StockFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mStockData = new ArrayList<>();
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            mStockData = savedInstanceState.getParcelableArrayList("AlphaVantage");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stock, container, false);
        ButterKnife.bind(this, rootView);
        if (mStockData != null) {
            initialiseStockData(mStockData);
            setStockData(mStockData);
        }
        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchText = query;
                StockSearchTask stockSearchTask = new StockSearchTask(StockFragment.this, StockFragment.this);
                stockSearchTask.execute(makeStockSearchUrl());
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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

    public void refreshAdapter() {
        if (mStockAdapter != null) mStockAdapter.notifyDataSetChanged();
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
        refreshAdapter();
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

    @Override
    public void returnStockList(ArrayList<AlphaVantage> alphaVantageList) {
        mStockData = alphaVantageList;
        refreshAdapter();
        initialiseStockData(mStockData);
        setStockData(mStockData);
    }
}