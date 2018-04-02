package com.anvil.adsama.nsaw.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.activities.DetailActivity;
import com.anvil.adsama.nsaw.adapters.StockPositionInterface;
import com.anvil.adsama.nsaw.adapters.StockRecyclerAdapter;
import com.anvil.adsama.nsaw.analytics.NsawApp;
import com.anvil.adsama.nsaw.model.AlphaVantage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockFragment extends Fragment implements StockPositionInterface {

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
    StockRecyclerAdapter mStockAdapter;
    ArrayList<AlphaVantage> mStockData;

    public StockFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        initialiseStockData();
        if (mStockData != null) {
            mCompanyText.setText(mStockData.get(0).getCompanyName());
            mOpenText.setText(String.valueOf(mStockData.get(0).getOpen()));
            mCloseText.setText(String.valueOf(mStockData.get(0).getClose()));
            mRefreshText.setText(String.valueOf(mStockData.get(0).getRefreshTime()));
        }
        return rootView;
    }

    private void initialiseStockData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mStockAdapter = new StockRecyclerAdapter(mStockData, getContext(), this);
        mStockRecyclerView.setAdapter(mStockAdapter);
        mStockRecyclerView.setLayoutManager(linearLayoutManager);
        mStockRecyclerView.setNestedScrollingEnabled(false);
    }

    public void refreshAdapter() {
        if (mStockAdapter != null) mStockAdapter.notifyDataSetChanged();
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
}