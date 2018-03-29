package com.anvil.adsama.nsaw.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.activities.DetailActivity;
import com.anvil.adsama.nsaw.analytics.NsawApp;
import com.anvil.adsama.nsaw.model.AlphaVantage;
import com.anvil.adsama.nsaw.model.DarkSkyCurrent;
import com.anvil.adsama.nsaw.model.DarkSkyDaily;
import com.anvil.adsama.nsaw.model.NewsAPI;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    TextView mDetailText, mDateText;
    CollapsingToolbarLayout mAppBarLayout;
    ImageView mCollapsingImageView;
    ArrayList<NewsAPI> newsData = new ArrayList<>();
    ArrayList<AlphaVantage> stockData = new ArrayList<>();
    ArrayList<DarkSkyCurrent> weatherCurrentData = new ArrayList<>();
    ArrayList<DarkSkyDaily> weatherDailyData = new ArrayList<>();
    int newsPosition;
    int stockPosition;
    int weatherPosition;

    public DetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            newsPosition = savedInstanceState.getInt("News Position");
            stockPosition = savedInstanceState.getInt("Stock Position");
            weatherPosition = savedInstanceState.getInt("Weather Position");
            newsData = savedInstanceState.getParcelableArrayList("News List");
            stockData = savedInstanceState.getParcelableArrayList("Stock List");
            weatherCurrentData = savedInstanceState.getParcelableArrayList("Weather List");
            if (weatherCurrentData != null) {
                weatherDailyData = weatherCurrentData.get(weatherPosition).getDailyList();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            mAppBarLayout = activity.findViewById(R.id.toolbar_layout);
            mCollapsingImageView = activity.findViewById(R.id.iv_backdrop);
            if (mCollapsingImageView != null) {
                Picasso.with(getContext()).load(newsData.get(newsPosition).getImageUrl()).into(mCollapsingImageView);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mDetailText = rootView.findViewById(R.id.tv_detail_text);
        mDateText = rootView.findViewById(R.id.tv_detail_date);
        if (newsData != null) {
            displayNewsData();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            FloatingActionButton storingButton = getActivity().findViewById(R.id.storing_fab);
            storingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "STORE KARAAA", Toast.LENGTH_SHORT).show();
                }
            });
            FloatingActionButton sharingButton = getActivity().findViewById(R.id.sharing_fab);
            sharingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "SHARE KARAAA", Toast.LENGTH_SHORT).show();
                }
            });
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_view);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_bottom_news:
                            Toast.makeText(getContext(), "NOOJ", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.action_bottom_stock:
                            Toast.makeText(getContext(), "STOOCK", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.action_bottom_weather:
                            Toast.makeText(getContext(), "WAAAYTHUR", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            });
        }
    }

    private void displayNewsData() {
        NewsAPI newsAPI = newsData.get(newsPosition);
        if (newsAPI != null) {
            Activity activity = getActivity();
            if (mAppBarLayout != null && activity != null && activity instanceof DetailActivity) {
                mAppBarLayout.setTitle(newsAPI.getTitle());
            }
            mDateText.setText(newsAPI.getDate());
            mDetailText.setText(newsAPI.getDescription());
        }
        Toast.makeText(getContext(), "FRAGMENT ADDED " + String.valueOf(newsAPI), Toast.LENGTH_SHORT).show();
    }

    private void displayStockData() {
        AlphaVantage alphaVantage = stockData.get(stockPosition);
    }

    private void displayWeatherData() {
        DarkSkyCurrent skyCurrent = weatherCurrentData.get(weatherPosition);
        DarkSkyDaily skyDaily = weatherDailyData.get(weatherPosition);
    }

    @Override
    public void onResume() {
        super.onResume();
        NsawApp.getInstance().trackScreenView("DETAIL FRAGMENT");
    }
}